package com.library.readerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReaderDTO {

    private Long id;
    private String name;
    private String email;
    private LocalDate membershipDate;
}
