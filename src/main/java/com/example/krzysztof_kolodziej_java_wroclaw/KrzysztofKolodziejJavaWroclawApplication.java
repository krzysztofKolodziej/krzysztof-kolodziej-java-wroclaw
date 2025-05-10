package com.example.krzysztof_kolodziej_java_wroclaw;

import com.example.krzysztof_kolodziej_java_wroclaw.dto.OrderDto;
import com.example.krzysztof_kolodziej_java_wroclaw.dto.PaymentMethodDto;
import com.example.krzysztof_kolodziej_java_wroclaw.dto.PaymentResultDto;
import com.example.krzysztof_kolodziej_java_wroclaw.service.PaymentOptimizerService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
public class KrzysztofKolodziejJavaWroclawApplication {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: java -jar /home/…/app.jar /home/…/orders.json /home/…/paymentmethods.json");
            return;
        }

        ApplicationContext ctx = SpringApplication.run(KrzysztofKolodziejJavaWroclawApplication.class, args);
        JsonUtils jsonUtils = ctx.getBean(JsonUtils.class);
        PaymentOptimizerService service = ctx.getBean(PaymentOptimizerService.class);

        List<OrderDto> orders = jsonUtils.readList(args[0], new TypeReference<>() {
        });
        List<PaymentMethodDto> methods = jsonUtils.readList(args[1], new TypeReference<>() {
        });

        List<PaymentResultDto> results = service.optimize(orders, methods);
        results.forEach(r -> System.out.println(r.paymentMethodId() + " " + r.amount()));
    }
}