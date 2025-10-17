package com.zascapay.client.controller;

import com.zascapay.client.util.SceneManager;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BankController implements Initializable {

    @FXML
    private Button backButton;
    @FXML
    private Button helpButton;
    // bind the ImageView from fxml so we can set the QR image dynamically
    @FXML
    private ImageView qrImage;

    // optional labels to display bank details dynamically
    @FXML
    private Label bankNameLabel;
    @FXML
    private Label accountLabel;
    @FXML
    private Label ownerLabel;
    @FXML
    private Label contentLabel;
//    private ImageView qrImageView; // new: bind to the ImageView in cash.fxml (fx:id="qrImageView")

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        URL headSetUrl = getClass().getResource("/images/headset.png");
        if (headSetUrl != null && helpButton != null) {
            Image ivImg = new Image(headSetUrl.toString());
            ImageView iv = new ImageView(ivImg);
            iv.setFitWidth(64);
            iv.setFitHeight(64);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);
            helpButton.setGraphic(iv);
        }

        // load QR image resource into qrImage (if present)
        URL qrUrl = getClass().getResource("/images/qr.png");
        System.out.println("qrUrl: " + qrUrl);
        qrImage.setImage(new Image(qrUrl.toString()));
        qrImage.setFitWidth(192);
        qrImage.setFitHeight(192);
        qrImage.setPreserveRatio(true);
        qrImage.setSmooth(true);

        // set example bank info; you can replace these with dynamic values later
        if (bankNameLabel != null) bankNameLabel.setText("ABC");
        if (accountLabel != null) accountLabel.setText("123456789");
        if (ownerLabel != null) ownerLabel.setText("Nguyễn Văn A");
        if (contentLabel != null) contentLabel.setText("[Mã đơn hàng]-[Tên KH]");

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            try {
                System.out.println("Auto switch to next frame after 3s");
                SceneManager.switchTo("success.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        delay.play();
    }

    @FXML
    void onBackAction() throws IOException {
        System.out.println("Back to Scan Product");
        SceneManager.switchTo("payment-method.fxml");

    }

    @FXML
    void onHelpAction() {
        System.out.println("Help clicked");
    }

}
