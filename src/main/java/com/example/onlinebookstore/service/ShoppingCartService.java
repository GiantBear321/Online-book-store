package com.example.onlinebookstore.service;

import com.example.onlinebookstore.dto.cartitem.CartItemRequestDto;
import com.example.onlinebookstore.dto.cartitem.CartItemUpdateDto;
import com.example.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCartDtoByUserId(Long id);

    ShoppingCartResponseDto addCartItemByUserId(Long id, CartItemRequestDto cartItemRequestDto);

    ShoppingCartResponseDto updateCartItem(Long id, Long cartItemId,
                                               CartItemUpdateDto cartItemUpdateDto);

    ShoppingCartResponseDto deleteCartItem(Long id, Long cartItemId);
}
