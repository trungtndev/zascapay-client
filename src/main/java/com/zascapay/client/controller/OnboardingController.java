package com.zascapay.client.controller;

import com.zascapay.client.util.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OnboardingController implements Initializable {
    @FXML
    private Button startButton;

    @FXML
    private ImageView logoView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        URL imageUrl = getClass().getResource("/images/logo.png");

        Image logo = new Image(imageUrl.toString());
        logoView.setImage(logo);
    }
    @FXML
    private void handleStartButton() throws IOException {
        SceneManager.switchTo("scan-product.fxml");
    }


}
