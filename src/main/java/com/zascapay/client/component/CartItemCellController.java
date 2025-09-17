package com.zascapay.client.component;

import com.zascapay.client.component.data.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CartItemCellController {
    @FXML
    private ImageView itemImage;
    @FXML
    private Label itemName;
    @FXML
    private Label itemPrice;

    public void setData(Item item) {
        itemName.setText(item.getName());
        itemPrice.setText(item.getPrice());
        if (item.getImageUrl() != null) {
            itemImage.setImage(new Image(item.getImageUrl()));
        }
    }
}
