package com.zascapay.client.component;

import com.zascapay.client.component.data.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class CartItemCellController {
    @FXML
    private ImageView itemImage;
    @FXML
    private Label itemName;
    @FXML
    private Label itemPrice;
    @FXML
    private Label itemQuantity;

    public void setData(Item item) {
        String name = item.getName();
        if (name == null || name.trim().isEmpty()) {
            name = "(no name)";
        }

        // allow the name label to grow and wrap so it's visible
        itemName.setWrapText(true);
        HBox.setHgrow(itemName, Priority.ALWAYS);
        itemName.setMaxWidth(Double.MAX_VALUE);

        itemName.setText(name);
        itemQuantity.setText(String.valueOf(item.getQuantity()));

        // compute line total = unitPrice * quantity and format for display
        double unit = tryParseDouble(item.getPrice());
        double lineTotal = unit * item.getQuantity();
        itemPrice.setText(String.format("%.2f", lineTotal));

        // debug print to confirm data received and computed values
        System.out.println("CartItemCell.setData() name='" + name + "', qty=" + item.getQuantity() + ", unit='" + item.getPrice() + "', lineTotal='" + String.format("%.2f", lineTotal) + "'");

        if (item.getImageUrl() != null) {
            try {
                itemImage.setImage(new Image(item.getImageUrl()));
            } catch (Exception ignored) {
                itemImage.setImage(null);
            }
        } else {
            itemImage.setImage(null);
        }
    }

    private double tryParseDouble(String s) {
        if (s == null) return 0.0;
        String str = s.trim();
        if (str.isEmpty()) return 0.0;
        // 1) direct
        try {
            return Double.parseDouble(str);
        } catch (Exception ignored) {}
        // 2) remove commas
        try {
            String removed = str.replaceAll(",", "");
            return Double.parseDouble(removed);
        } catch (Exception ignored) {}
        // 3) comma -> dot
        try {
            String dot = str.replace(',', '.');
            return Double.parseDouble(dot);
        } catch (Exception ignored) {}
        return 0.0;
    }
}
