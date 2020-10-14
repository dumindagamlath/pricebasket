package com.store.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.exception.ProductNotFoundException;
import com.store.model.Order;
import com.store.model.product.Product;
import com.store.model.product.ProductOrder;
import com.store.service.BasketService;
import com.store.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Service
public class BasketServiceImpl implements BasketService {
    @Autowired
    private ProductService productLookupService;

    private final List<ProductOrder> itemsInBasket = new ArrayList<>();

    private void addProduct(final String name) throws ProductNotFoundException {
        Product product = productLookupService.getProduct(name);
        Optional<ProductOrder> productOrder = itemsInBasket.stream()
                .filter(p -> p.getProduct().getName().equals(product.getName()))
                .findFirst();
        if (productOrder.isPresent()) {
            ProductOrder productOrderItem = productOrder.get();
            productOrderItem.setQuantity(productOrderItem.getQuantity() + 1);
            productOrderItem.setAmount(BigDecimal.valueOf(productOrderItem.getQuantity()).multiply(product.getPrice()));
        } else {
            itemsInBasket.add(new ProductOrder(product, 1, product.getPrice(), BigDecimal.ZERO));
        }
    }

    public Order createBasket(final String... products) {
        Arrays.asList(products)
                .forEach(p -> addProduct(p));
        return new Order(itemsInBasket, BigDecimal.ZERO, BigDecimal.ZERO);
    }

}
