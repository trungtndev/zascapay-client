package com.zascapay.client.controller;

import com.zascapay.client.component.CartItemCell;
import com.zascapay.client.component.CartItemCell_L;
import com.zascapay.client.component.data.Item;
import com.zascapay.client.util.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CartOverviewController implements Initializable {
    @FXML
    private Button helpButton;
    @FXML
    private Button continueButton;

    @FXML
    private Button backButton;

    @FXML
    private ListView<Item> itemList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        URL headSetUrl = getClass().getResource("/images/headset.png");
        ImageView iv = new ImageView(headSetUrl.toString());
        iv.setFitWidth(64);
        iv.setFitHeight(64);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        helpButton.setGraphic(iv);
        // TODO: Load items from cart
        itemList.setCellFactory(listView -> new CartItemCell_L());
        itemList.getItems().addAll(
                new Item("Item 1dassssssssssssssssssssssssssssssssssss", "35.000", getClass().getResource("/images/empty.png").toString()),
                new Item("Item 11dassssssssssssssssssssssssssssssssssss", "35.000", getClass().getResource("/images/empty.png").toString()),
                new Item("Item 11dassssssssssssssssssssssssssssssssssss", "35.000", getClass().getResource("/images/empty.png").toString()),
                new Item("Item 11dassssssssssssssssssssssssssssssssssss", "35.000", getClass().getResource("/images/empty.png").toString()),
                new Item("Item 11dassssssssssssssssssssssssssssssssssss", "35.000", getClass().getResource("/images/empty.png").toString()),
                new Item("Item 11dassssssssssssssssssssssssssssssssssss", "35.000", getClass().getResource("/images/empty.png").toString()),
                new Item("Item 1", "35.000", getClass().getResource("/images/empty.png").toString()),
                new Item("Item 1", "35.000", getClass().getResource("/images/empty.png").toString()),
                new Item("Item 2", "45.000", getClass().getResource("/images/empty.png").toString())
        );
    }

    @FXML
    void onContinueAction() throws IOException {
        System.out.println("Continue to Payment");
        SceneManager.switchTo("payment-method.fxml");
    }

    @FXML
    void onBackAction() throws IOException {
        System.out.println("Back to Scan Product");
        SceneManager.switchTo("scan-product.fxml");

    }

    @FXML
    void onHelpAction() {
        System.out.println("Help clicked");
    }


}
