package com.store.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.store.model.Order;
import com.store.model.product.ProductOrder;
import com.store.service.BillingService;

/**
 * Generate the output as below if discounts found
 * Subtotal: 			£3.10
 * Apples 10% off: 	   	  -10p
 * Total: 			     £3.00
 *
 * Generate the output as below if discounts not found
 * Subtotal: 			£1.30
 * (No offers available)
 * Total price: 		£1.30
 *
 */
@Service
public class BillingServiceImpl implements BillingService {
    @Override public void printBill(final Order newOrder) {
        printBreakdown(newOrder);
    }

    /**
     * Print the receipt subtotal, discounts and total
     * @param order <code>Order</code> to be printed
     */
    private void printBreakdown(Order order) {
        // Print subtotal
        output(String.format("Subtotal: £%.2f", order.getPrice()));
        List<ProductOrder> discountedProducts = order.getOrderItems().stream()
                .filter(p-> Objects.nonNull(p.getDiscount()) && p.getDiscount().compareTo(BigDecimal.ZERO) == 1)
                .collect(Collectors.toList());
        // Look for discounted products and print
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
        // Print the total
        output(String.format("Total: £%.2f", order.getPrice().subtract(order.getDiscount())));
    }

    /**
     * Print to console
     * @param output string
     */
    private void output (String output) {
        System.out.println(output);
    }
}
