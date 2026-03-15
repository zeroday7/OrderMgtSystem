package com.ordermgt.service.impl;

import com.ordermgt.domain.Product;
import com.ordermgt.domain.Stock;
import com.ordermgt.dto.CreateProductRequest;
import com.ordermgt.dto.ProductResponse;
import com.ordermgt.repository.ProductRepository;
import com.ordermgt.repository.StockRepository;
import com.ordermgt.service.ProductService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    public ProductServiceImpl(ProductRepository productRepository, StockRepository stockRepository) {
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
    }

    @Override
    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = productRepository.save(new Product(request.name(), request.price()));
        Stock stock = stockRepository.save(new Stock(product, request.initialStock()));

        return toResponse(product, stock.getQuantity());
    }

    @Override
    public List<ProductResponse> getProducts() {
        return productRepository.findAll().stream()
                .map(product -> toResponse(product, product.getStock() == null ? 0 : product.getStock().getQuantity()))
                .toList();
    }

    private ProductResponse toResponse(Product product, Integer stockQuantity) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                stockQuantity
        );
    }
}
