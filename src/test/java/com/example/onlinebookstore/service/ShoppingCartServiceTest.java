package com.example.onlinebookstore.service;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.onlinebookstore.dto.cartitem.CartItemRequestDto;
import com.example.onlinebookstore.dto.cartitem.CartItemUpdateDto;
import com.example.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.onlinebookstore.exceptions.EntityNotFoundException;
import com.example.onlinebookstore.mapper.CartItemMapper;
import com.example.onlinebookstore.mapper.ShoppingCartMapper;
import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.model.CartItem;
import com.example.onlinebookstore.model.ShoppingCart;
import com.example.onlinebookstore.model.User;
import com.example.onlinebookstore.repository.CartItemRepository;
import com.example.onlinebookstore.repository.ShoppingCartRepository;
import com.example.onlinebookstore.repository.UserRepository;
import com.example.onlinebookstore.service.impl.ShoppingCartServiceImpl;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartItemMapper cartItemMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartServiceImpl;

    @Test
    public void getShoppingCartDtoByUserId_ValidData_Success() {
        User user = getMockUser();
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        ShoppingCartResponseDto shoppingCartResponseDto = new ShoppingCartResponseDto();
        shoppingCartResponseDto.setCartItems(new HashSet<>());
        shoppingCartResponseDto.setId(1L);
        shoppingCartResponseDto.setUserId(1L);
        when(shoppingCartMapper.toDto(Mockito.any()))
                .thenReturn(shoppingCartResponseDto);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCartItems(new HashSet<>());
        shoppingCart.setId(1L);
        shoppingCart.setUser(user);
        Optional<ShoppingCart> ofResult2 = Optional.of(shoppingCart);
        when(shoppingCartRepository.findByUserId(Mockito.<Long>any())).thenReturn(ofResult2);
        assertSame(shoppingCartResponseDto, shoppingCartServiceImpl.getShoppingCartDtoByUserId(1L));
        verify(userRepository).findById(Mockito.<Long>any());
        verify(shoppingCartMapper).toDto(Mockito.any());
        verify(shoppingCartRepository).findByUserId(Mockito.<Long>any());
    }

    @Test
    @DisplayName("Add new cart item")
    public void addCartItemByUserId_ValidCartItem_ShouldAddCartItem() {
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        Book book = new Book();
        book.setAuthor("test");
        book.setCategories(new HashSet<>());
        book.setCoverImage("Cover Image");
        book.setDeleted(true);
        book.setDescription("description");
        book.setId(1L);
        book.setIsbn("isbn");
        book.setPrice(BigDecimal.valueOf(10L));
        book.setTitle("title");

        User user = getMockUser();

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCartItems(new HashSet<>());
        shoppingCart.setId(1L);
        shoppingCart.setUser(user);

        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setDeleted(true);
        cartItem.setId(1L);
        cartItem.setQuantity(1);
        cartItem.setShoppingCart(shoppingCart);
        when(cartItemMapper.toEntity(Mockito.<CartItemRequestDto>any())).thenReturn(cartItem);

        CartItemRequestDto cartItemRequestDto = new CartItemRequestDto();
        cartItemRequestDto.setBookId(1L);
        cartItemRequestDto.setQuantity(1);
        assertThrows(RuntimeException.class,
                () -> shoppingCartServiceImpl.addCartItemByUserId(1L, cartItemRequestDto)
        );
        verify(userRepository).findById(Mockito.<Long>any());
        verify(cartItemMapper).toEntity(Mockito.<CartItemRequestDto>any());
    }

    @Test
    public void deleteCartItem_ValidId_Success() {
        User user = getMockUser();

        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        Optional<ShoppingCart> emptyResult = Optional.empty();
        when(shoppingCartRepository.findByUserId(Mockito.<Long>any())).thenReturn(emptyResult);
        doNothing().when(cartItemRepository).deleteById(Mockito.<Long>any());
        assertThrows(EntityNotFoundException.class,
                () -> shoppingCartServiceImpl.deleteCartItem(1L, 1L)
        );
        verify(userRepository).findById(Mockito.<Long>any());
        verify(shoppingCartRepository).findByUserId(Mockito.<Long>any());
        verify(cartItemRepository).deleteById(Mockito.<Long>any());
    }

    private User getMockUser() {
        User user = new User();
        user.setDeleted(true);
        user.setEmail("test@mail");
        user.setFirstName("test");
        user.setId(1L);
        user.setLastName("tester");
        user.setPassword("123456");
        user.setRoles(new HashSet<>());
        user.setShippingAddress("38 Street");
        return user;
    }
}