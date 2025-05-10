package com.example.krzysztof_kolodziej_java_wroclaw.dto;

import java.math.BigDecimal;

public record PaymentResultDto(
        String paymentMethodId,
        BigDecimal amount
) {
}
