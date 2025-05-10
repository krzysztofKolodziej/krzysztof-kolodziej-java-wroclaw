package com.example.krzysztof_kolodziej_java_wroclaw.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderDto(
        String id,
        BigDecimal value,
        List<String> promotions
) {
}
