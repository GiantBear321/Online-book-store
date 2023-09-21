package com.example.onlinebookstore.service;

import com.example.onlinebookstore.dto.book.BookDto;
import com.example.onlinebookstore.dto.book.BookSearchParameters;
import com.example.onlinebookstore.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto updateById(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters parameters);
}
