package com.example.onlinebookstore.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemRequestDto {
    @NotNull
    @Positive
    private Long bookId;
    @NotNull
    @Positive
    private int quantity;
}
