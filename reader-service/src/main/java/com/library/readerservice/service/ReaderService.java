package com.library.readerservice.service;

import com.library.readerservice.dto.ReaderCreateDTO;
import com.library.readerservice.dto.ReaderDTO;
import com.library.readerservice.model.Reader;
import com.library.readerservice.repository.ReaderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReaderService {

    private final ReaderRepository readerRepository;

    public ReaderService(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }


    public List<ReaderDTO> findAll() {
        return readerRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<ReaderDTO> findById(Long id) {
        return readerRepository.findById(id)
                .map(this::toDTO);
    }

    public ReaderDTO create(ReaderCreateDTO dto) {
        Reader reader = Reader.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .membershipDate(dto.getMembershipDate())
                .build();

        return toDTO(readerRepository.save(reader));
    }

    public Optional<ReaderDTO> update(Long id, ReaderCreateDTO dto) {
        return readerRepository.findById(id)
                .map(existing -> {
                    existing.setName(dto.getName());
                    existing.setEmail(dto.getEmail());
                    existing.setMembershipDate(dto.getMembershipDate());
                    return toDTO(readerRepository.save(existing));
                });
    }

    public boolean deleteById(Long id) {
        if (readerRepository.existsById(id)) {
            readerRepository.deleteById(id);
            return true;
        }
        return false;
    }


    private ReaderDTO toDTO(Reader reader) {
        return ReaderDTO.builder()
                .id(reader.getId())
                .name(reader.getName())
                .email(reader.getEmail())
                .membershipDate(reader.getMembershipDate())
                .build();
    }
}
