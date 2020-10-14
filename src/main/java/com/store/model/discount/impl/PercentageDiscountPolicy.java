package com.store.model.discount.impl;

import java.math.BigDecimal;
import java.util.Optional;

import com.store.model.Order;
import com.store.model.discount.DiscountPolicy;
import com.store.model.product.ProductOrder;

public class PercentageDiscountPolicy implements DiscountPolicy {
    private final Double productDiscount;

    public PercentageDiscountPolicy(Double discount) {
        productDiscount = discount;
    }

    @Override
    public void discount(final ProductOrder productOrder, final Order order) {
        productOrder.setDiscount(productOrder.getAmount()
                    .multiply(BigDecimal.valueOf(productDiscount)));
    }
}
