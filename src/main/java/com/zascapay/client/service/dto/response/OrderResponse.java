package com.zascapay.client.service.dto.response;

import com.google.gson.annotations.SerializedName;

public class OrderResponse {
    @SerializedName("order_id")
    private int orderId;

    public OrderResponse() {}

    public OrderResponse(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "orderId=" + orderId +
                '}';
    }
}

