package com.library.loanservice.client;


import com.library.loanservice.dto.ReaderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "READER-SERVICE")
public interface ReaderClient {

    @GetMapping("/api/readers/{id}")
    ResponseEntity<ReaderDTO> getReaderById(@PathVariable("id") Long id);
}
