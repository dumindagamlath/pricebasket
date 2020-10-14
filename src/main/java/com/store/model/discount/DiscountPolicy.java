package com.store.model.discount;

import java.math.BigDecimal;

import com.store.model.Order;
import com.store.model.product.ProductOrder;

public interface DiscountPolicy {
    public void discount(ProductOrder productOrder, Order order);
}
