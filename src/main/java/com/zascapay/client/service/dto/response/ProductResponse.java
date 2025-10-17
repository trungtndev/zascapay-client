package com.zascapay.client.service.dto.response;

import com.google.gson.annotations.SerializedName;

public class ProductResponse {
    @SerializedName("product_id")
    private int productId;

    @SerializedName("product_name")
    private String productName;

    // API returns price as string or null in the example; keep it as String to preserve format
    @SerializedName("price")
    private String price;

    // No-arg constructor for Gson
    public ProductResponse() {
    }

    public ProductResponse(int productId, String productName, String price) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ProductResponse{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
