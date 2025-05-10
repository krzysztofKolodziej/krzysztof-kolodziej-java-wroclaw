package com.example.krzysztof_kolodziej_java_wroclaw.service;

import com.example.krzysztof_kolodziej_java_wroclaw.dto.OrderDto;
import com.example.krzysztof_kolodziej_java_wroclaw.dto.PaymentMethodDto;
import com.example.krzysztof_kolodziej_java_wroclaw.dto.PaymentResultDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PaymentOptimizerServiceTest {

    @Autowired
    private PaymentOptimizerService service;

    @Test
    void exampleScenario() {
        List<OrderDto> orders = List.of(
                new OrderDto("ORDER1", new BigDecimal("100.00"), List.of("mZysk")),
                new OrderDto("ORDER2", new BigDecimal("200.00"), List.of("BosBankrut")),
                new OrderDto("ORDER3", new BigDecimal("150.00"), List.of("mZysk","BosBankrut")),
                new OrderDto("ORDER4", new BigDecimal("50.00"), null)
        );
        List<PaymentMethodDto> methods = List.of(
                new PaymentMethodDto("PUNKTY",15, new BigDecimal("100.00")),
                new PaymentMethodDto("mZysk",10, new BigDecimal("180.00")),
                new PaymentMethodDto("BosBankrut",5,new BigDecimal("200.00"))
        );
        List<PaymentResultDto> results = service.optimize(orders, methods);
        Map<String, BigDecimal> map = results.stream()
                .collect(Collectors.toMap(PaymentResultDto::paymentMethodId, PaymentResultDto::amount));

        assertThat(map).containsExactlyInAnyOrderEntriesOf(Map.of(
                "mZysk", new BigDecimal("165.00"),
                "BosBankrut", new BigDecimal("190.00"),
                "PUNKTY", new BigDecimal("100.00")
        ));
    }
}