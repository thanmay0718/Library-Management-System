package com.library.management.service.ServiceImpl;

import com.library.management.entity.Book;
import com.library.management.exception.ResourceNotFoundException;
import com.library.management.mapper.BookMapper;
import com.library.management.payload.BookRequestDTO;
import com.library.management.payload.BookResponseDTO;
import com.library.management.repository.BookRepository;
import com.library.management.service.BookService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;
@Service
public class BookServiceImpl implements BookService {

    private static final Logger logger = LoggerFactory
                    .getLogger(BookServiceImpl.class);

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper){
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookResponseDTO addBook(BookRequestDTO book) {
        logger.info("Adding a new book : {}", book.getTitle());
        Book book1 = bookMapper.toEntity(book);
        Book savedBook =  bookRepository.save(book1);
        logger.info("Book added successfully with id : {}", savedBook.getId());
        return bookMapper.toResponseDTO(savedBook);
    }

    @Override
    public List<BookResponseDTO> getAllBooks() {
        logger.info("Fetching all books");
        List<BookResponseDTO> books = bookRepository.findAll()
                        .stream()
                                .map(bookMapper::toResponseDTO)
                                        .toList();
        logger.info("Total books fetched: {}", books.size());
        return books;
    }

    @Override
    public BookResponseDTO getBookById(Long id) {

        logger.info("Fetching book with ID: {}", id);

        Book book = findBookEntityById(id);
        return bookMapper.toResponseDTO(book);
    }

    @Override
    public BookResponseDTO updateBook(Long id, BookRequestDTO updatedBook) {
        logger.info("Updating book with ID: {}", id);
        Book existingBook = findBookEntityById(id);

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setIsbn(updatedBook.getIsbn());
        existingBook.setCategory(updatedBook.getCategory());
        existingBook.setTotalCopies(updatedBook.getTotalCopies());
        existingBook.setAvailableCopies(updatedBook.getAvailableCopies());

        Book savedBook = bookRepository.save(existingBook);
        logger.info("Book updated successfully with ID: {}" ,id);
        return bookMapper.toResponseDTO(savedBook);
    }

    @Override
    public String deleteBook(Long id) {
        logger.info("Deleting book with id: {}", id);
        Book existingBook = findBookEntityById(id);
        bookRepository.delete(existingBook);
        logger.info("Book deleted successfully wit ID: {}", id);
        return "Your " + id + " book has been deleted";
    }

    private Book findBookEntityById(Long id) {

        return bookRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Book not found with ID: {}", id);

                    return new ResourceNotFoundException(
                            "Book not found with id: " + id);
                });
    }
}
