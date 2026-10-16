package com.library.management.service;

import com.library.management.entity.Book;
import com.library.management.payload.BookRequestDTO;
import com.library.management.payload.BookResponseDTO;

import java.util.List;


public interface BookService {
    BookResponseDTO addBook(BookRequestDTO bookRequestDTO);

    List<BookResponseDTO> getAllBooks();

    BookResponseDTO getBookById(Long id);

    BookResponseDTO updateBook(Long id, BookRequestDTO bookRequestDTO);

    String deleteBook(Long id);
}
