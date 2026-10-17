package com.library.management.repository;

import com.library.management.entity.Book;
import com.library.management.entity.Loan;
import com.library.management.entity.LoanStatus;
import com.library.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    // Get all loans for a particular user
    List<Loan> findByUser(User user);

    // Get the borrowing history of a book
    List<Loan> findByBook(Book book);

    // Find a user's active or returned loans
    List<Loan> findByUserAndStatus(User user, LoanStatus status);

    // Find all borrowed, returned, or overdue loans
    List<Loan> findByStatus(LoanStatus status);

    // Ensure a user accesses only their own loan
    Optional<Loan> findByIdAndUser(Long id, User user);

    // Count active loans to enforce borrowing limits
    long countByUserAndStatus(User user, LoanStatus status);
}
