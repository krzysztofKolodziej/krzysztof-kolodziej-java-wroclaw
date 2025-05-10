package com.example.krzysztof_kolodziej_java_wroclaw.service.rules;

import com.example.krzysztof_kolodziej_java_wroclaw.dto.OrderDto;
import com.example.krzysztof_kolodziej_java_wroclaw.dto.PaymentMethodDto;
import com.example.krzysztof_kolodziej_java_wroclaw.service.PaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
@Order(1)
@RequiredArgsConstructor
public class CardPromotionRule implements PaymentRule {

    private final PaymentProcessor processor;

    @Override
    public void apply(List<OrderDto> orders,
                      Map<String, PaymentMethodDto> methodMap,
                      Map<String, BigDecimal> spent,
                      Set<String> paidOrders) {

        record Option(String orderId, String methodId, BigDecimal discounted, BigDecimal saving) {
        }

        List<Option> options = orders.stream()
                .filter(o -> o.promotions() != null)
                .flatMap(o -> o.promotions().stream()
                        .map(methodId -> {
                            PaymentMethodDto m = methodMap.get(methodId);
                            BigDecimal discounted = processor.applyDiscount(o.value(), m.discount());
                            BigDecimal saving = o.value().subtract(discounted);
                            return new Option(o.id(), methodId, discounted, saving);
                        }))
                .sorted(Comparator.comparing(Option::saving).reversed())
                .toList();

        for (Option opt : options) {
            if (paidOrders.contains(opt.orderId)) continue;

            PaymentMethodDto current = methodMap.get(opt.methodId);
            if (current.limit().compareTo(opt.discounted) >= 0) {
                processor.pay(opt.methodId, opt.discounted, methodMap, spent);
                paidOrders.add(opt.orderId);
            }
        }
    }
}
