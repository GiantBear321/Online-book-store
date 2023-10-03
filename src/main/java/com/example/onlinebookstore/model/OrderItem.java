package com.example.onlinebookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinColumn(nullable = false, name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(nullable = false, name = "book_id")
    private Book book;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal price;
}
