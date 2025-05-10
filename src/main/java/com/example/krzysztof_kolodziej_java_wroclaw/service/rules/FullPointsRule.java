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
@Order(2)
@RequiredArgsConstructor
public class FullPointsRule implements PaymentRule {

    private final PaymentProcessor processor;

    @Override
    public void apply(List<OrderDto> orders,
                      Map<String, PaymentMethodDto> methodMap,
                      Map<String, BigDecimal> spent,
                      Set<String> paidOrders) {
        for (var order : orders) {
            if (paidOrders.contains(order.id())) continue;
            var pts = methodMap.get("PUNKTY");
            if (pts == null) continue;
            BigDecimal cost = processor.applyDiscount(order.value(), pts.discount());
            if (pts.limit().compareTo(cost) >= 0) {
                processor.pay("PUNKTY", cost, methodMap, spent);
                paidOrders.add(order.id());
            }
        }
    }
}