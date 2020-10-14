package com.store.service.impl;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.store.model.Order;
import com.store.service.DiscountService;

/**
 * Service to apply discount for <code>ProductOrder</code>
 * Discount will be applied if the product has a discount policy
 */
@Service
public class DiscountServiceImpl implements DiscountService {
    @Override public void applyDiscount(final Order order) {
        // Apply for each element with a discount policy
        order.getOrderItems()
                .stream()
                .filter(p -> Objects.nonNull(p.getProduct().getDiscountPolicy()))
                .forEach(i->i.getProduct().getDiscountPolicy().discount(i, order));
    }
}
