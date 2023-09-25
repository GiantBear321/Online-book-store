package com.example.onlinebookstore.service.impl;

import com.example.onlinebookstore.dto.cartitem.CartItemRequestDto;
import com.example.onlinebookstore.dto.cartitem.CartItemUpdateDto;
import com.example.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.onlinebookstore.exceptions.EntityNotFoundException;
import com.example.onlinebookstore.mapper.CartItemMapper;
import com.example.onlinebookstore.mapper.ShoppingCartMapper;
import com.example.onlinebookstore.model.CartItem;
import com.example.onlinebookstore.model.ShoppingCart;
import com.example.onlinebookstore.model.User;
import com.example.onlinebookstore.repository.CartItemRepository;
import com.example.onlinebookstore.repository.ShoppingCartRepository;
import com.example.onlinebookstore.repository.UserRepository;
import com.example.onlinebookstore.service.ShoppingCartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    @Transactional
    public ShoppingCartResponseDto getShoppingCartDtoByUserId(Long id) {
        ShoppingCart shoppingCartById = getShoppingCartById(id);
        return shoppingCartMapper.toDto(shoppingCartById);
    }

    @Override
    public ShoppingCartResponseDto addCartItemByUserId(Long id,
                                                       CartItemRequestDto cartItemRequestDto) {
        CartItem cartItem = cartItemMapper.toEntity(cartItemRequestDto);
        ShoppingCart shoppingCart = getShoppingCartById(id);
        cartItem.setShoppingCart(shoppingCart);
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(getShoppingCartById(id));
    }

    @Override
    public ShoppingCartResponseDto updateCartItem(Long id, Long cartItemId,
                                                  CartItemUpdateDto cartItemUpdateDto) {
        CartItem cartItemDb = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("Can't find cart item by item id: " + cartItemId)
        );
        CartItem cartItemDto = cartItemMapper.toEntity(cartItemUpdateDto);
        cartItemDb.setQuantity(cartItemDto.getQuantity());
        cartItemRepository.save(cartItemDb);
        return shoppingCartMapper.toDto(getShoppingCartById(id));
    }

    @Override
    public ShoppingCartResponseDto deleteCartItem(Long id, Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
        return shoppingCartMapper.toDto(getShoppingCartById(id));
    }

    public ShoppingCart getShoppingCartById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find user by user id: " + id)
        );
        return shoppingCartRepository.findByUserId(user.getId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find shopping cart by id " + user.getId())
        );
    }
}
