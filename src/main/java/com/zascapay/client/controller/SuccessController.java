package com.zascapay.client.controller;

import com.zascapay.client.util.SceneManager;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SuccessController implements Initializable {

    @FXML
    private ImageView successImageView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        URL resource = this.getClass().getResource("/images/success.png");
        Image image = new Image(resource.toString());
        successImageView.setImage(image);
        successImageView.setFitHeight(256);
        successImageView.setFitWidth(256);


        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(event -> {
            try {
                System.out.println("Auto switch to next frame after 10s");
                SceneManager.switchTo("invoice-option.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        delay.play();
    }
}
