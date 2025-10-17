package com.zascapay.client.service.dto.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ScanResponse {
    @SerializedName("products")
    private List<ProductResponse> products;

    @SerializedName("image")
    private String image; // base64 encoded image

    public ScanResponse() {
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public void setProducts(List<ProductResponse> products) {
        this.products = products;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ScanResponse{" +
                "products=" + products +
                ", image='" + image + '\'' +
                '}';
    }
}
