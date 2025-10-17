package com.zascapay.client.service.dto.response;

import com.google.gson.annotations.SerializedName;

public class PaymentItem {
    @SerializedName("id")
    private int id;

    @SerializedName("product_id")
    private int productId;

    @SerializedName("name")
    private String name;

    @SerializedName("unit_price")
    private String unitPrice;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("line_total")
    private String lineTotal;

    public PaymentItem() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUnitPrice() { return unitPrice; }
    public void setUnitPrice(String unitPrice) { this.unitPrice = unitPrice; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getLineTotal() { return lineTotal; }
    public void setLineTotal(String lineTotal) { this.lineTotal = lineTotal; }

    @Override
    public String toString() {
        return "PaymentItem{" +
                "id=" + id +
                ", productId=" + productId +
                ", name='" + name + '\'' +
                ", unitPrice='" + unitPrice + '\'' +
                ", quantity=" + quantity +
                ", lineTotal='" + lineTotal + '\'' +
                '}';
    }
}

