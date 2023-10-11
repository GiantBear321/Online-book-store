package com.example.onlinebookstore.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.onlinebookstore.dto.book.BookDto;
import com.example.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.onlinebookstore.dto.book.CreateBookRequestDto;
import com.example.onlinebookstore.exceptions.EntityNotFoundException;
import com.example.onlinebookstore.mapper.BookMapper;
import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.model.Category;
import com.example.onlinebookstore.repository.BookRepository;
import com.example.onlinebookstore.repository.CategoryRepository;
import com.example.onlinebookstore.service.impl.BookServiceImpl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    public static final int FIRST_ELEMENT_INDEX = 0;
    public static final int SECOND_ELEMENT_INDEX = 1;
    public static final Long BOOK_ID = 1L;
    private Book book1;
    private Book book2;
    private CreateBookRequestDto bookRequestDto;
    private BookDto bookDto;
    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    public void setUp() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Fantasy");

        book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setIsbn("9781234567897");
        book1.setPrice(new BigDecimal("23.33"));
        book1.setCategories(Set.of(category));

        bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setTitle("Book 1");
        bookRequestDto.setAuthor("Author 1");
        bookRequestDto.setIsbn("9781234567897");
        bookRequestDto.setPrice(new BigDecimal("23.33"));
        bookRequestDto.setCategoryIds(Set.of(1L));

        bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Book 1");
        bookDto.setAuthor("Author 1");
        bookDto.setIsbn("9781234567897");
        bookDto.setPrice(new BigDecimal("23.33"));
        bookDto.setCategoryIds(Set.of(1L));

        book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");
        book2.setAuthor("Author 1");
        book2.setIsbn("9781234562222");
        book2.setPrice(new BigDecimal("23.33"));
        book2.setCategories(Set.of(category));
    }

    @AfterEach
    public void cleanUp() {
        book1 = null;
        book2 = null;
    }

    @Test
    @DisplayName("Verify correct user was returned after saving")
    public void saveBook_WithValidFields_ShouldReturnValidBook() {
        BookDto expected = bookDto;

        when(bookMapper.toModel(bookRequestDto)).thenReturn(book1);
        when(bookRepository.save(book1)).thenReturn(book1);
        when(bookMapper.toDto(book1)).thenReturn(expected);

        BookDto actual = bookService.save(bookRequestDto);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify correct book list is returned")
    public void findAll_WithValidPageable_ShouldReturnListOfBooks() {
        List<Book> bookList = new ArrayList<>();
        bookList.add(book1);
        bookList.add(book2);
        when(bookRepository.findAllWithCategories(any(Pageable.class))).thenReturn(bookList);

        BookDto bookDto1 = bookDto;

        List<BookDto> expected = new ArrayList<>();
        expected.add(bookDto1);

        BookDto bookDto2 = new BookDto();
        bookDto2.setId(2L);
        bookDto2.setTitle("Book 2");
        bookDto2.setAuthor("Author 1");
        bookDto2.setIsbn("9781234562222");
        bookDto2.setPrice(new BigDecimal("23.33"));
        bookDto2.setCategoryIds(Set.of(1L));
        expected.add(bookDto2);
        when(bookMapper.toDto(book1)).thenReturn(expected.get(FIRST_ELEMENT_INDEX));
        when(bookMapper.toDto(book2)).thenReturn(expected.get(SECOND_ELEMENT_INDEX));

        List<BookDto> actual = bookService.findAll(mock(Pageable.class));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify correct book is returned by ID")
    public void findBookById_WithValidId_ShouldReturnBook() {
        when(bookRepository.findBookById(BOOK_ID)).thenReturn(Optional.of(book1));

        BookDto expected = bookDto;

        when(bookMapper.toDto(book1)).thenReturn(expected);
        BookDto actual = bookService.findById(BOOK_ID);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify exception when book is not found by ID")
    public void findBookById_WithInvalidId_ShouldThrowEntityNotFoundException() {
        when(bookRepository.findBookById(BOOK_ID)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(BOOK_ID));
    }

    @Test
    @DisplayName("Verify correct book list is returned by category ID")
    public void findAllByCategoryId_WithValidCategoryId_ShouldReturnListOfBooks() {
        List<Book> bookList = new ArrayList<>();
        bookList.add(book1);

        when(bookRepository.findAllByCategoryId(anyLong())).thenReturn(bookList);

        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds();
        bookDtoWithoutCategoryIds.setId(1L);
        bookDtoWithoutCategoryIds.setTitle("Book 1");
        bookDtoWithoutCategoryIds.setAuthor("Author 1");
        bookDtoWithoutCategoryIds.setIsbn("9781234567897");
        bookDtoWithoutCategoryIds.setPrice(new BigDecimal("23.33"));

        List<BookDtoWithoutCategoryIds> expected = new ArrayList<>();
        expected.add(bookDtoWithoutCategoryIds);
        when(bookMapper.toDtoWithoutCategories(any(Book.class))).thenReturn(expected.get(
                FIRST_ELEMENT_INDEX));

        List<BookDtoWithoutCategoryIds> actual = bookService.findAllByCategoryId(anyLong());
        Assertions.assertEquals(expected, actual);
    }
}