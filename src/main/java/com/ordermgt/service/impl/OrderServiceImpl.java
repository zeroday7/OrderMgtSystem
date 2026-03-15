package com.ordermgt.service.impl;

import com.ordermgt.domain.Order;
import com.ordermgt.domain.Product;
import com.ordermgt.domain.Stock;
import com.ordermgt.dto.CreateOrderRequest;
import com.ordermgt.dto.OrderResponse;
import com.ordermgt.exception.BusinessException;
import com.ordermgt.exception.OutOfStockException;
import com.ordermgt.repository.OrderRepository;
import com.ordermgt.repository.ProductRepository;
import com.ordermgt.repository.StockRepository;
import com.ordermgt.service.OrderService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            StockRepository stockRepository
    ) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
    }

    @Override
    public OrderResponse placeOrder(CreateOrderRequest request) {
        Long productId = Objects.requireNonNull(request.productId(), "상품 ID는 필수입니다.");
        Integer quantity = Objects.requireNonNull(request.quantity(), "주문 수량은 필수입니다.");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException("상품을 찾을 수 없습니다."));

        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new BusinessException("재고 정보를 찾을 수 없습니다."));

        if (stock.getQuantity() < quantity) {
            throw new OutOfStockException("재고가 부족합니다.");
        }

        stock.decrease(quantity);

        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        Order order = orderRepository.save(new Order(product, quantity, totalPrice));

        return toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrders() {
        return orderRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getProduct().getId(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getOrderedAt()
        );
    }
}
