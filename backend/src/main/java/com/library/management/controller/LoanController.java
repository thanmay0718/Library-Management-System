
package com.library.management.controller;

import com.library.management.payload.LoanRequestDTO;
import com.library.management.payload.LoanResponseDTO;
import com.library.management.service.LoanService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/borrow")
    public ResponseEntity<LoanResponseDTO> borrowBook(
            @Valid @RequestBody LoanRequestDTO request) throws AccessDeniedException {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(loanService.borrowBook(request));
    }

    @PutMapping("/{loanId}/return")
    public ResponseEntity<LoanResponseDTO> returnBook(
            @PathVariable Long loanId) throws AccessDeniedException {

        return ResponseEntity.ok(loanService.returnBook(loanId));
    }

    @PutMapping("/{loanId}/renew")
    public ResponseEntity<LoanResponseDTO> renewLoan(
            @PathVariable Long loanId) throws AccessDeniedException {

        return ResponseEntity.ok(loanService.renewLoan(loanId));
    }

    @GetMapping("/my-loans")
    public ResponseEntity<List<LoanResponseDTO>> getMyLoans() throws AccessDeniedException {

        return ResponseEntity.ok(loanService.getMyLoans());
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LoanResponseDTO>> getOverdueLoans() throws AccessDeniedException {

        return ResponseEntity.ok(loanService.getOverdueLoans());
    }
}
