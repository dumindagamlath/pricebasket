package com.store.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.store.exception.ProductNotFoundException;
import com.store.model.discount.impl.MultiBuyDiscountPolicy;
import com.store.model.discount.impl.PercentageDiscountPolicy;
import com.store.model.product.Product;
import com.store.service.ProductService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static List<Product> products = new ArrayList<>();

    static {
        Product apple = new Product("Apple", BigDecimal.valueOf(1.00), new PercentageDiscountPolicy(0.1));
        products.add(apple);
        Product bread = new Product("Bread", BigDecimal.valueOf(0.80), null);
        products.add(bread);
        Product soup = new Product("Soup", BigDecimal.valueOf(0.65), new MultiBuyDiscountPolicy(2, bread, 0.5));
        products.add(soup);
        Product milk = new Product("Milk", BigDecimal.valueOf(1.30), null);
        products.add(milk);
    }

    @Override
    public Product getProduct(String name) throws ProductNotFoundException {
        Optional<Product> product = products.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst();
        if (!product.isPresent()) {
            throw new ProductNotFoundException("Product '"+name +"' not found.");
        }

        return product.get();
    }
}
