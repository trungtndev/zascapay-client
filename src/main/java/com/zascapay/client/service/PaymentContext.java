package com.zascapay.client.service;

import com.zascapay.client.service.dto.response.PaymentResponse;

/**
 * Simple in-memory holder for the last payment response to share between controllers.
 */
public class PaymentContext {
    private static volatile PaymentResponse lastPayment;

    public static PaymentResponse getLastPayment() {
        return lastPayment;
    }

    public static void setLastPayment(PaymentResponse p) {
        lastPayment = p;
    }

    private PaymentContext() {}
}

