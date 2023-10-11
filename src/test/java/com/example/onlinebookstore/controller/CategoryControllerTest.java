package com.example.onlinebookstore.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.onlinebookstore.dto.category.CategoryDto;
import com.example.onlinebookstore.dto.category.CreateCategoryRequestDto;
import com.example.onlinebookstore.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryService categoryService;

    private CreateCategoryRequestDto categoryRequestDto;
    private CategoryDto categoryDto;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @BeforeEach
    void setUp() {
        categoryRequestDto = new CreateCategoryRequestDto();
        categoryRequestDto.setName("test category 123");
        categoryRequestDto.setDescription("Description of test category");

        categoryDto = new CategoryDto();
        categoryDto.setName("test category 123");
        categoryDto.setDescription("Description of test category");
        categoryDto.setId(1L);
    }

    @Test
    @DisplayName("Create a new category")
    @WithMockUser(username = "test", password = "test", roles = {"ADMIN", "USER"})
    void createCategory_ValidCategoryDto_Success() throws Exception {
        CategoryDto expected = categoryDto;
        String jsonRequest = objectMapper.writeValueAsString(categoryRequestDto);

        MvcResult mvcResult = mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        CategoryDto actual = objectMapper.readValue(jsonResponse, CategoryDto.class);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @DisplayName("Get all categories")
    @WithMockUser(username = "test", password = "test", roles = {"ADMIN", "USER"})
    void getAllCategories_ReturnAllCategories() throws Exception {
        CategoryDto savedCategory1 = categoryService.findById(1L);
        CategoryDto savedCategory2 = categoryService.findById(2L);
        CategoryDto savedCategory3 = categoryService.findById(3L);
        List<CategoryDto> expected = new ArrayList<>();
        expected.add(savedCategory1);
        expected.add(savedCategory2);
        expected.add(savedCategory3);

        MvcResult mvcResult = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<CategoryDto> actual =
                objectMapper.readValue(jsonResponse, new TypeReference<List<CategoryDto>>() {
                });
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update a category")
    @WithMockUser(username = "test", password = "test", roles = {"ADMIN", "USER"})
    void updateCategory_ValidCategoryDto_Success() throws Exception {
        CategoryDto savedCategory = categoryService.findById(1L);
        savedCategory.setName("Updated Category");

        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("Updated Category");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(put("/categories/{id}", savedCategory.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(equalTo("Updated Category"))));
    }
}