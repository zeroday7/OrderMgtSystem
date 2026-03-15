package com.ordermgt.controller;

import com.ordermgt.dto.ProductResponse;
import com.ordermgt.dto.UpdateStockRequest;
import com.ordermgt.service.StockService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PatchMapping("/increase")
    public ProductResponse increaseStock(@Valid @RequestBody UpdateStockRequest request) {
        return stockService.increaseStock(request);
    }
}
