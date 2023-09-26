package com.example.onlinebookstore.dto.shoppingcart;

import com.example.onlinebookstore.dto.cartitem.CartItemResponseDto;
import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
