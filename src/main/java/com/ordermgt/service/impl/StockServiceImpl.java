package com.ordermgt.service.impl;

import com.ordermgt.domain.Stock;
import com.ordermgt.dto.ProductResponse;
import com.ordermgt.dto.UpdateStockRequest;
import com.ordermgt.exception.BusinessException;
import com.ordermgt.repository.StockRepository;
import com.ordermgt.service.StockService;
import org.springframework.stereotype.Service;

@Service
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public ProductResponse increaseStock(UpdateStockRequest request) {
        Stock stock = stockRepository.findByProductId(request.productId())
                .orElseThrow(() -> new BusinessException("재고 정보를 찾을 수 없습니다."));

        stock.increase(request.quantity());

        return new ProductResponse(
                stock.getProduct().getId(),
                stock.getProduct().getName(),
                stock.getProduct().getPrice(),
                stock.getQuantity()
        );
    }
}
