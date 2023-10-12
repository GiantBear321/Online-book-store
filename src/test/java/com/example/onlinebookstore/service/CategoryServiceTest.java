package com.example.onlinebookstore.service;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.onlinebookstore.dto.category.CategoryDto;
import com.example.onlinebookstore.dto.category.CreateCategoryRequestDto;
import com.example.onlinebookstore.exceptions.EntityNotFoundException;
import com.example.onlinebookstore.mapper.CategoryMapper;
import com.example.onlinebookstore.model.Category;
import com.example.onlinebookstore.repository.CategoryRepository;
import com.example.onlinebookstore.service.impl.CategoryServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    private static final Long CATEGORY_ID = 1L;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category1;
    private Category category2;
    private CategoryDto categoryDto1;
    private CategoryDto categoryDto2;
    private CreateCategoryRequestDto categoryRequestDto;

    @BeforeEach
    void setUp() {
        category1 = new Category();
        category1.setId(1L);
        category1.setName("First category");
        category1.setDescription("Description of first category");

        category2 = new Category();
        category2.setId(2L);
        category2.setName("Second category");
        category2.setDescription("Description of second category");

        categoryDto1 = new CategoryDto();
        categoryDto1.setId(1L);
        categoryDto1.setName("First category");
        categoryDto1.setDescription("Description of first category");

        categoryRequestDto = new CreateCategoryRequestDto();
        categoryRequestDto.setName("First category");
        categoryRequestDto.setDescription("Description of first category");

        categoryDto2 = new CategoryDto();
        categoryDto2.setId(2L);
        categoryDto2.setName("Second category");
        categoryDto2.setDescription("Description of second category");
    }

    @Test
    @DisplayName("Verify correct category list is returned")
    public void findAll_WithValidPageable_ShouldReturnListOfCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);
        Page<Category> categoriesPage = new PageImpl<>(categories);
        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(categoriesPage);

        List<CategoryDto> expected = new ArrayList<>();
        expected.add(categoryDto1);
        expected.add(categoryDto2);

        when(categoryMapper.toDto(category1))
                .thenReturn(categoryDto1);
        when(categoryMapper.toDto(category2))
                .thenReturn(categoryDto2);

        List<CategoryDto> actual = categoryService.findAll(mock(Pageable.class));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify exception when category is not found by ID")
    public void getById_WithInvalidId_ShouldThrowEntityNotFoundException() {
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> categoryService.findById(CATEGORY_ID));
    }

    @Test
    @DisplayName("Verify category is saved correctly")
    public void saveCategory_WithValidCategoryDto_ShouldReturnSavedCategory() {
        when(categoryMapper.toEntity(categoryRequestDto)).thenReturn(category1);
        when(categoryRepository.save(category1)).thenReturn(category1);
        when(categoryMapper.toDto(category1)).thenReturn(categoryDto1);
        CategoryDto actual = categoryService.save(categoryRequestDto);
        Assertions.assertEquals(categoryDto1, actual);
    }

    @Test
    @DisplayName("Verify category is deleted by ID")
    public void deleteCategoryById_WithValidId_ShouldDeleteCategory() {
        categoryService.deleteById(CATEGORY_ID);
        verify(categoryRepository, times(1)).deleteById(CATEGORY_ID);
    }
}