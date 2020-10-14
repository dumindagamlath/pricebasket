package com.store.model.product;

import java.math.BigDecimal;

import com.store.model.discount.DiscountPolicy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Product {
    private String name;
    private BigDecimal price;
    private DiscountPolicy discountPolicy;
}
