package com.store.service.impl;

import java.math.BigDecimal;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.store.model.Order;
import com.store.service.CheckoutService;

@Service
public class CheckoutServiceImpl implements CheckoutService {
    @Override public void completeOrder(final Order order) {
        order.setPrice(order.getOrderItems().stream()
                .map(x->x.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        order.setDiscount(order.getOrderItems().stream()
                .filter(x->Objects.nonNull(x.getDiscount()))
                .map(x->x.getDiscount())
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}
