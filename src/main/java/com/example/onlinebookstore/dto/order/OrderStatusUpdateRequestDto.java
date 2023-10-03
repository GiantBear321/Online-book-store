package com.example.onlinebookstore.dto.order;

import com.example.onlinebookstore.model.Order;
import lombok.Data;

@Data
public class OrderStatusUpdateRequestDto {
    private Order.Status status;
}
