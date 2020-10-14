package com.store.exception;

/**
 * Exception when product is not found.
 */
public class ProductNotFoundException extends RuntimeException{
    /**
     * Product not found
     * @param message detail message with the product
     */
    public ProductNotFoundException(String message) {
        super(message);
    }
}
