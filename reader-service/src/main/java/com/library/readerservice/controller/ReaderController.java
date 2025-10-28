package com.library.readerservice.controller;

import com.library.readerservice.dto.ReaderCreateDTO;
import com.library.readerservice.dto.ReaderDTO;
import com.library.readerservice.service.ReaderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readers")
public class ReaderController {

    private final ReaderService readerService;

    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @GetMapping
    public ResponseEntity<List<ReaderDTO>> findAll() {
        List<ReaderDTO> readers = readerService.findAll();
        return ResponseEntity.ok(readers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReaderDTO> getById(@PathVariable Long id) {
        return readerService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ReaderDTO> create(@Valid @RequestBody ReaderCreateDTO readerCreateDTO) {
        ReaderDTO created = readerService.create(readerCreateDTO);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReaderDTO> update(@PathVariable Long id, @Valid @RequestBody ReaderCreateDTO readerCreateDTO) {
        return readerService.update(id, readerCreateDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (readerService.findById(id).isPresent()) {
            readerService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
