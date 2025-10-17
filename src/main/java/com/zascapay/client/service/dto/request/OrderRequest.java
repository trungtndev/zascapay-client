package com.zascapay.client.service.dto.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderRequest {
    @SerializedName("items")
    private List<OrderItem> items;

    public OrderRequest() {}

    public OrderRequest(List<OrderItem> items) {
        this.items = items;
    }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "items=" + items +
                '}';
    }
}

