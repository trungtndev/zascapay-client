package com.zascapay.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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

    }

    @FXML
    public void onContinueAction(ActionEvent actionEvent) {
    }
    @FXML
    public void onBackAction(ActionEvent actionEvent) {
    }
}
