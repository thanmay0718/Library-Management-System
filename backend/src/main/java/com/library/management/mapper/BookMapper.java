package com.library.management.mapper;

import com.library.management.entity.Book;
import com.library.management.payload.BookRequestDTO;
import com.library.management.payload.BookResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    private static ModelMapper modelMapper;

    public BookMapper(ModelMapper modelMapper){
        BookMapper.modelMapper = modelMapper;
    }

    public Book toEntity(BookRequestDTO dto){
        return modelMapper.map(dto, Book.class);
    }

    public BookResponseDTO toResponseDTO(Book book){
        return modelMapper.map(book, BookResponseDTO.class);
    }
}
