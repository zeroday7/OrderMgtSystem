package com.ordermgt.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ordermgt.domain.Order;
import com.ordermgt.domain.Product;
import com.ordermgt.domain.Stock;
import com.ordermgt.dto.CreateOrderRequest;
import com.ordermgt.dto.OrderResponse;
import com.ordermgt.exception.OutOfStockException;
import com.ordermgt.repository.OrderRepository;
import com.ordermgt.repository.ProductRepository;
import com.ordermgt.repository.StockRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void placeOrder_stockIsOneAndOrderQuantityIsOne_thenSuccess() {
        Long productId = 1L;
        CreateOrderRequest request = new CreateOrderRequest(productId, 1);

        Product product = new Product("Keyboard", BigDecimal.valueOf(10000));
        ReflectionTestUtils.setField(product, "id", productId);

        Stock stock = new Stock(product, 1);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(stockRepository.findByProductId(productId)).thenReturn(Optional.of(stock));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = orderService.placeOrder(request);

        assertThat(response.productId()).isEqualTo(productId);
        assertThat(response.quantity()).isEqualTo(1);
        assertThat(response.totalPrice()).isEqualByComparingTo("10000");
        assertThat(stock.getQuantity()).isZero();

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void placeOrder_stockIsZeroAndOrderQuantityIsOne_thenThrowOutOfStockException() {
        Long productId = 1L;
        CreateOrderRequest request = new CreateOrderRequest(productId, 1);

        Product product = new Product("Keyboard", BigDecimal.valueOf(10000));
        ReflectionTestUtils.setField(product, "id", productId);

        Stock stock = new Stock(product, 0);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(stockRepository.findByProductId(productId)).thenReturn(Optional.of(stock));

        assertThatThrownBy(() -> orderService.placeOrder(request))
                .isInstanceOf(OutOfStockException.class)
                .hasMessage("재고가 부족합니다.");

        verify(orderRepository, never()).save(any(Order.class));
        assertThat(stock.getQuantity()).isZero();
    }
}
