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
        ImageView iv = new ImageView(headSetUrl.toString());
        iv.setFitWidth(64);
        iv.setFitHeight(64);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        helpButton.setGraphic(iv);

    }

    public String getSelectedPayment() {
        Toggle selectedToggle = paymentGroup.getSelectedToggle();
        if (selectedToggle != null) {
            return ((RadioButton) selectedToggle).getId();
        }
        return null;
    }

    @FXML
    void onContinueAction() throws IOException {
        System.out.println("Continue to Payment");

        String payment = getSelectedPayment();
        if (payment == null) {
            System.out.println("Chưa chọn phương thức!");
            return;
        }
        if (payment.equals("cashOption")) {
            SceneManager.switchTo("cash.fxml");
        }
        else {
//            SceneManager.switchTo("card-payment.fxml");
        }

    }

    @FXML
    void onBackAction() throws IOException {
        System.out.println("Back to Scan Product");
        SceneManager.switchTo("cart-overview.fxml");
    }

    @FXML
    void onHelpAction() throws IOException {

    }


}
