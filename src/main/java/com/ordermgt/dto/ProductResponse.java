package com.ordermgt.dto;

import java.math.BigDecimal;

public record ProductResponse(
        Long productId,
        String name,
        BigDecimal price,
        Integer stockQuantity
) {
}
