package com.library.management.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Book{
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotBlank(message = "Title is required")
  @Size(max = 150, message = "Title cannot be exceed more than 150 characters")
  private String title;

  @NotBlank(message = "Author is required")
  private String author;

  @NotBlank(message = "ISBN is required")
  private String isbn;

  @NotBlank(message = "Category is required")
  private String category;

  @Min(value = 0, message = "Total copies cannot be negative")
  private int totalCopies;

  @Min(value = 0, message = "Available copies cannot be negative")
  private int availableCopies;
}