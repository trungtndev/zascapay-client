package com.zascapay.client.service.dto.request;

import com.google.gson.annotations.SerializedName;

public class OrderItem {
    @SerializedName("product_id")
    private int productId;

    @SerializedName("quantity")
    private int quantity;

    public OrderItem() {}

    public OrderItem(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}

