package com.example.onlinebookstore.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.onlinebookstore.dto.book.BookDto;
import com.example.onlinebookstore.dto.book.CreateBookRequestDto;
import com.example.onlinebookstore.dto.cartitem.CartItemRequestDto;
import com.example.onlinebookstore.dto.cartitem.CartItemResponseDto;
import com.example.onlinebookstore.dto.cartitem.CartItemUpdateDto;
import com.example.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.onlinebookstore.model.Role;
import com.example.onlinebookstore.model.User;
import com.example.onlinebookstore.service.BookService;
import com.example.onlinebookstore.service.ShoppingCartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;

    @Resource
    private ObjectMapper objectMapper;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private BookService bookService;

    @BeforeAll
    public static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @DisplayName("Get shopping cart")
    void getShoppingCart_ValidRequest_Success() throws Exception {
        User mockUser = getMockUser();

        mockMvc.perform(get("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(mockUser)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Add a book to the shopping cart")
    public void addBookToCart_ValidCartItemRequest_Success() throws Exception {
        User user = getMockUser();

        BookDto savedBook = bookService.findById(1L);
        CartItemRequestDto cartItemRequestDto = new CartItemRequestDto();
        cartItemRequestDto.setBookId(savedBook.getId());
        cartItemRequestDto.setQuantity(10);
        String jsonRequest = objectMapper.writeValueAsString(cartItemRequestDto);
        mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(user))
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Update cart item")
    @Sql(scripts = {
            "classpath:database/insert-cart_item-to-cart_items-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateCartItem_ValidRequest_Success() throws Exception {
        User user = getMockUser();
        CartItemUpdateDto cartItemQuantityRequestDto = new CartItemUpdateDto();
        cartItemQuantityRequestDto.setQuantity(3);
        mockMvc.perform(MockMvcRequestBuilders.put("/cart/cart-items/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(user))
                        .content(objectMapper.writeValueAsString(
                                cartItemQuantityRequestDto)))
                .andExpect(status().isOk());
        ShoppingCartResponseDto allCartItems = shoppingCartService.getShoppingCartDtoByUserId(user.getId());
        CartItemResponseDto expected = allCartItems.getCartItems().stream()
                .filter(cartItemResponseDto ->
                        cartItemResponseDto.getQuantity()
                                == 3)
                .findFirst().get();

        assertEquals(3, expected.getQuantity());
    }

    @Test
    @DisplayName("Delete cart item")
    void deleteCartItem_ValidRequest_Success() throws Exception {
        User mockUser = getMockUser();
        long cartItemId = 1L;

        MvcResult result = mockMvc.perform(delete("/cart/cart-items/" + cartItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(mockUser)))
                .andExpect(status().isOk())
                .andReturn();
    }

    private User getMockUser() {
        User user = new User();
        user.setId(1L);
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setName(Role.RoleName.USER);
        roles.add(role);
        Role roleAdmin = new Role();
        roleAdmin.setName(Role.RoleName.ADMIN);
        roles.add(roleAdmin);
        user.setRoles(roles);
        return user;
    }
}