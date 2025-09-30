package com.zascapay.client.controller;

import com.zascapay.client.util.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InvoiceDisplayController implements Initializable {
    @FXML
    private Button helpButton;

    @FXML
    private Button nextButton;

    @FXML
    private Button backButton;

    @FXML
    private TextField emailField;


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

    @FXML
    public void onContinueAction(ActionEvent actionEvent) throws IOException {
        SceneManager.switchTo("thank.fxml");

    }

    @FXML
    public void onBackAction(ActionEvent actionEvent) throws IOException {
        SceneManager.switchTo("invoice-option.fxml");
    }

    public void onHelpAction(ActionEvent actionEvent) {
        System.out.println("Help clicked");
    }
}
