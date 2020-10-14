package com.store.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.store.model.Order;
import com.store.service.CheckoutService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Service
public class CheckoutServiceImpl implements CheckoutService {
    @Override public void completeOrder(final Order order) {
        order.setPrice(order.getOrderItems().stream()
                .map(x->x.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        order.setDiscount(order.getOrderItems().stream()
                .map(x->x.getDiscount())
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}
