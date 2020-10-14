package com.store.service.impl;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.store.exception.ProductNotFoundException;
import com.store.model.Order;
import com.store.model.discount.impl.PercentageDiscountPolicy;
import com.store.model.product.Product;
import com.store.service.BasketService;
import com.store.service.ProductService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BasketServiceImplTest {
    private BasketService basketService;

    @Mock
    private ProductService productService;

    @BeforeEach
    void init(){
        basketService = new BasketServiceImpl(productService);
    }

    @Test
    void createBasket_should_throw_ProductNotFoundException_when_given_unavailable_item() throws ProductNotFoundException {
        when(productService.getProduct("Pear"))
                .thenThrow(new ProductNotFoundException("Product 'Pear' not found."));

        Exception exception = assertThrows(ProductNotFoundException.class, () -> basketService.createBasket("Pear"));

        assertEquals("Product 'Pear' not found.", exception.getMessage());
    }

    @Test
    void createBasket_should_return_available_product_in_order() throws ProductNotFoundException {
        when(productService.getProduct("Apple"))
                .thenReturn(new Product("Apple", BigDecimal.ZERO, null));

        Order order = basketService.createBasket("Apple");

        assertEquals("Apple", order.getOrderItems().get(0).getProduct().getName());
    }

    @Test
    void createBasket_with_multiple_items_should_return_available_products_in_order() throws ProductNotFoundException {
        when(productService.getProduct("Apple"))
                .thenReturn(new Product("Apple", BigDecimal.ZERO, null));

        when(productService.getProduct("Milk"))
                .thenReturn(new Product("Milk", BigDecimal.ZERO, null));

        Order order = basketService.createBasket("Apple", "Milk");

        assertEquals("Apple", order.getOrderItems().get(0).getProduct().getName());
        assertEquals("Milk", order.getOrderItems().get(1).getProduct().getName());
    }

    @Test
    void createBasket_should_return_available_product_quantity_for_single_item() throws ProductNotFoundException {
        when(productService.getProduct("Apple"))
                .thenReturn(new Product("Apple", BigDecimal.ZERO, null));

        Order order = basketService.createBasket("Apple");

        assertEquals("Apple", order.getOrderItems().get(0).getProduct().getName());
        assertEquals(1, order.getOrderItems().get(0).getQuantity());
    }

    @Test
    void createBasket_should_return_available_product_quantity_for_multiple_items() throws ProductNotFoundException {
        when(productService.getProduct("Apple"))
                .thenReturn(new Product("Apple", BigDecimal.ZERO, null));

        Order order = basketService.createBasket("Apple", "Apple");

        assertEquals("Apple", order.getOrderItems().get(0).getProduct().getName());
        assertEquals(2, order.getOrderItems().get(0).getQuantity());
    }

    @Test
    void createBasket_should_return_available_product_order_amount_for_single_item() throws ProductNotFoundException {
        when(productService.getProduct("Apple"))
                .thenReturn(new Product("Apple", BigDecimal.valueOf(1.20), null));

        Order order = basketService.createBasket("Apple");

        assertEquals("Apple", order.getOrderItems().get(0).getProduct().getName());
        assertEquals(1, order.getOrderItems().get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(1.20), order.getOrderItems().get(0).getAmount());
    }

    @Test
    void createBasket_should_return_available_product_order_amount_for_multiple_items() throws ProductNotFoundException {
        when(productService.getProduct("Apple"))
                .thenReturn(new Product("Apple", BigDecimal.valueOf(1.20), null));

        Order order = basketService.createBasket("Apple", "Apple");

        assertEquals("Apple", order.getOrderItems().get(0).getProduct().getName());
        assertEquals(2, order.getOrderItems().get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(2.40), order.getOrderItems().get(0).getAmount());
    }

    @Test
    void createBasket_should_return_product_order_items_list() throws ProductNotFoundException {
        when(productService.getProduct("Apple"))
                .thenReturn(new Product("Apple", BigDecimal.valueOf(1.20), null));
        when(productService.getProduct("Milk"))
                .thenReturn(new Product("Milk", BigDecimal.valueOf(1.50), null));
        when(productService.getProduct("Soup"))
                .thenReturn(new Product("Soup", BigDecimal.valueOf(0.65), null));

        Order order = basketService.createBasket("Apple", "Apple", "Milk", "Milk", "Soup");

        assertEquals(3, order.getOrderItems().size());
    }
}
