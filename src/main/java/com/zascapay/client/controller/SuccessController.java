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

    }
}
