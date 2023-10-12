package com.example.onlinebookstore.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.onlinebookstore.dto.book.BookDto;
import com.example.onlinebookstore.dto.book.CreateBookRequestDto;
import com.example.onlinebookstore.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BookService bookService;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @DisplayName("Save a new book")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void save_ValidSaveBookRequestDto_Success() throws Exception {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setTitle("Book 1");
        bookRequestDto.setAuthor("Author 1");
        bookRequestDto.setIsbn("9786006515657");
        bookRequestDto.setPrice(new BigDecimal("23.33"));
        bookRequestDto.setCategoryIds(Set.of(1L));

        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setTitle("Book 1");
        expected.setAuthor("Author 1");
        expected.setIsbn("9786006515657");
        expected.setPrice(new BigDecimal("23.33"));
        expected.setCategoryIds(Set.of(1L));

        String jsonRequest = objectMapper.writeValueAsString(bookRequestDto);

        MvcResult mvcResult = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        BookDto actual =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("Get all books")
    @WithMockUser(username = "test", password = "test", roles = {"ADMIN", "USER"})
    void getAllBooks_ReturnAllAvailableBook_Sucess() throws Exception {
        BookDto savedBook1 = bookService.findById(1L);
        BookDto savedBook2 = bookService.findById(2L);
        BookDto savedBook3 = bookService.findById(3L);

        List<BookDto> expected = new ArrayList<>();
        expected.add(savedBook1);
        expected.add(savedBook2);
        expected.add(savedBook3);

        Pageable pageable = PageRequest.of(0, 3);
        MvcResult mvcResult =
                mockMvc.perform(get("/books").contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(
                                pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                ).andReturn();
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<BookDto> actual =
                objectMapper.readValue(jsonResponse, new TypeReference<List<BookDto>>() {
                });
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update an existing book")
    @WithMockUser(username = "test", password = "test", roles = {"ADMIN", "USER"})
    void updateById_ValidUpdateBookRequestDto_Success() throws Exception {
        BookDto expected = bookService.findById(1L);
        expected.setTitle("Updated Title");
        expected.setAuthor("Updated Author");
        expected.setPrice(new BigDecimal("29.99"));

        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setTitle("Sample Book 1");
        bookRequestDto.setAuthor("Author A");
        bookRequestDto.setIsbn("9781234567897");
        bookRequestDto.setPrice(new BigDecimal("19.99"));
        bookRequestDto.setCategoryIds(Set.of(3L));

        String jsonRequest = objectMapper.writeValueAsString(bookRequestDto);

        mockMvc.perform(put("/books/{id}", expected.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }
}