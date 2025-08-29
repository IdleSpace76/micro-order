package ru.micro.order.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.micro.order.domain.Order;
import ru.micro.order.domain.PaymentType;
import ru.micro.order.domain.Product;
import ru.micro.order.service.OrderService;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Test
    void createOrder_ValidRequest_ReturnsCreated() throws Exception {
        // given
        Order order = createValidOrder();

        when(orderService.createOrder(any(Order.class))).thenReturn(order);

        // when & then
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "customerId": "cust-123",
                                    "paymentMethod": "CREDIT_CARD",
                                    "paymentDetails": "card-123456",
                                    "products": [
                                        {
                                            "id": "prod-1",
                                            "name": "Product 1",
                                            "manufacturerName": "Manufacturer 1",
                                            "quantity": 2,
                                            "price": 100.0
                                        }
                                    ]
                                }"""))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value("order-123"));
    }

    @Test
    void createOrder_InvalidRequest_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")) // Пустой объект - невалидный
                .andExpect(status().isBadRequest());
    }

    @Test
    void createOrder_WithExistingId_ReturnsConflict() throws Exception {
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": "existing-id",
                                    "customerId": "cust-123",
                                    "paymentMethod": "CREDIT_CARD",
                                    "paymentDetails": "card-123456",
                                    "products": [
                                        {
                                            "id": "prod-1",
                                            "name": "Product 1",
                                            "manufacturerName": "Manufacturer 1",
                                            "quantity": 2,
                                            "price": 100.0
                                        }
                                    ]
                                }"""))
                .andExpect(status().isConflict());
    }

    @Test
    void getAllOrders_ReturnsOrders() throws Exception {
        List<Order> orders = List.of(new Order(), new Order());
        when(orderService.findAll()).thenReturn(orders);

        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    private Order createValidOrder() {
        Order order = new Order();
        order.setId("order-123");
        order.setCustomerId("cust-123");
        order.setPaymentMethod(PaymentType.CREDIT_CARD);
        order.setPaymentDetails("card-123456");

        order.setProducts(Set.of(createProduct()));

        return order;
    }

    private Product createProduct() {
        Product product = new Product();
        product.setId("prod-1");
        product.setManufacturerName("Manufacturer 1");
        product.setName("Product 1");
        product.setQuantity(2);
        product.setPrice(100.0);
        return product;
    }
}