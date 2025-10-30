package com.library.loanservice.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanWithReaderDTO {
    private Long loanId;
    private String bookTitle;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private ReaderDTO reader;
}
