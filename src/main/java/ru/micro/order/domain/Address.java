package ru.micro.order.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author a.zharov
 */
@Data
@NoArgsConstructor
public class Address implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    private String streetName;

    @NotNull
    private String streetNumber;

    private String additionalInfo;

    @NotNull
    private String zipCode;

    @NotNull
    private String city;

    private String state;

    @NotNull
    private String country;
}
