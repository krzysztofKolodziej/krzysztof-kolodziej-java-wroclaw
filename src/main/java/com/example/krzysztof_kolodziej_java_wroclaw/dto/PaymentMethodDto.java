package com.example.krzysztof_kolodziej_java_wroclaw.dto;

import java.math.BigDecimal;

public record PaymentMethodDto(
        String id,
        int discount,
        BigDecimal limit
) {
}
