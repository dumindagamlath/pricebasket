package com.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.store.exception.ProductNotFoundException;
import com.store.model.Order;
import com.store.service.BasketService;
import com.store.service.BillingService;
import com.store.service.CheckoutService;
import com.store.service.DiscountService;

@SpringBootApplication
public class PriceBasket implements CommandLineRunner {

    @Autowired
    private BasketService basketService;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private BillingService billingService;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(PriceBasket.class);
        application.run(args);
    }

    @Override public void run(final String... args) throws Exception {
        Order newOrder = null;
        // Create order with provided list of items
        try {
            newOrder = basketService.createBasket(args);
        } catch(ProductNotFoundException pnfe) {
            System.out.println(pnfe.getMessage());
        }

        if (newOrder != null) {
            // Apply the discounts for product lines
            discountService.applyDiscount(newOrder);

            // Calculate the subtotal, total of the order
            checkoutService.completeOrder(newOrder);

            // Print the receipt
            billingService.printBill(newOrder);
        }
    }
}
