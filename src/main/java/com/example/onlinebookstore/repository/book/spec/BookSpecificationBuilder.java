package com.example.onlinebookstore.repository.book.spec;

import com.example.onlinebookstore.dto.book.BookSearchParameters;
import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.repository.specification.SpecificationBuilder;
import com.example.onlinebookstore.repository.specification.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> spec = Specification.where(null);
        if (searchParameters.title() != null && searchParameters.title().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider("title")
                    .getSpecification(searchParameters.title()));
        }

        if (searchParameters.author() != null && searchParameters.author().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider("author")
                    .getSpecification(searchParameters.author()));
        }
        if (searchParameters.categories() != null && searchParameters.categories()
                .length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider("category")
                    .getSpecification(searchParameters.categories()));
        }
        return spec;
    }
}
