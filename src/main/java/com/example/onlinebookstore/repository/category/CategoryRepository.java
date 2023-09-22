package com.example.onlinebookstore.repository.category;

import com.example.onlinebookstore.model.Category;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Set<Category> findByIdIn(Set<Long> categoryIds);
}
