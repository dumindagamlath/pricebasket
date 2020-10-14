package com.store.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.store.model.discount.DiscountPolicy;
import com.store.model.product.ProductOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private List<ProductOrder> orderItems;
    private BigDecimal price;
    private BigDecimal discount;
}
