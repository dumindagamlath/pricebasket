package com.store.model.discount.impl;

import java.math.BigDecimal;
import java.util.Optional;

import com.store.model.Order;
import com.store.model.discount.DiscountPolicy;
import com.store.model.product.ProductOrder;

/**
 * Percentage discount policy
 * eg. Apples have a 10% discount off their normal price this week
 */
public class PercentageDiscountPolicy implements DiscountPolicy {
    private final Double productDiscount;

    /**
     * Constructor
     * @param discount Applicable percentage discount
     */
    public PercentageDiscountPolicy(Double discount) {
        productDiscount = discount;
    }

    /**
     * Apply percentage discount to the <code>ProductOrder</code>
     * @param productOrder <code>ProductOrder</code>
     * @param order <code>Order</code>
     */
    @Override
    public void discount(final ProductOrder productOrder, final Order order) {
        productOrder.setDiscount(productOrder.getAmount()
                    .multiply(BigDecimal.valueOf(productDiscount)));
    }
}
