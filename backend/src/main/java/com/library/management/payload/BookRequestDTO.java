package com.library.management.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDTO {

        @NotBlank(message = "Book title is required")
        @Size(max = 150, message = "Title cannot exceed 150 characters")
        private String title;

        @NotBlank(message = "Author name is required")
        @Size(max = 100, message = "Author name cannot exceed 100 characters")
        private String author;

        @NotBlank(message = "ISBN is required")
        private String isbn;

        @NotBlank(message = "Category is required")
        private String category;

        @NotNull(message = "Total copies is required")
        @Min(value = 1, message = "Total copies must be at least 1")
        private Integer totalCopies;

        @NotNull(message = "Available copies is required")
        @Min(value = 0, message = "Available copies cannot be negative")
        private Integer availableCopies;
}