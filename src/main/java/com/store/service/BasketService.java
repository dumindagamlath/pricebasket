package com.store.service;

import com.store.exception.ProductNotFoundException;
import com.store.model.Order;

public interface BasketService {
    public Order createBasket(final String... products) throws ProductNotFoundException;
}
