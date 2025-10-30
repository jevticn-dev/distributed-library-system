package com.library.loanservice.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanCreateDTO {

    @NotNull(message = "Reader ID is required")
    private Long readerId;

    @NotBlank(message = "Book title is required")
    private String bookTitle;

    @NotNull(message = "LoanDate is required")
    private LocalDate loanDate;

    private LocalDate returnDate;
}
