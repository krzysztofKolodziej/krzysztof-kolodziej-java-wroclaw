package com.example.krzysztof_kolodziej_java_wroclaw.service;

import com.example.krzysztof_kolodziej_java_wroclaw.dto.OrderDto;
import com.example.krzysztof_kolodziej_java_wroclaw.dto.PaymentMethodDto;
import com.example.krzysztof_kolodziej_java_wroclaw.dto.PaymentResultDto;
import com.example.krzysztof_kolodziej_java_wroclaw.service.rules.PaymentRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentOptimizerService {

    private final List<PaymentRule> rules;

    public List<PaymentResultDto> optimize(List<OrderDto> orders, List<PaymentMethodDto> allMethods) {
        var methodMap = allMethods.stream()
                .collect(Collectors.toMap(PaymentMethodDto::id,
                        m -> new PaymentMethodDto(m.id(), m.discount(), m.limit()),
                        (a, b) -> a,
                        LinkedHashMap::new));

        var spent = new HashMap<String, BigDecimal>();
        var paidOrders = new HashSet<String>();

        for (var rule : rules) {
            rule.apply(orders, methodMap, spent, paidOrders);
        }

        return spent.entrySet().stream()
                .map(e -> new PaymentResultDto(e.getKey(), e.getValue().setScale(2, RoundingMode.HALF_UP)))
                .toList();
    }
}