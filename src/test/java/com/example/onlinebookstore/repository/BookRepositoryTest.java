package com.example.onlinebookstore.repository;

import com.example.onlinebookstore.model.Book;;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find all books by specified category ID")
    public void findAllByCategoryId_ValidID_ShouldReturnListOfBook() {
        Long categoryID = 3L;
        List<Book> booksList = bookRepository.findAllByCategoryId(categoryID);
        Long expectedListSize = 3L;
        Assertions.assertEquals(expectedListSize, booksList.size());
    }

    @Test
    @DisplayName("Find one book by specified book ID")
    public void findBookById_ValidId_ShouldReturnRightBook() {
        Long bookId = 5L;;
        Assertions.assertEquals("Witcher", bookRepository.findBookById(bookId).get().getTitle() );
    }
}