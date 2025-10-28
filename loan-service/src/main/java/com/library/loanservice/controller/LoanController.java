package com.library.loanservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoanController {

    @GetMapping("/api/loans/test")
    public String testLoan()
    {
        return "Loan Service is working!";
    }
}
