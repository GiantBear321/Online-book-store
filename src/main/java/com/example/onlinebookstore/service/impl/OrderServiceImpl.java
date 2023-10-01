package com.example.onlinebookstore.service.impl;

import com.example.onlinebookstore.dto.order.OrderAddressUpdateDto;
import com.example.onlinebookstore.dto.order.OrderResponseDto;
import com.example.onlinebookstore.dto.order.OrderStatusUpdateRequestDto;
import com.example.onlinebookstore.dto.orderitem.OrderItemResponseDto;
import com.example.onlinebookstore.exceptions.EntityNotFoundException;
import com.example.onlinebookstore.mapper.OrderItemMapper;
import com.example.onlinebookstore.mapper.OrderMapper;
import com.example.onlinebookstore.model.Order;
import com.example.onlinebookstore.model.OrderItem;
import com.example.onlinebookstore.model.ShoppingCart;
import com.example.onlinebookstore.model.User;
import com.example.onlinebookstore.repository.CartItemRepository;
import com.example.onlinebookstore.repository.OrderItemRepository;
import com.example.onlinebookstore.repository.OrderRepository;
import com.example.onlinebookstore.repository.ShoppingCartRepository;
import com.example.onlinebookstore.service.OrderService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public OrderResponseDto createOrder(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find shopping cart by id: " + user.getId()));

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(user.getShippingAddress());
        order.setUser(user);
        order.setStatus(Order.Status.PENDING);
        Set<OrderItem> orderItems = getOrderItemsFromShoppingCart(shoppingCart, order);
        BigDecimal total = orderItems.stream()
                .map(orderItem -> orderItem.getBook().getPrice()
                        .multiply(new BigDecimal(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);

        shoppingCart.getCartItems().forEach(cartItem -> {
            cartItemRepository.deleteById(cartItem.getId());
        });
        orderRepository.save(order);
        order.setOrderItems(orderItems);
        orderItemRepository.saveAll(orderItems);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderResponseDto> findAllOrders(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable)
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public void updateOrderStatus(Long orderId,
                                  OrderStatusUpdateRequestDto orderStatusUpdateRequestDto) {
        orderRepository.updateStatusByOrderId(orderId,
                orderStatusUpdateRequestDto.getStatus());
    }

    @Override
    public void updateShippingAddress(Long userId,
                                                  Long orderId,
                                                  OrderAddressUpdateDto orderAddressUpdateDto) {
        orderRepository
                .updateShippingAddress(orderId, orderAddressUpdateDto.getShippingAddress(), userId);
    }

    @Override
    public Set<OrderItemResponseDto> findAllOrderItems(Long orderId) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find order by id: " + orderId)
                );
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public OrderItemResponseDto findOrderItemById(Long orderId, Long itemId) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find order by id: " + orderId));
        return order.getOrderItems().stream()
                .filter(o -> o.getId().equals(itemId))
                .findFirst()
                .map(orderItemMapper::toDto)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find item by id: " + itemId
                                + ", with order id:  " + orderId)
                );
    }

    private Set<OrderItem> getOrderItemsFromShoppingCart(ShoppingCart shoppingCart, Order order) {
        return shoppingCart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setBook(cartItem.getBook());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setOrder(order);
                    orderItem.setPrice(cartItem.getBook().getPrice());
                    return orderItem;
                })
                .collect(Collectors.toSet());
    }
}
