package com.zascapay.client.component;

import com.zascapay.client.component.data.Item;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class CartItemCell_L extends ListCell<Item> {
    private FXMLLoader loader;
    private CartItemCellController controller;

    @Override
    protected void updateItem(Item item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("/com/zascapay/client/component/CartItemCell-L.fxml"));
                try {
                    HBox box = loader.load();
                    controller = loader.getController();
                    setGraphic(box);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
                controller.setData(item);
        }
    }
}
