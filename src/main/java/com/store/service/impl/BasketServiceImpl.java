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

/**
 * Service to create an order from a given list of items.
 * If any item is not in the list then ProductNotFoundException will be thrown.
 */
@AllArgsConstructor
@Service
public class BasketServiceImpl implements BasketService {
    @Autowired
    private ProductService productLookupService;

    /**
     * <code>ProductOrder</code> items which list items in the order
     */
    private final List<ProductOrder> itemsInBasket = new ArrayList<>();

    /**
     * Lookup the product by name and adds to the basket if not already in the order,
     * else increment the quantity and set the amount
     * @param name product name to be searched
     * @throws ProductNotFoundException as not in the stock/not found
     */
    private void addProduct(final String name) throws ProductNotFoundException {
        Product product = productLookupService.getProduct(name);
        // product found, check it exists in the list
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

    /**
     * Create an order from the given items
     * @param products Set of items to order
     * @return <code>Order</code>
     */
    public Order createBasket(final String... products) {
        Arrays.asList(products)
                .forEach(p -> addProduct(p));
        return new Order(itemsInBasket, BigDecimal.ZERO, BigDecimal.ZERO);
    }

}
