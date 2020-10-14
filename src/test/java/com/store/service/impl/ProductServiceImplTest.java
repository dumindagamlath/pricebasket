package com.store.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import com.store.exception.ProductNotFoundException;
import com.store.model.discount.impl.PercentageDiscountPolicy;
import com.store.model.product.Product;
import com.store.service.ProductService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    private ProductService productService;

    @BeforeEach
    void init(){
        productService = new ProductServiceImpl();
    }

    @Test
    void getProduct_should_throw_ProductNotFoundException_when_given_unavailable_item() throws ProductNotFoundException {

        Exception exception = assertThrows(ProductNotFoundException.class, () -> productService.getProduct("Pear"));

        assertEquals("Product 'Pear' not found.", exception.getMessage());
    }

    @Test
    void getProduct_should_return_available_product() throws ProductNotFoundException {

        Product product = productService.getProduct("Apple");

        assertEquals("Apple", product.getName());
    }
}
