package com.library.loanservice.service;

import com.library.loanservice.client.ReaderClient;
import com.library.loanservice.dto.LoanCreateDTO;
import com.library.loanservice.dto.LoanDTO;
import com.library.loanservice.dto.LoanWithReaderDTO;
import com.library.loanservice.dto.ReaderDTO;
import com.library.loanservice.model.Loan;
import com.library.loanservice.repository.LoanRepository;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final ReaderClient readerClient;

    public LoanService(LoanRepository loanRepository, ReaderClient readerClient) {
        this.loanRepository = loanRepository;
        this.readerClient = readerClient;
    }

    // GET /api/loans
    public List<LoanDTO> findAll() {
        return loanRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // GET /api/loans/{id}
    public Optional<LoanDTO> findById(Long id) {
        return loanRepository.findById(id)
                .map(this::convertToDTO);
    }

    // POST /api/loans
    public LoanDTO create(LoanCreateDTO dto) {

        try {

            readerClient.getReaderById(dto.getReaderId());

        } catch (FeignException.NotFound ex) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Reader with ID " + dto.getReaderId() + " not found. Cannot create loan.",
                    ex
            );
        }

        Loan loan = Loan.builder()
                .readerId(dto.getReaderId())
                .bookTitle(dto.getBookTitle())
                .loanDate(dto.getLoanDate())
                .returnDate(dto.getReturnDate())
                .build();

        return convertToDTO(loanRepository.save(loan));
    }

    // PUT /api/loans/{id}
    public Optional<LoanDTO> update(Long id, LoanCreateDTO dto) {

        return loanRepository.findById(id).map(existingLoan -> {

            try {
                readerClient.getReaderById(dto.getReaderId());
            } catch (FeignException.NotFound ex) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Reader with ID " + dto.getReaderId() + " not found. Cannot update loan.",
                        ex
                );
            }

            existingLoan.setReaderId(dto.getReaderId());
            existingLoan.setBookTitle(dto.getBookTitle());
            existingLoan.setLoanDate(dto.getLoanDate());
            existingLoan.setReturnDate(dto.getReturnDate());

            return convertToDTO(loanRepository.save(existingLoan));
        });
    }




    // DELETE /api/loans/{id}
    public void deleteById(Long id) {
        loanRepository.deleteById(id);
    }

    private LoanDTO convertToDTO(Loan loan) {
        return LoanDTO.builder()
                .id(loan.getId())
                .readerId(loan.getReaderId())
                .bookTitle(loan.getBookTitle())
                .loanDate(loan.getLoanDate())
                .returnDate(loan.getReturnDate())
                .build();
    }

    private Loan convertToEntity(LoanCreateDTO dto) {
        return Loan.builder()
                .readerId(dto.getReaderId())
                .bookTitle(dto.getBookTitle())
                .loanDate(dto.getLoanDate())
                .returnDate(dto.getReturnDate())
                .build();
    }


    @CircuitBreaker(name = "readerServiceCB", fallbackMethod = "getLoanWithReaderFallback")
    @Retry(name = "readerServiceRetry")
    public Optional<LoanWithReaderDTO> getLoanWithReader(Long loanId) {
        return loanRepository.findById(loanId)
                .map(loan -> {
                    ResponseEntity<ReaderDTO> response = readerClient.getReaderById(loan.getReaderId());

                    if (response.getStatusCode().is5xxServerError()) {
                        throw new ResponseStatusException(response.getStatusCode(), "Reader Service unavailable.");
                    }

                    ReaderDTO reader;
                    if (response.getStatusCode() == HttpStatus.NOT_FOUND || response.getBody() == null) {
                        reader = new ReaderDTO(0L, "UNKNOWN", "UNKNOWN", null);
                    } else {
                        reader = response.getBody();
                    }

                    return LoanWithReaderDTO.builder()
                            .loanId(loan.getId())
                            .bookTitle(loan.getBookTitle())
                            .loanDate(loan.getLoanDate())
                            .returnDate(loan.getReturnDate())
                            .reader(reader)
                            .build();
                });
    }

    public Optional<LoanWithReaderDTO> getLoanWithReaderFallback(Long loanId, Throwable t) {
        return loanRepository.findById(loanId)
                .map(loan -> LoanWithReaderDTO.builder()
                        .loanId(loan.getId())
                        .bookTitle(loan.getBookTitle())
                        .loanDate(loan.getLoanDate())
                        .returnDate(loan.getReturnDate())
                        .reader(new ReaderDTO(0L, "UNKNOWN", "UNKNOWN", null)) // fallback vrednosti
                        .build());
    }
}
