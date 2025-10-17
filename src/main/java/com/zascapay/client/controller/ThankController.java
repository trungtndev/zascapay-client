package com.zascapay.client.controller;

import com.zascapay.client.service.PaymentContext;
import com.zascapay.client.service.TemporaryCart;
import com.zascapay.client.util.SceneManager;
import javafx.animation.PauseTransition;
import javafx.fxml.Initializable;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThankController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ThankController.class.getName());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Clear ephemeral application state so the next user/session starts fresh
        try {
            TemporaryCart.getInstance().clear();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to clear TemporaryCart", e);
        }

        try {
            PaymentContext.setLastPayment(null);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to clear PaymentContext", e);
        }

        // Keep the thank screen visible briefly, then go to onboarding
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(evt -> {
            try {
                SceneManager.switchTo("onboarding.fxml");
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to switch to onboarding scene", e);
            }
        });
        pause.play();
    }
}
