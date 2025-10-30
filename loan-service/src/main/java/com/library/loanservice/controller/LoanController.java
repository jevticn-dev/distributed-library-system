package com.library.loanservice.controller;

import com.library.loanservice.dto.LoanCreateDTO;
import com.library.loanservice.dto.LoanDTO;
import com.library.loanservice.dto.LoanWithReaderDTO;
import com.library.loanservice.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    // GET /api/loans
    @GetMapping
    public List<LoanDTO> getAll() {
        return loanService.findAll();
    }

    // GET /api/loans/{id}
    @GetMapping("/{id}")
    public ResponseEntity<LoanDTO> getById(@PathVariable Long id) {
        return loanService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/loans
    @PostMapping
    public ResponseEntity<LoanDTO> create(@Valid @RequestBody LoanCreateDTO loanCreateDTO) {
        LoanDTO createdLoan = loanService.create(loanCreateDTO);
        return ResponseEntity.ok(createdLoan);
    }

    // PUT /api/loans/{id}
    @PutMapping("/{id}")
    public ResponseEntity<LoanDTO> update(@PathVariable Long id, @Valid @RequestBody LoanCreateDTO loanCreateDTO) {
        return loanService.update(id, loanCreateDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/loans/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (loanService.findById(id).isPresent()) {
            loanService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/with-reader")
    public ResponseEntity<LoanWithReaderDTO> getLoanWithReader(@PathVariable Long id) {
        return loanService.getLoanWithReader(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
