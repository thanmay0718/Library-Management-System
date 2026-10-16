package com.library.management.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDTO {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String category;
    private Integer totalCopies;
    private Integer availableCopies;
}
