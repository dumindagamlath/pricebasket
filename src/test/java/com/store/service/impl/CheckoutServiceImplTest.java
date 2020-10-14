package com.store.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.store.model.Order;
import com.store.model.discount.impl.MultiBuyDiscountPolicy;
import com.store.model.discount.impl.PercentageDiscountPolicy;
import com.store.model.product.Product;
import com.store.model.product.ProductOrder;
import com.store.service.CheckoutService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class CheckoutServiceImplTest {
    private CheckoutService checkoutService;

    @BeforeEach
    void init(){
        checkoutService = new CheckoutServiceImpl();
    }

    @Test
    void completeOrder_should_return_no_discount() {
        Order order = getSingleItemOrderWithoutDiscount();

        checkoutService.completeOrder(order);

        assertEquals(1, order.getOrderItems().size());
        assertEquals(BigDecimal.valueOf(1.20), order.getPrice());
        assertEquals(BigDecimal.ZERO, order.getDiscount());
    }

    @Test
    void completeOrder_should_return_discount() {
        Order order = getSingleItemOrderWithDiscount();

        checkoutService.completeOrder(order);

        assertEquals(1, order.getOrderItems().size());
        assertEquals(BigDecimal.valueOf(1.20), order.getPrice());
        assertEquals(BigDecimal.valueOf(0.12), order.getDiscount());
    }

    @Test
    void completeOrder_should_return_discount_for_multiple_quantities() {
        Order order = getSingleItemMultiBuyOrderWithDiscount();

        checkoutService.completeOrder(order);

        assertEquals(1, order.getOrderItems().size());
        assertEquals(BigDecimal.valueOf(2.50), order.getPrice());
        assertEquals(BigDecimal.valueOf(1.25), order.getDiscount());
    }

    @Test
    void completeOrder_should_return_discount_for_multiple_items() {
        Order order = getMultipleItemMultiBuyOrderWithDiscount();

        checkoutService.completeOrder(order);

        assertEquals(2, order.getOrderItems().size());
        assertEquals(BigDecimal.valueOf(5.00), order.getPrice());
        assertEquals(BigDecimal.valueOf(0.48), order.getDiscount());
    }

    @Test
    void completeOrder_should_return_discount_for_multiple_items_with_multiple_discounts() {
        Order order = getMultipleItemMultiBuyOrderWithMultipleDiscounts();

        checkoutService.completeOrder(order);

        assertEquals(3, order.getOrderItems().size());
        assertEquals(BigDecimal.valueOf(7.50), order.getPrice());
        assertEquals(BigDecimal.valueOf(1.73), order.getDiscount());
    }

    private Order getSingleItemOrderWithoutDiscount() {
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
        return newOrder;
    }

    private Order getSingleItemOrderWithDiscount() {
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
        return newOrder;
    }

    private Order getSingleItemMultiBuyOrderWithDiscount() {
        Order newOrder = new Order();
        List<ProductOrder> orderItems = new ArrayList<>();
        Product apple = Product.builder()
                .name("Apple")
                .price(BigDecimal.valueOf(1.25))
                .discountPolicy(new PercentageDiscountPolicy(0.50)).build();
        orderItems.add(ProductOrder.builder()
                .product(apple)
                .quantity(2)
                .amount(BigDecimal.valueOf(2.50))
                .discount(BigDecimal.valueOf(1.25))
                .build());
        newOrder.setOrderItems(orderItems);
        return newOrder;
    }

    private Order getMultipleItemMultiBuyOrderWithDiscount() {
        Order newOrder = new Order();
        List<ProductOrder> orderItems = new ArrayList<>();
        Product bread = Product.builder()
                .name("Bread")
                .price(BigDecimal.valueOf(1.20)).build();
        Product soup = Product.builder()
                .name("Soup")
                .price(BigDecimal.valueOf(0.65))
                .discountPolicy(new MultiBuyDiscountPolicy(2, bread, 0.20)).build();
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
        newOrder.setOrderItems(orderItems);
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
        return newOrder;
    }
}
