package com.zascapay.client.component.data;

public class Item {
    private final String name;
    private final String price;
    private final String imageUrl;

    public Item(String name, String price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
}
