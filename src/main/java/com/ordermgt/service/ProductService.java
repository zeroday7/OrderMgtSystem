package com.ordermgt.service;

import com.ordermgt.dto.CreateProductRequest;
import com.ordermgt.dto.ProductResponse;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface ProductService {

    @Transactional
    ProductResponse createProduct(CreateProductRequest request);

    @Transactional(readOnly = true)
    List<ProductResponse> getProducts();
}
