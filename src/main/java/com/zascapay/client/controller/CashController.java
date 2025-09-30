package com.zascapay.client.controller;

import com.zascapay.client.util.SceneManager;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CashController implements Initializable {

    @FXML
    private Button backButton;
    @FXML
    private Button helpButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        URL headSetUrl = getClass().getResource("/images/headset.png");
        ImageView iv = new ImageView(headSetUrl.toString());
        iv.setFitWidth(64);
        iv.setFitHeight(64);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        helpButton.setGraphic(iv);


        PauseTransition delay = new PauseTransition(Duration.seconds(10));
        delay.setOnFinished(event -> {
            try {
                System.out.println("Auto switch to next frame after 10s");
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
