package com.store.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.store.model.Order;
import com.store.model.product.ProductOrder;
import com.store.service.BillingService;

@Service
public class BillingServiceImpl implements BillingService {
    @Override public void printBill(final Order newOrder) {
        printBreakdown(newOrder);
    }

    private void printBreakdown(Order order) {
        output(String.format("Subtotal: £%.2f", order.getPrice()));
        List<ProductOrder> discountedProducts = order.getOrderItems().stream()
                .filter(p-> Objects.nonNull(p.getDiscount()) && p.getDiscount().compareTo(BigDecimal.ZERO) == 1)
                .collect(Collectors.toList());

        if (discountedProducts.isEmpty()) {
            output("(No offers available)");
        } else {
            discountedProducts.stream()
                    .forEach(p -> {
                        output(String.format("%s %d%% off: -%dp",
                                p.getProduct().getName(),
                                p.getDiscount().divide(p.getAmount()).multiply(BigDecimal.valueOf(100.00)).intValueExact(),
                                p.getDiscount().multiply(BigDecimal.valueOf(100.00)).intValueExact()));
                    });
        }
        output(String.format("Total: £%.2f", order.getPrice().subtract(order.getDiscount())));
    }

    private void output (String output) {
        System.out.println(output);
    }
}
