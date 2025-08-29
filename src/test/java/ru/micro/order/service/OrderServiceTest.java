package ru.micro.order.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.micro.order.domain.Order;
import ru.micro.order.repository.OrderRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_Success() {
        // given
        Order order = new Order();
        order.setCustomerId("cust-123");
        Order savedOrder = new Order();
        savedOrder.setId("order-123");
        savedOrder.setCustomerId("cust-123");

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(restTemplate.postForEntity(
                anyString(),
                any(HttpEntity.class),
                eq(Order.class)
        )).thenReturn(ResponseEntity.ok(savedOrder));

        // when
        Order result = orderService.createOrder(order);

        // then
        assertNotNull(result);
        assertEquals("order-123", result.getId());
        verify(orderRepository).save(any(Order.class));
        verify(restTemplate).postForEntity(anyString(), any(HttpEntity.class), eq(Order.class));
    }

    @Test
    void findAll_ReturnsOrders() {
        // given
        List<Order> orders = List.of(new Order(), new Order());
        when(orderRepository.findAll()).thenReturn(orders);

        // when
        List<Order> result = orderService.findAll();

        // then
        assertEquals(2, result.size());
        verify(orderRepository).findAll();
    }
}