package com.example.onlinebookstore.controller;

import com.example.onlinebookstore.dto.book.BookDto;
import com.example.onlinebookstore.dto.book.BookSearchParameters;
import com.example.onlinebookstore.dto.book.CreateBookRequestDto;
import com.example.onlinebookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book management", description = "Endpoints for managing books")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/books")
public class BookController {
    private final BookService bookService;

    @Operation(summary = "Find all books", description = "Find all books")
    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public List<BookDto> findAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @Operation(summary = "Save a new book to DB", description = "Save a new book to DB")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public BookDto save(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @Operation(summary = "Find book by id", description = "Find book by id")
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public BookDto findById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @Operation(summary = "Update book data in DB", description = "Update book data in DB")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public BookDto updateById(@PathVariable Long id,
                              @RequestBody @Valid CreateBookRequestDto createBookRequestDto) {
        return bookService.updateById(id, createBookRequestDto);
    }

    @Operation(summary = "Search book's by parameters",
            description = "Search book's by parameters")
    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    public List<BookDto> search(BookSearchParameters parameters) {
        return bookService.search(parameters);
    }

    @Operation(summary = "Delete book by id", description = "Delete book by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(@PathVariable Long id) {
        bookService.deleteById(id);
    }
}
