package com.example.krzysztof_kolodziej_java_wroclaw.service.rules;

import com.example.krzysztof_kolodziej_java_wroclaw.dto.OrderDto;
import com.example.krzysztof_kolodziej_java_wroclaw.dto.PaymentMethodDto;
import com.example.krzysztof_kolodziej_java_wroclaw.service.PaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Order(4)
@RequiredArgsConstructor
public class FallbackRule implements PaymentRule {

    private final PaymentProcessor processor;

    @Override
    public void apply(List<OrderDto> orders,
                      Map<String, PaymentMethodDto> methodMap,
                      Map<String, BigDecimal> spent,
                      Set<String> paidOrders) {
        for (var order : orders) {
            if (paidOrders.contains(order.id())) continue;
            processor.payAny(order.value(), methodMap, spent, Set.of());
            paidOrders.add(order.id());
        }
    }
}
