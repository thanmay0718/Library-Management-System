package com.library.management.service;

import com.library.management.entity.LoanStatus;
import com.library.management.payload.LoanRequestDTO;
import com.library.management.payload.LoanResponseDTO;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface LoanService {

    LoanResponseDTO borrowBook(LoanRequestDTO requestDTO) throws AccessDeniedException;

    LoanResponseDTO returnBook(Long oanId) throws AccessDeniedException;

    LoanResponseDTO renewLoan(Long loanId) throws AccessDeniedException;

    List<LoanResponseDTO> getMyLoans() throws AccessDeniedException;

    List<LoanResponseDTO> getOverdueLoans() throws AccessDeniedException;
}
