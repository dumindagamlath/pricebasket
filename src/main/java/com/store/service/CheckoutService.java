package com.store.service;

import com.store.model.Order;

public interface CheckoutService {
    void completeOrder(Order order);
}
