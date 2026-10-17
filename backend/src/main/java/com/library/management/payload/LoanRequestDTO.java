package com.library.management.payload;

import com.library.management.entity.Loan;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanRequestDTO {

    @NotNull (message = "Book ID is required")
    @Positive(message = "Book ID must be positive")
    private Long bookId;
}
