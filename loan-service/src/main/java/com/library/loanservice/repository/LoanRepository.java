package com.library.loanservice.repository;

import com.library.loanservice.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
