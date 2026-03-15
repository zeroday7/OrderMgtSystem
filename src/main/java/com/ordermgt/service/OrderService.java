package com.ordermgt.service;

import com.ordermgt.dto.CreateOrderRequest;
import com.ordermgt.dto.OrderResponse;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface OrderService {

    @Transactional
    OrderResponse placeOrder(CreateOrderRequest request);

    @Transactional(readOnly = true)
    List<OrderResponse> getOrders();
}
