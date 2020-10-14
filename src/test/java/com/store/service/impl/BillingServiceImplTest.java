package com.store.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.store.service.BillingService;

@ExtendWith(MockitoExtension.class)
public class BillingServiceImplTest {
    private BillingService billingService;

    @BeforeEach
    void init(){
        billingService = new BillingServiceImpl();
    }
}
