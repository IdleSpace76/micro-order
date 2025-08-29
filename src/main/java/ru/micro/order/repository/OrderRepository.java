package ru.micro.order.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.micro.order.domain.Order;

/**
 * @author a.zharov
 */
@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

}
