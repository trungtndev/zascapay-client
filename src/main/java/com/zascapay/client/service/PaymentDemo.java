package com.zascapay.client.service;

import com.zascapay.client.service.dto.response.PaymentResponse;

public class PaymentDemo {
    public static void main(String[] args) {
        String baseUrl = "http://localhost:8888"; // change to your API base URL
        int orderId = 123; // sample order id

        PaymentClient client = new PaymentClient(baseUrl);
        try {
            PaymentResponse resp = client.createPayment(orderId);
            System.out.println("Payment created: " + resp.getId() + ", status=" + resp.getStatus());
            if (resp.getItems() != null) {
                resp.getItems().forEach(item -> System.out.println(" - " + item.getName() + " x" + item.getQuantity() + " = " + item.getLineTotal()));
            }
        } catch (Exception e) {
            System.err.println("Payment failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

