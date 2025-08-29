package ru.micro.order.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.micro.order.domain.Health;
import ru.micro.order.domain.HealthStatus;

/**
 * @author a.zharov
 */
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class HealthController {

    @GetMapping(
            value = "/health",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Health> getHealth() {
        log.debug("REST request to get the Health Status");
        Health health = new Health();
        health.setStatus(HealthStatus.UP);
        return ResponseEntity.ok().body(health);
    }
}
