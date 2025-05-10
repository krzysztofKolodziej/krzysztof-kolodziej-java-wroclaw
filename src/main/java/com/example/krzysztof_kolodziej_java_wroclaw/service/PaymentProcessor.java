package com.example.krzysztof_kolodziej_java_wroclaw.service;

import com.example.krzysztof_kolodziej_java_wroclaw.dto.PaymentMethodDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class PaymentProcessor {

    public BigDecimal applyDiscount(BigDecimal value, int pct) {
        return value.multiply(BigDecimal.valueOf(100 - pct))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    public void pay(String methodId, BigDecimal amount,
                    Map<String, PaymentMethodDto> methodMap,
                    Map<String, BigDecimal> spent) {
        var current = methodMap.get(methodId);
        var newLimit = current.limit().subtract(amount);
        methodMap.put(methodId, new PaymentMethodDto(current.id(), current.discount(), newLimit));
        spent.merge(methodId, amount, BigDecimal::add);
    }

    public void payAny(BigDecimal amount,
                       Map<String, PaymentMethodDto> methodMap,
                       Map<String, BigDecimal> spent,
                       Set<String> excluded) {
        BigDecimal remaining = amount;

        List<PaymentMethodDto> methods = methodMap.values().stream()
                .filter(pm -> !excluded.contains(pm.id()))
                .sorted(Comparator
                        .comparingInt(PaymentMethodDto::discount).reversed()
                        .thenComparing(PaymentMethodDto::id))
                .toList();

        for (var pm : methods) {
            if (pm.limit().compareTo(BigDecimal.ZERO) <= 0) continue;
            BigDecimal toUse = pm.limit().min(remaining);
            pay(pm.id(), toUse, methodMap, spent);
            remaining = remaining.subtract(toUse);
            if (remaining.compareTo(BigDecimal.ZERO) == 0) break;
        }

        if (remaining.compareTo(BigDecimal.ZERO) > 0) {
            System.err.println("No funds to pay " + remaining);
        }
    }
}