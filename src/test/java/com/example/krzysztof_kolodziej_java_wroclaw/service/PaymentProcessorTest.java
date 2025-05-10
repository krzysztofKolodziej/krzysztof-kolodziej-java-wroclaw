package com.example.krzysztof_kolodziej_java_wroclaw.service;

import com.example.krzysztof_kolodziej_java_wroclaw.dto.PaymentMethodDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentProcessorTest {
    private PaymentProcessor processor;
    private Map<String, PaymentMethodDto> methodMap;
    private Map<String, BigDecimal> spent;

    @BeforeEach
    void setUp() {
        processor = new PaymentProcessor();
        methodMap = new HashMap<>();
        methodMap.put("A", new PaymentMethodDto("A",0,new BigDecimal("50.00")));
        methodMap.put("B", new PaymentMethodDto("B",0,new BigDecimal("30.00")));
        spent = new HashMap<>();
    }

    @Test
    void applyDiscount() {
        BigDecimal res = processor.applyDiscount(new BigDecimal("100.00"),10);
        assertThat(res).isEqualByComparingTo(new BigDecimal("90.00"));
    }

    @Test
    void payAndLimitReduction() {
        processor.pay("A", new BigDecimal("20.00"), methodMap, spent);
        assertThat(methodMap.get("A").limit()).isEqualByComparingTo(new BigDecimal("30.00"));
        assertThat(spent.get("A")).isEqualByComparingTo(new BigDecimal("20.00"));
    }

    @Test
    void payAnyDistributesAcrossMethodsSortedByDiscount() {
        methodMap.put("A", new PaymentMethodDto("A",5,new BigDecimal("50.00")));
        methodMap.put("B", new PaymentMethodDto("B",10,new BigDecimal("30.00")));

        processor.payAny(new BigDecimal("60.00"), methodMap, spent, Set.of());

        assertThat(spent.get("B")).isEqualByComparingTo(new BigDecimal("30.00"));
        assertThat(spent.get("A")).isEqualByComparingTo(new BigDecimal("30.00"));
    }
}
