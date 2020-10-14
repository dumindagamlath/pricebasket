package com.store.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.store.model.Order;
import com.store.model.discount.impl.MultiBuyDiscountPolicy;
import com.store.model.discount.impl.PercentageDiscountPolicy;
import com.store.model.product.Product;
import com.store.model.product.ProductOrder;
import com.store.service.BillingService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class BillingServiceImplTest {
    private BillingService billingService;
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void init(){
        billingService = new BillingServiceImpl();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void printBill_without_offers_should_capture_output() {
        billingService.printBill(getItemOrderWithoutDiscount());

        verifyOutputWithoutOffers(BigDecimal.valueOf(1.20));
    }

    @Test
    void printBill_with_offers_should_capture_output() {
        billingService.printBill(getItemOrderWithDiscount());

        assertTrue(outputStreamCaptor.toString()
                .trim().contains("Apple 10% off: -12p"));
        verifyOutputWithOffers(BigDecimal.valueOf(1.20), BigDecimal.valueOf(0.12));
    }

    @Test
    void printBill_with_offers_should_capture_output_multiple_discounts() {
        billingService.printBill(getMultipleItemMultiBuyOrderWithMultipleDiscounts());

        assertTrue(outputStreamCaptor.toString()
                .trim().contains("Apple 50% off: -125p"));
        assertTrue(outputStreamCaptor.toString()
                .trim().contains("Bread 20% off: -48p"));
        verifyOutputWithOffers(BigDecimal.valueOf(7.50), BigDecimal.valueOf(1.73));
    }

    private void verifyOutputWithoutOffers(BigDecimal amount) {
        assertTrue(outputStreamCaptor.toString()
                .trim().contains(String.format("Subtotal: £%.2f", amount)));
        assertTrue(outputStreamCaptor.toString()
                .trim().contains("(No offers available)"));
        assertTrue(outputStreamCaptor.toString()
                .trim().contains(String.format("Total: £%.2f", amount)));
    }

    private void verifyOutputWithOffers(BigDecimal amount, BigDecimal discount) {
        assertTrue(outputStreamCaptor.toString()
                .trim().contains(String.format("Subtotal: £%.2f", amount)));
        assertTrue(outputStreamCaptor.toString()
                .trim().contains(String.format("Total: £%.2f", amount.subtract(discount))));
    }

    private Order getItemOrderWithoutDiscount() {
        Order newOrder = new Order();
        List<ProductOrder> orderItems = new ArrayList<>();
        Product apple = Product.builder()
                .name("Apple")
                .price(BigDecimal.valueOf(1.20)).build();
        orderItems.add(ProductOrder.builder()
                .product(apple)
                .quantity(1)
                .amount(BigDecimal.valueOf(1.20))
                .build());
        newOrder.setOrderItems(orderItems);
        newOrder.setDiscount(BigDecimal.ZERO);
        newOrder.setPrice(BigDecimal.valueOf(1.20));
        return newOrder;
    }

    private Order getItemOrderWithDiscount() {
        Order newOrder = new Order();
        List<ProductOrder> orderItems = new ArrayList<>();
        Product apple = Product.builder()
                .name("Apple")
                .price(BigDecimal.valueOf(1.20))
                .discountPolicy(new PercentageDiscountPolicy(0.10)).build();
        orderItems.add(ProductOrder.builder()
                .product(apple)
                .quantity(1)
                .amount(BigDecimal.valueOf(1.20))
                .discount(BigDecimal.valueOf(0.12))
                .build());
        newOrder.setOrderItems(orderItems);
        newOrder.setDiscount(BigDecimal.valueOf(0.12));
        newOrder.setPrice(BigDecimal.valueOf(1.20));
        return newOrder;
    }

    private Order getMultipleItemMultiBuyOrderWithMultipleDiscounts() {
        Order newOrder = new Order();
        List<ProductOrder> orderItems = new ArrayList<>();
        Product bread = Product.builder()
                .name("Bread")
                .price(BigDecimal.valueOf(1.20)).build();
        Product soup = Product.builder()
                .name("Soup")
                .price(BigDecimal.valueOf(0.65))
                .discountPolicy(new MultiBuyDiscountPolicy(2, bread, 0.20)).build();
        Product apple = Product.builder()
                .name("Apple")
                .price(BigDecimal.valueOf(1.25))
                .discountPolicy(new PercentageDiscountPolicy(0.50)).build();
        orderItems.add(ProductOrder.builder()
                .product(soup)
                .quantity(4)
                .amount(BigDecimal.valueOf(2.60))
                .build());
        orderItems.add(ProductOrder.builder()
                .product(bread)
                .quantity(2)
                .amount(BigDecimal.valueOf(2.40))
                .discount(BigDecimal.valueOf(0.48))
                .build());
        orderItems.add(ProductOrder.builder()
                .product(apple)
                .quantity(2)
                .amount(BigDecimal.valueOf(2.50))
                .discount(BigDecimal.valueOf(1.25))
                .build());
        newOrder.setOrderItems(orderItems);
        newOrder.setDiscount(BigDecimal.valueOf(1.73));
        newOrder.setPrice(BigDecimal.valueOf(7.50));
        return newOrder;
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

}
