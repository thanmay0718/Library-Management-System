package com.library.management.service.ServiceImpl;

import com.library.management.entity.Book;
import com.library.management.entity.Loan;
import com.library.management.entity.LoanStatus;
import com.library.management.entity.User;
import com.library.management.exception.ResourceNotFoundException;
import com.library.management.payload.LoanRequestDTO;
import com.library.management.payload.LoanResponseDTO;
import com.library.management.repository.BookRepository;
import com.library.management.repository.LoanRepository;
import com.library.management.repository.UserRepository;
import com.library.management.service.LoanService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    private static final int LOAN_PERIOD_DAYS = 14;
    private static final int RENEWAL_PERIOD_DAYS = 5;

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    public LoanServiceImpl(LoanRepository loanRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public User getAuthenticatedUser() throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated user not found"));
    }

    private boolean isAdmin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN" ::equals);
    }


    private LoanResponseDTO toResponseDTO(Loan loan) {
        return new LoanResponseDTO(
                loan.getId(),
                loan.getUser().getId(),
                loan.getUser().getUsername(),
                loan.getBook().getId(),
                loan.getBook().getTitle(),
                loan.getBorrowDate(),
                loan.getDueDate(),
                loan.getReturnDate(),
                loan.getStatus() == LoanStatus.BORROWED
                    && loan.getDueDate().isBefore(LocalDate.now())
                    ? LoanStatus.OVERDUE
                        : loan.getStatus()
        );
    }


    @Override
    @Transactional
    public LoanResponseDTO borrowBook(LoanRequestDTO requestDTO) throws AccessDeniedException {
        User user = getAuthenticatedUser();

        Book book = bookRepository.findByIdForUpdate(requestDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        if (book.getAvailableCopies() <= 0){
            throw new IllegalStateException("No copies of this Book are currently available");
        }

        LocalDate today = LocalDate.now();

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setBorrowDate(today);
        loan.setDueDate(today.plusDays(LOAN_PERIOD_DAYS));
        loan.setStatus(LoanStatus.BORROWED);

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        Loan savedLoan = loanRepository.save(loan);
        return toResponseDTO(savedLoan);
    }

    @Override
    @Transactional
    public LoanResponseDTO returnBook(Long loanId) throws AccessDeniedException {
        User user = getAuthenticatedUser();

        Loan loan = loanRepository.findByIdAndUser(loanId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Loan not found for the authenticated user"));

        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new IllegalStateException("This book has already been returned");
        }

        Book book = bookRepository.findByIdForUpdate(
                        loan.getBook().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Book not found"));

        loan.setReturnDate(LocalDate.now());
        loan.setStatus(LoanStatus.RETURNED);

        book.setAvailableCopies(
                Math.min(book.getTotalCopies(), book.getAvailableCopies() + 1));

        bookRepository.save(book);

        return toResponseDTO(loanRepository.save(loan));
    }

    @Override
    @Transactional
    public LoanResponseDTO renewLoan(Long loanId) throws AccessDeniedException {
        User user = getAuthenticatedUser();

        Loan loan = loanRepository.findByIdAndUser(loanId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Loan not found for the authenticated user"));

        if (loan.getStatus() != LoanStatus.BORROWED) {
            throw new IllegalStateException(
                    "Only active loans can be renewed");
        }

        if (loan.getDueDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException(
                    "Overdue loans cannot be renewed");
        }

        loan.setDueDate(
                loan.getDueDate().plusDays(RENEWAL_PERIOD_DAYS));

        return toResponseDTO(loanRepository.save(loan));
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<LoanResponseDTO> getMyLoans() throws AccessDeniedException {
        User user = getAuthenticatedUser();

        return loanRepository.findByUser(user)
                .stream()
                .map(this::toResponseDTO)
                .toList();    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<LoanResponseDTO> getOverdueLoans() throws AccessDeniedException {
        User user = getAuthenticatedUser();

        if (user.getRole().name().equals("USER")) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Only administrators can view all overdue loans");
        }

        return loanRepository.findByStatus(LoanStatus.BORROWED)
                .stream()
                .filter(loan ->
                        loan.getDueDate().isBefore(LocalDate.now()))
                .map(this::toResponseDTO)
                .toList();
    }
}


//
//1. @Transactional
//Borrowing a book updates two database records: the book's available-copy count and the loan record. If either operation fails, the transaction rolls back instead of leaving inconsistent data.
//        2. getAuthenticatedUser()
//Your CustomUserDetailsService uses email as the Spring Security username. Therefore, authentication.getName() gives us the authenticated user's email. We look up that user directly rather than trusting a client-provided ID.
//        3. findByIdForUpdate()
//The pessimistic lock prevents concurrent borrowing transactions from both claiming the same last available copy, provided the database supports the lock and the method executes inside a transaction.
//4. Due dates
//- Borrowing: due in 14 days.
//- Renewal: extends the existing due date by 7 days.
//- Overdue: the due date is before today and the book has not been returned.