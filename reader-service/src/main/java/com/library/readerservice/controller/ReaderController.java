package com.library.readerservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReaderController {
    @GetMapping("/api/readers/test")
    public String test()
    {
        return "Reader Service is working!";
    }
}
