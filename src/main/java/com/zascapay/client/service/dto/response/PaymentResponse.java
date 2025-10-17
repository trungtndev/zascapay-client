package com.zascapay.client.service.dto.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class PaymentResponse {
    @SerializedName("id")
    private int id;

    @SerializedName("order_id")
    private int orderId;

    @SerializedName("user_id")
    private Integer userId; // nullable

    @SerializedName("amount")
    private String amount;

    @SerializedName("currency")
    private String currency;

    @SerializedName("method")
    private String method;

    @SerializedName("status")
    private String status;

    @SerializedName("provider_transaction_id")
    private String providerTransactionId;

    @SerializedName("processed_at")
    private String processedAt;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("metadata")
    private Map<String, Object> metadata;

    @SerializedName("items")
    private List<PaymentItem> items;

    public PaymentResponse() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getProviderTransactionId() { return providerTransactionId; }
    public void setProviderTransactionId(String providerTransactionId) { this.providerTransactionId = providerTransactionId; }

    public String getProcessedAt() { return processedAt; }
    public void setProcessedAt(String processedAt) { this.processedAt = processedAt; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    public List<PaymentItem> getItems() { return items; }
    public void setItems(List<PaymentItem> items) { this.items = items; }

    // Helper: normalized status string (lowercase, trimmed)
    public String getNormalizedStatus() {
        return status == null ? "" : status.trim().toLowerCase();
    }

    // Helper: common success synonyms
    public boolean isSuccess() {
        String s = getNormalizedStatus();
        return "succeeded".equals(s) || "success".equals(s) || "completed".equals(s);
    }

    // Helper: common pending/processing synonyms
    public boolean isPending() {
        String s = getNormalizedStatus();
        return "pending".equals(s) || "processing".equals(s) || "in_progress".equals(s);
    }

    @Override
    public String toString() {
        return "PaymentResponse{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", userId=" + userId +
                ", amount='" + amount + '\'' +
                ", currency='" + currency + '\'' +
                ", method='" + method + '\'' +
                ", status='" + status + '\'' +
                ", providerTransactionId='" + providerTransactionId + '\'' +
                ", processedAt='" + processedAt + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", metadata=" + metadata +
                ", items=" + items +
                '}';
    }
}
