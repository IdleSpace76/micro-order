package ru.micro.order.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.micro.order.domain.Order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author a.zharov
 */
@DataMongoTest
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Test
    void saveOrder_ShouldPersist() {
        // given
        Order order = new Order();
        order.setCustomerId("cust-123");

        // when
        Order saved = orderRepository.save(order);

        // then
        assertNotNull(saved.getId());
        assertEquals("cust-123", saved.getCustomerId());
    }
}
