package com.ordermgt.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateStockRequest(
        @NotNull Long productId,
        @NotNull @Min(1) Integer quantity
) {
}
