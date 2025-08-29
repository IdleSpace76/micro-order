package ru.micro.order.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.micro.order.domain.Order;
import ru.micro.order.service.OrderService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author a.zharov
 */
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class OrderController {

    private static final String ENTITY_NAME = "order";

    @Value("${spring.application.name}")
    private String applicationName;

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order) throws URISyntaxException {
        log.debug("REST request to save Order : {}", order);
        if (order.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A new order cannot already have an ID");
        }
        Order result = orderService.createOrder(order);

        HttpHeaders headers = new HttpHeaders();
        String message = String.format("A new %s is created with identifier %s", ENTITY_NAME, result.getId());
        headers.add("X-" + applicationName + "-alert", message);
        headers.add("X-" + applicationName + "-params", result.getId());
        return ResponseEntity.created(new URI("/api/orders/" + result.getId())).headers(headers).body(result);
    }

    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        log.info("REST request to get all Orders");
        return orderService.findAll();
    }
}
