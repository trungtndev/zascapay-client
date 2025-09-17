package com.zascapay.client.service;

import com.zascapay.client.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CartService {
    private final ObservableList<Product> items = FXCollections.observableArrayList();

    public ObservableList<Product> getItems() {
        return items;
    }

    public void add(Product p) {
        items.add(p);
    }

    public void remove(Product p) {
        items.remove(p);
    }

    public void clear() {
        items.clear();
    }

    public double getTotal() {
        return items.stream().mapToDouble(Product::getPrice).sum();
    }

    public int getCount() {
        return items.size();
    }
}
