package com.store.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.store.exception.ProductNotFoundException;
import com.store.model.Order;
import com.store.model.discount.impl.MultiBuyDiscountPolicy;
import com.store.model.discount.impl.PercentageDiscountPolicy;
import com.store.model.product.Product;
import com.store.model.product.ProductOrder;
import com.store.service.DiscountService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DiscountServiceImplTest {

    private DiscountService discountService;

    @BeforeEach
    void init(){
        discountService = new DiscountServiceImpl();
    }

    @Test
    void applyDiscount_should_not_apply_for_product_without_DiscountPolicy() {
        Order order = getSingleItemOrderWithoutDiscountPolicy();

        discountService.applyDiscount(order);

        assertEquals(1, order.getOrderItems().size());
        assertEquals(BigDecimal.valueOf(1.20), order.getOrderItems().get(0).getAmount());
        assertNull(order.getOrderItems().get(0).getDiscount());
    }

    @Test
    void applyDiscount_should_apply_discount_for_product_with_PercentageDiscountPolicy_with_single_item() {
        Order order = getSingleItemOrderWithPercentageDiscountPolicy();

        discountService.applyDiscount(order);

        assertEquals(1, order.getOrderItems().size());
        assertEquals(BigDecimal.valueOf(1.20), order.getOrderItems().get(0).getAmount());
        assertEquals(BigDecimal.valueOf(0.12), order.getOrderItems().get(0).getDiscount());
    }

    @Test
    void applyDiscount_should_return_discount_for_product_with_PercentageDiscountPolicy_with_multiple_items() {
        Order order = getMultipleItemOrderWithPercentageDiscountPolicy();

        discountService.applyDiscount(order);

        assertEquals(1, order.getOrderItems().size());
        assertEquals(BigDecimal.valueOf(2.50), order.getOrderItems().get(0).getAmount());
        assertEquals(BigDecimal.valueOf(1.25), order.getOrderItems().get(0).getDiscount());
    }

    @Test
    void applyDiscount_should_not_apply_discount_for_product_with_MultiBuyDiscountPolicy_with_single_item() {
        Order order = getSingleItemOrderWithMultiBuyDiscountPolicyWithoutReducedItem();

        discountService.applyDiscount(order);

        assertEquals(1, order.getOrderItems().size());
        assertEquals(BigDecimal.valueOf(0.65), order.getOrderItems().get(0).getAmount());
        assertNull(order.getOrderItems().get(0).getDiscount());
    }

    @Test
    void applyDiscount_should_not_apply_discount_for_product_with_MultiBuyDiscountPolicy_with_multiple_item() {
        Order order = getMultipleItemOrderWithMultiBuyDiscountPolicyWithoutReducedItem();

        discountService.applyDiscount(order);

        assertEquals(1, order.getOrderItems().size());
        assertEquals(BigDecimal.valueOf(1.30), order.getOrderItems().get(0).getAmount());
        assertNull(order.getOrderItems().get(0).getDiscount());
    }

    @Test
    void applyDiscount_should_not_apply_discount_for_product_with_MultiBuyDiscountPolicy_with_single_item_without_quantity_required() {
        Order order = getSingleItemOrderWithMultiBuyDiscountPolicyWithReducedItem();

        discountService.applyDiscount(order);

        assertEquals(2, order.getOrderItems().size());
        assertEquals(BigDecimal.valueOf(0.65), order.getOrderItems().get(0).getAmount());
        assertNull(order.getOrderItems().get(0).getDiscount());
        assertEquals(BigDecimal.valueOf(1.20), order.getOrderItems().get(1).getAmount());
        assertNull(order.getOrderItems().get(1).getDiscount());
    }

    @Test
    void applyDiscount_should_apply_discount_for_product_with_MultiBuyDiscountPolicy_with_single_item_with_quantity_required() {
        Order order = getMultipleItemOrderWithMultiBuyDiscountPolicyWithReducedItem();

        discountService.applyDiscount(order);

        assertEquals(2, order.getOrderItems().size());
        assertEquals(BigDecimal.valueOf(1.30), order.getOrderItems().get(0).getAmount());
        assertNull(order.getOrderItems().get(0).getDiscount());
        assertEquals(BigDecimal.valueOf(1.20), order.getOrderItems().get(1).getAmount());
        assertEquals(BigDecimal.valueOf(0.24), order.getOrderItems().get(1).getDiscount());
    }

    @Test
    void applyDiscount_should_apply_discount_for_product_with_MultiBuyDiscountPolicy_with_single_applicable_product_item() {
        Order order = getMultipleItemOrderWithMultiBuyDiscountPolicyWithOnlySingleApplicableReducedItem();

        discountService.applyDiscount(order);

        assertEquals(2, order.getOrderItems().size());
        assertEquals(BigDecimal.valueOf(1.95), order.getOrderItems().get(0).getAmount());
        assertNull(order.getOrderItems().get(0).getDiscount());
        assertEquals(BigDecimal.valueOf(2.40), order.getOrderItems().get(1).getAmount());
        assertEquals(BigDecimal.valueOf(0.24), order.getOrderItems().get(1).getDiscount());
    }

    @Test
    void applyDiscount_should_apply_discount_for_product_with_MultiBuyDiscountPolicy_with_multiple_applicable_product_quantities() {
        Order order = getMultipleItemOrderWithMultiBuyDiscountPolicyWithMultipleApplicableReducedItem();

        discountService.applyDiscount(order);

        assertEquals(2, order.getOrderItems().size());
        assertEquals(BigDecimal.valueOf(2.60), order.getOrderItems().get(0).getAmount());
        assertNull(order.getOrderItems().get(0).getDiscount());
        assertEquals(BigDecimal.valueOf(2.40), order.getOrderItems().get(1).getAmount());
        assertEquals(BigDecimal.valueOf(0.48), order.getOrderItems().get(1).getDiscount());
    }

    private Order getSingleItemOrderWithoutDiscountPolicy() {
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

    private Order getSingleItemOrderWithPercentageDiscountPolicy() {
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
                .build());
        newOrder.setOrderItems(orderItems);
        return newOrder;
    }

    private Order getMultipleItemOrderWithPercentageDiscountPolicy() {
        Order newOrder = new Order();
        List<ProductOrder> orderItems = new ArrayList<>();
        Product apple = Product.builder()
                .name("Apple")
                .price(BigDecimal.valueOf(1.22))
                .discountPolicy(new PercentageDiscountPolicy(0.50)).build();
        orderItems.add(ProductOrder.builder()
                .product(apple)
                .quantity(2)
                .amount(BigDecimal.valueOf(2.50))
                .build());
        newOrder.setOrderItems(orderItems);
        return newOrder;
    }

    private Order getSingleItemOrderWithMultiBuyDiscountPolicyWithoutReducedItem() {
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
                .quantity(1)
                .amount(BigDecimal.valueOf(0.65))
                .build());
        newOrder.setOrderItems(orderItems);
        return newOrder;
    }

    private Order getMultipleItemOrderWithMultiBuyDiscountPolicyWithoutReducedItem() {
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
                .quantity(2)
                .amount(BigDecimal.valueOf(1.30))
                .build());
        newOrder.setOrderItems(orderItems);
        return newOrder;
    }

    private Order getSingleItemOrderWithMultiBuyDiscountPolicyWithReducedItem() {
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
                .quantity(1)
                .amount(BigDecimal.valueOf(0.65))
                .build());
        orderItems.add(ProductOrder.builder()
                .product(bread)
                .quantity(1)
                .amount(BigDecimal.valueOf(1.20))
                .build());
        newOrder.setOrderItems(orderItems);
        return newOrder;
    }

    private Order getMultipleItemOrderWithMultiBuyDiscountPolicyWithReducedItem() {
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
                .quantity(2)
                .amount(BigDecimal.valueOf(1.30))
                .build());
        orderItems.add(ProductOrder.builder()
                .product(bread)
                .quantity(1)
                .amount(BigDecimal.valueOf(1.20))
                .build());
        newOrder.setOrderItems(orderItems);
        return newOrder;
    }

    private Order getMultipleItemOrderWithMultiBuyDiscountPolicyWithOnlySingleApplicableReducedItem() {
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
                .quantity(3)
                .amount(BigDecimal.valueOf(1.95))
                .build());
        orderItems.add(ProductOrder.builder()
                .product(bread)
                .quantity(2)
                .amount(BigDecimal.valueOf(2.40))
                .build());
        newOrder.setOrderItems(orderItems);
        return newOrder;
    }

    private Order getMultipleItemOrderWithMultiBuyDiscountPolicyWithMultipleApplicableReducedItem() {
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
                .build());
        newOrder.setOrderItems(orderItems);
        return newOrder;
    }
}
