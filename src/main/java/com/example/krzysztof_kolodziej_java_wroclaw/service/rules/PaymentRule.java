package com.example.krzysztof_kolodziej_java_wroclaw.service.rules;

import com.example.krzysztof_kolodziej_java_wroclaw.dto.OrderDto;
import com.example.krzysztof_kolodziej_java_wroclaw.dto.PaymentMethodDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PaymentRule {

    void apply(List<OrderDto> orders,
               Map<String, PaymentMethodDto> methodMap,
               Map<String, BigDecimal> spent,
               Set<String> paidOrders);
}
