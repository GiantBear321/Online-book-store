package com.example.onlinebookstore.repository.book.spec;

import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.repository.SpecificationProvider;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.like(root.get("author"),
                    "%" + params[0] + "%");
            return criteriaBuilder.and(predicate);
        };
    }

    @Override
    public String getKey() {
        return "author";
    }
}
