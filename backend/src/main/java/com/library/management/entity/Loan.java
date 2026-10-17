package com.library.management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;



@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private LocalDate borrowDate;

    private  LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status = LoanStatus.BORROWED;

    @Column(nullable = false)
    private LocalDate dueDate;
}


//@ManyToOne: Many loan records can refer to the same user or book.
//        FetchType.LAZY: Related entities are loaded when needed rather than automatically fetching all their details.
//@JoinColumn: Creates the foreign-key columns user_id and book_id.
//        LocalDate: Stores dates without unnecessary time or timezone information.
//@Enumerated(EnumType.STRING): Stores values such as BORROWED in MySQL rather than fragile numeric enum positions.