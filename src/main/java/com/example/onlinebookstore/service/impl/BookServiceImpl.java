package com.example.onlinebookstore.service.impl;

import com.example.onlinebookstore.dto.book.BookDto;
import com.example.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.onlinebookstore.dto.book.BookSearchParameters;
import com.example.onlinebookstore.dto.book.CreateBookRequestDto;
import com.example.onlinebookstore.exceptions.EntityNotFoundException;
import com.example.onlinebookstore.mapper.BookMapper;
import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.model.Category;
import com.example.onlinebookstore.repository.BookRepository;
import com.example.onlinebookstore.repository.CategoryRepository;
import com.example.onlinebookstore.repository.book.spec.BookSpecificationBuilder;
import com.example.onlinebookstore.service.BookService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;
    private final CategoryRepository categoryRepository;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Set<Category> categories = categoryRepository
                .findByIdIn(requestDto.getCategoryIds());
        Book book = bookMapper.toModel(requestDto);
        book.setCategories(categories);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAllWithCategories(pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        return bookRepository.findBookById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by id " + id));
    }

    @Override
    public BookDto updateById(Long id, CreateBookRequestDto requestDto) {
        Set<Category> categories = categoryRepository
                .findByIdIn(requestDto.getCategoryIds());
        Book dbBook = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by id " + id));
        Book updatedBook = bookMapper.toModel(requestDto);
        updatedBook.setId(id);
        updatedBook.setCategories(categories);
        return bookMapper.toDto(bookRepository.save(updatedBook));
    }

    @Override
    public List<BookDto> search(BookSearchParameters parameters) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(parameters);
        return bookRepository.findAll(bookSpecification)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId) {
        return bookRepository.findAllByCategoryId(categoryId).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
