package com.zascapay.client.component.data;

public class Item {
    private final int productId;
    private String name;
    private String price;
    private String imageUrl;
    private int quantity;

    // Old constructor kept for compatibility (productId=0)
    public Item(String name, String price, String imageUrl) {
        this(0, name, price, imageUrl, 1);
    }

    public Item(int productId, String name, String price, String imageUrl, int quantity) {
        this.productId = productId;
        this.name = name != null ? name.trim() : null;
        this.price = price != null ? price.trim() : null;
        this.imageUrl = imageUrl != null ? imageUrl.trim() : null;
        this.quantity = Math.max(1, quantity);
    }

    public int getProductId() { return productId; }
    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = Math.max(1, quantity); }
    public void incrementQuantity(int delta) { this.quantity = Math.max(1, this.quantity + delta); }

    // equals by productId if non-zero, otherwise by normalized name
    public boolean matches(Item other) {
        if (other == null) return false;
        if (this.productId != 0 && other.productId != 0) {
            return this.productId == other.productId;
        }
        if (this.name == null || other.name == null) return false;
        return this.name.trim().equalsIgnoreCase(other.name.trim());
    }
}
