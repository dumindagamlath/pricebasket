package com.store.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.store.service.CheckoutService;

@ExtendWith(MockitoExtension.class)
public class CheckoutServiceImplTest {
    private CheckoutService checkoutService;

    @BeforeEach
    void init(){
        checkoutService = new CheckoutServiceImpl();
    }
}
