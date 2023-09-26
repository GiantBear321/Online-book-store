package com.example.onlinebookstore.repository.book.spec;

import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.repository.specification.SpecificationProvider;
import com.example.onlinebookstore.repository.specification.SpecificationProviderManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecProvider;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecProvider.stream().filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cant find specification provider or key"
                        + key));
    }
}
