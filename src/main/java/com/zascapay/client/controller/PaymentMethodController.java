package com.zascapay.client.controller;

import com.zascapay.client.util.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PaymentMethodController implements Initializable {
    public Button helpButton;
    @FXML
    private Button continueButton;

    @FXML
    private Button backButton;

    @FXML
    private ToggleGroup paymentGroup;

    @FXML
    private RadioButton cashOption;
    @FXML
    private RadioButton bankOption;
    @FXML
    private RadioButton walletOption;
    @FXML
    private RadioButton cardOption;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        URL headSetUrl = getClass().getResource("/images/headset.png");
        if (headSetUrl != null && helpButton != null) {
            ImageView iv = new ImageView(headSetUrl.toString());
            iv.setFitWidth(64);
            iv.setFitHeight(64);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);
            helpButton.setGraphic(iv);
        }

    }

    public String getSelectedPayment() {
        Toggle selectedToggle = paymentGroup.getSelectedToggle();
        if (selectedToggle != null) {
            // try to return the Toggle's id (set in FXML as id="..."), fallback to fx:id if id is null
            RadioButton rb = (RadioButton) selectedToggle;
            String id = rb.getId();
            if (id != null && !id.isEmpty()) return id;
            if (rb.getUserData() != null) return rb.getUserData().toString();
            return rb.getText();
        }
        return null;
    }

    @FXML
    void onContinueAction() {
        System.out.println("Continue to Payment");

        String payment = getSelectedPayment();
        if (payment == null) {
            System.out.println("Chưa chọn phương thức!");
            return;
        }
        try {
            if (payment.equals("cashOption") || "Tiền mặt".equals(payment)) {
                SceneManager.switchTo("cash.fxml");
                return;
            }
            if (payment.equals("bankOption") || "Chuyển khoản".equals(payment) || "Ngân hàng".equals(payment)) {
                // switch to Bank screen (QR renamed to bank.fxml)
                SceneManager.switchTo("bank.fxml");
                return;
            }
            // handle other options (card, wallet)
            if (payment.equals("cardOption") || "Thẻ".equals(payment)) {
                // implement card flow later
                System.out.println("Card payment selected - not implemented yet");
                return;
            }
            if (payment.equals("walletOption") || "Ví điện tử".equals(payment)) {
                System.out.println("Wallet payment selected - not implemented yet");
                return;
            }
        } catch (IOException e) {
            // log and keep app running
            System.err.println("Failed to switch scene: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @FXML
    void onBackAction() {
        System.out.println("Back to Scan Product");
        try {
            SceneManager.switchTo("cart-overview.fxml");
        } catch (IOException e) {
            System.err.println("Failed to switch to cart-overview.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void onHelpAction() {

    }


}
