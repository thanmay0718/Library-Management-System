package com.library.management.controller;

import com.library.management.payload.BookRequestDTO;
import com.library.management.payload.BookResponseDTO;
import com.library.management.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/addBook")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookResponseDTO> addBook(@Valid @RequestBody BookRequestDTO book){
        BookResponseDTO savedBook = bookService.addBook(book);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedBook);
    }

    @GetMapping("/getBook")
    public ResponseEntity<List<BookResponseDTO>> getBooks(){
        List<BookResponseDTO> list = bookService.getAllBooks();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id){
        BookResponseDTO book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @PutMapping("/updateBook/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(@Valid @PathVariable Long id, @RequestBody BookRequestDTO book){
        BookResponseDTO updatedBook = bookService.updateBook(id, book);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/deleteBook/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok("Book with ID " + id + " was successfully deleted.");
    }

}
