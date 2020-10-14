package com.store.model.discount.impl;

import java.math.BigDecimal;
import java.util.Optional;

import com.store.model.Order;
import com.store.model.discount.DiscountPolicy;
import com.store.model.product.Product;
import com.store.model.product.ProductOrder;

/**
 * Custom discount policy for specific products
 * This policy will give a discount on a different product if multibuy a product
 * eg. Buy 2 tins of soup and get a loaf of bread for half price
 */
public class MultiBuyDiscountPolicy implements DiscountPolicy {
    private final Product productDiscount;
    private final Integer itemCount;
    private final Double applicableDiscount;

    /**
     * Constructor
     * @param count Offer product count in order
     * @param productReduce Discounted product
     * @param itemDiscount Discount
     */
    public MultiBuyDiscountPolicy(Integer count, Product productReduce, Double itemDiscount) {
        productDiscount = productReduce;
        itemCount = count;
        applicableDiscount = itemDiscount;
    }

    /**
     * @param productOrder Order which offer is valid if buys certain quantity
     * @param order The order which may/may not have the disounted product
     */
    @Override public void discount(final ProductOrder productOrder, final Order order) {
        // Check for offer applicable times
        int noOfDiscounts = productOrder.getQuantity() / itemCount;
        // Check for discounted item in order
        Optional<ProductOrder> productOrderApplicable = order.getOrderItems()
                .stream()
                .filter(p->p.getProduct().getName().equals(productDiscount.getName()))
                .findFirst();
        // Apply discount if discounted item is present in order and offer quantity is satisfied.
        if (noOfDiscounts > 0 && productOrderApplicable.isPresent()) {
            ProductOrder productItemOrder = productOrderApplicable.get();
            productItemOrder.setDiscount(productItemOrder.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(noOfDiscounts))
                    .multiply(BigDecimal.valueOf(applicableDiscount)));
        }
    }
}
