package com.example.onlinebookstore.repository;

import com.example.onlinebookstore.model.Category;
import java.util.HashSet;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testFindByIdIn() {
        Category category = new Category();
        category.setName("Name");
        category.setDeleted(true);
        category.setDescription("The characteristics of someone or something");

        Category category2 = new Category();
        category2.setName("42");
        category2.setDeleted(false);
        category2.setDescription("Description");

        categoryRepository.save(category);
        categoryRepository.save(category2);

        Assertions.assertTrue(categoryRepository.findByIdIn(new HashSet<>()).isEmpty());
    }
}