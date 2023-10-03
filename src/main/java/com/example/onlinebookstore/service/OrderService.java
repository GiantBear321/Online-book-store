package com.example.onlinebookstore.service;

import com.example.onlinebookstore.dto.order.OrderAddressUpdateDto;
import com.example.onlinebookstore.dto.order.OrderResponseDto;
import com.example.onlinebookstore.dto.order.OrderStatusUpdateRequestDto;
import com.example.onlinebookstore.dto.orderitem.OrderItemResponseDto;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface OrderService {

    OrderResponseDto createOrder(Authentication authentication);

    List<OrderResponseDto> findAllOrders(Long userId, Pageable pageable);

    void updateOrderStatus(Long orderId, OrderStatusUpdateRequestDto orderStatusUpdateRequestDto);

    void updateShippingAddress(Long userId,
                               Long orderId,
                               OrderAddressUpdateDto orderAddressUpdateDto);

    Set<OrderItemResponseDto> findAllOrderItems(Long orderId);

    OrderItemResponseDto findOrderItemById(Long orderId, Long itemId);
}
