package com.store.model.product;

import java.math.BigDecimal;
import java.util.Optional;

import com.store.model.discount.DiscountPolicy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProductOrder {
    private Product product;
    private Integer quantity;
    private BigDecimal amount;
    private BigDecimal discount;
}
