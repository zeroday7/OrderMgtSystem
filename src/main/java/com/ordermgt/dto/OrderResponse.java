package com.ordermgt.dto;

import com.ordermgt.domain.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
        Long orderId,
        String orderNumber,
        Long productId,
        Integer quantity,
        BigDecimal totalPrice,
        OrderStatus status,
        LocalDateTime orderedAt
) {
}
