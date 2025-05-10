package com.example.krzysztof_kolodziej_java_wroclaw.service.rules;

import com.example.krzysztof_kolodziej_java_wroclaw.dto.OrderDto;
import com.example.krzysztof_kolodziej_java_wroclaw.dto.PaymentMethodDto;
import com.example.krzysztof_kolodziej_java_wroclaw.service.PaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Order(3)
@RequiredArgsConstructor
public class PartialPointsRule implements PaymentRule {

    private final PaymentProcessor processor;

    @Override
    public void apply(List<OrderDto> orders,
                      Map<String, PaymentMethodDto> methodMap,
                      Map<String, BigDecimal> spent,
                      Set<String> paidOrders) {

        for (OrderDto order : orders) {
            if (paidOrders.contains(order.id())) continue;

            PaymentMethodDto pts = methodMap.get("PUNKTY");
            if (pts == null) continue;

            BigDecimal total = order.value();

            BigDecimal minPoints = total
                    .multiply(BigDecimal.valueOf(0.1))
                    .setScale(2, RoundingMode.DOWN);

            BigDecimal discountedTotal = processor.applyDiscount(total, 10);

            if (pts.limit().compareTo(minPoints) < 0) {
                continue;
            }
            BigDecimal ptsToUse = pts.limit().min(discountedTotal);
            BigDecimal remainder = discountedTotal.subtract(ptsToUse);

            BigDecimal cardFunds = methodMap.values().stream()
                    .filter(m -> !m.id().equals("PUNKTY"))
                    .map(PaymentMethodDto::limit)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (cardFunds.compareTo(remainder) < 0) {
                continue;
            }

            processor.pay("PUNKTY", ptsToUse, methodMap, spent);
            processor.payAny(remainder, methodMap, spent, Set.of("PUNKTY"));
            paidOrders.add(order.id());
        }
    }
}

