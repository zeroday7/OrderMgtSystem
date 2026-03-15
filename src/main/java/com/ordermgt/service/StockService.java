package com.ordermgt.service;

import com.ordermgt.dto.ProductResponse;
import com.ordermgt.dto.UpdateStockRequest;
import org.springframework.transaction.annotation.Transactional;

public interface StockService {

    @Transactional
    ProductResponse increaseStock(UpdateStockRequest request);
}
