package ru.micro.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.micro.order.domain.Order;
import ru.micro.order.repository.OrderRepository;

import java.util.List;

/**
 * @author a.zharov
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final RestTemplate restTemplate;
    private final OrderRepository orderRepository;

    @Value("${spring.application.microservice-customer.url}")
    private String customerBaseUrl;

    private static final String CUSTOMER_ORDER_URL = "customerOrders/";

    @Transactional
    public Order createOrder(Order order) {
        Order result = orderRepository.save(order);

        String url = customerBaseUrl + CUSTOMER_ORDER_URL + result.getCustomerId();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        log.info("Order Request URL: {}", url);
        try {
            HttpEntity<Order> request = new HttpEntity<>(result, headers);
            ResponseEntity<Order> responseEntity = restTemplate.postForEntity(url, request, Order.class);
            if (responseEntity.getStatusCode().isError()) {
                log.error("For Order ID: {}, error response: {} is received to create Order in Customer Microservice",
                        result.getId(),
                        responseEntity.getStatusCode());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, String.format(
                        "For Order UUID: %s, Customer Microservice Message: %s",
                        result.getId(),
                        responseEntity.getStatusCode()
                ));
            }
            if (responseEntity.hasBody()) {
                log.error("Order From Response: {}", responseEntity.getBody().getId());
            }
        } catch (Exception e) {
            log.error("For Order ID: {}, cannot create Order in Customer Microservice for reason: {}",
                    result.getId(),
                    ExceptionUtils.getRootCauseMessage(e)
            );
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(
                    "For Order UUID: %s, Customer Microservice Response: %s",
                    result.getId(),
                    ExceptionUtils.getRootCauseMessage(e)
            ));
        }
        return result;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }
}
