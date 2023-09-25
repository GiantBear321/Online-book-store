package com.example.onlinebookstore.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemUpdateDto {
    @NotNull
    @Positive
    private int quantity;
}
