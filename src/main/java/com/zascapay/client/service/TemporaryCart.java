package com.zascapay.client.service;

import com.zascapay.client.component.data.Item;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Temporary in-memory cart that lives for the application lifetime and is cleared on checkout.
 * Safe to call from any thread; UI changes are marshalled to the JavaFX application thread.
 */
public final class TemporaryCart {

    private static final TemporaryCart INSTANCE = new TemporaryCart();

    private final ObservableList<Item> items = FXCollections.observableArrayList();

    private TemporaryCart() {}

    public static TemporaryCart getInstance() {
        return INSTANCE;
    }

    /**
     * Returns the live ObservableList that can be bound to UI controls (ListView, TableView).
     */
    public ObservableList<Item> getItems() {
        return items;
    }

    /**
     * Adds an item to the cart. If an item with the same productId/name exists, increase quantity.
     * If called off the JavaFX thread the operation will be scheduled on the FX thread.
     */
    public void addItem(Item newItem) {
        if (Platform.isFxApplicationThread()) {
            mergeOrAdd(newItem);
        } else {
            Platform.runLater(() -> mergeOrAdd(newItem));
        }
    }

    private void mergeOrAdd(Item newItem) {
        for (int i = 0; i < items.size(); i++) {
            Item it = items.get(i);
            if (it.matches(newItem)) {
                // compute new quantity
                int updatedQty = it.getQuantity() + newItem.getQuantity();

                // prefer latest scanned unit price and image when available
                String updatedPrice = (newItem.getPrice() != null && !newItem.getPrice().trim().isEmpty()) ? newItem.getPrice() : it.getPrice();
                String updatedImage = (newItem.getImageUrl() != null && !newItem.getImageUrl().trim().isEmpty()) ? newItem.getImageUrl() : it.getImageUrl();

                // log what's changing for easier debugging
                System.out.println("TemporaryCart.mergeOrAdd(): merging productId=" + it.getProductId()
                        + " name='" + it.getName() + "' oldPrice='" + it.getPrice() + "' newPrice='" + (newItem.getPrice()) + "' oldQty=" + it.getQuantity() + " addQty=" + newItem.getQuantity() + " -> newQty=" + updatedQty);

                // create updated item and replace the existing one so ListView refreshes
                Item updated = new Item(it.getProductId(), it.getName(), updatedPrice, updatedImage, updatedQty);
                items.set(i, updated);
                return;
            }
        }
        // no match -> add new
        System.out.println("TemporaryCart.mergeOrAdd(): adding new productId=" + newItem.getProductId() + " name='" + newItem.getName() + "' price='" + newItem.getPrice() + "' qty=" + newItem.getQuantity());
        items.add(newItem);
    }

    /**
     * Removes an item from the cart.
     */
    public void removeItem(Item item) {
        if (Platform.isFxApplicationThread()) {
            items.remove(item);
        } else {
            Platform.runLater(() -> items.remove(item));
        }
    }

    /**
     * Clears the cart contents.
     */
    public void clear() {
        if (Platform.isFxApplicationThread()) {
            items.clear();
        } else {
            Platform.runLater(items::clear);
        }
    }

    /**
     * Returns a snapshot of the current items and clears the cart. Use this for checkout.
     * This method blocks the calling thread until the snapshot is taken if called off the FX thread.
     */
    public List<Item> checkout() {
        if (Platform.isFxApplicationThread()) {
            List<Item> snapshot = new ArrayList<>(items);
            items.clear();
            return snapshot;
        } else {
            final AtomicReference<List<Item>> result = new AtomicReference<>();
            final Object lock = new Object();
            Platform.runLater(() -> {
                synchronized (lock) {
                    result.set(new ArrayList<>(items));
                    items.clear();
                    lock.notify();
                }
            });
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return Collections.emptyList();
                }
            }
            return result.get();
        }
    }

    /**
     * Returns the total price of items in the cart. Accepts price stored as String and tries multiple
     * parsing strategies to support different formats (null, empty, commas as thousands separators, etc.).
     * Now multiplies parsed price by item quantity.
     */
    public double getTotal() {
        double sum = 0.0;
        for (Item it : items) {
            String raw = it.getPrice();
            if (raw == null) continue;
            String s = raw.trim();
            if (s.isEmpty()) continue;

            // try multiple parsing strategies
            Double parsed = tryParseDouble(s);
            if (parsed != null) {
                sum += parsed * it.getQuantity();
            }
        }
        return sum;
    }

    private Double tryParseDouble(String s) {
        // 1) direct parse
        try {
            return Double.parseDouble(s);
        } catch (Exception ignored) {}

        // 2) remove common thousands separator commas (e.g., 1,234 -> 1234)
        try {
            String removedCommas = s.replaceAll(",", "");
            return Double.parseDouble(removedCommas);
        } catch (Exception ignored) {}

        // 3) replace comma with dot (e.g., 12,34 -> 12.34)
        try {
            String commaToDot = s.replace(',', '.');
            return Double.parseDouble(commaToDot);
        } catch (Exception ignored) {}

        // cannot parse
        return null;
    }

    public int getItemCount() {
        int total = 0;
        for (Item it : items) total += it.getQuantity();
        return total;
    }
}
