package com.store.model.discount.impl;

import java.math.BigDecimal;
import java.util.Optional;

import com.store.model.Order;
import com.store.model.discount.DiscountPolicy;
import com.store.model.product.Product;
import com.store.model.product.ProductOrder;

public class MultiBuyDiscountPolicy implements DiscountPolicy {
    private final Product productDiscount;
    private final Integer itemCount;
    private final Double applicableDiscount;

    public MultiBuyDiscountPolicy(Integer count, Product productReduce, Double itemDiscount) {
        productDiscount = productReduce;
        itemCount = count;
        applicableDiscount = itemDiscount;
    }
    @Override public void discount(final ProductOrder productOrder, final Order order) {
        int noOfDiscounts = productOrder.getQuantity() / itemCount;
        Optional<ProductOrder> productOrderApplicable = order.getOrderItems()
                .stream()
                .filter(p->p.getProduct().getName().equals(productDiscount.getName()))
                .findFirst();
        if (noOfDiscounts > 0 && productOrderApplicable.isPresent()) {
            ProductOrder productItemOrder = productOrderApplicable.get();
            productItemOrder.setDiscount(productItemOrder.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(noOfDiscounts))
                    .multiply(BigDecimal.valueOf(applicableDiscount)));
        }
    }
}
