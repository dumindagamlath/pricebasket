package com.store.service;

import java.util.List;

import com.store.exception.ProductNotFoundException;
import com.store.model.product.Product;

public interface ProductService {
    public Product getProduct(String name) throws ProductNotFoundException;
}
