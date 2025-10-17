package com.zascapay.client.controller;

import com.zascapay.client.component.CartItemCell_L;
import com.zascapay.client.component.data.Item;
import com.zascapay.client.service.TemporaryCart;
import com.zascapay.client.util.SceneManager;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    @FXML
    private Label summaryItems;

    @FXML
    private Label totalLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        URL headSetUrl = getClass().getResource("/images/headset.png");
        ImageView iv = new ImageView(headSetUrl.toString());
        iv.setFitWidth(64);
        iv.setFitHeight(64);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        helpButton.setGraphic(iv);
        // Bind ListView to the shared temporary cart so items added during scanning remain here
        itemList.setCellFactory(listView -> new CartItemCell_L());
        itemList.setItems(TemporaryCart.getInstance().getItems());

        // Update summary labels when cart changes and toggle continue button state
        TemporaryCart.getInstance().getItems().addListener((ListChangeListener<Item>) change -> {
            updateCartSummary();
            continueButton.setDisable(TemporaryCart.getInstance().getItemCount() == 0);
            // force refresh to ensure cell controls are updated
            Platform.runLater(() -> itemList.refresh());
        });
        updateCartSummary();
        // set initial continue button state
        continueButton.setDisable(TemporaryCart.getInstance().getItemCount() == 0);
    }

    private void updateCartSummary() {
        Platform.runLater(() -> {
            int count = TemporaryCart.getInstance().getItemCount();
            double total = TemporaryCart.getInstance().getTotal();
            summaryItems.setText(count + " items");
            totalLabel.setText(String.format("%.2f", total));
        });
    }

    @FXML
    void onContinueAction() throws IOException {
        // Navigate to payment method selection. The order will be placed in the cash flow controller.
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
