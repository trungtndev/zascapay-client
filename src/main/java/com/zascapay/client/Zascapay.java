package com.zascapay.client;

import com.zascapay.client.util.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Zascapay extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        SceneManager.setStage(stage);
        SceneManager.setDimensions(1280, 768);
        SceneManager.switchTo("scan-product.fxml");

    }
}
