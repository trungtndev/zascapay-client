package com.zascapay.client.service.dto.request;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class PaymentRequest {
    @SerializedName("order_id")
    private int orderId;

    @SerializedName("method")
    private String method;

    @SerializedName("metadata")
    private Map<String, String> metadata;

    public PaymentRequest() {}

    public PaymentRequest(int orderId, String method, Map<String, String> metadata) {
        this.orderId = orderId;
        this.method = method;
        this.metadata = metadata;
    }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public Map<String, String> getMetadata() { return metadata; }
    public void setMetadata(Map<String, String> metadata) { this.metadata = metadata; }

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "orderId=" + orderId +
                ", method='" + method + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}

