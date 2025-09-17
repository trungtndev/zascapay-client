package com.zascapay.client.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class SceneManager {
    private static Stage stage;
    private static double width;
    private static double height;

    public static void setStage(Stage s) {
        stage = s;
//        stage.setFullScreen(true);

    }

    public static void setDimensions(double w, double h) {
        width = w;
        height = h;
    }

    public static void switchTo(String fxml) throws IOException {
        URL pathFxml = SceneManager.class.getResource("/com/zascapay/client/" + fxml);
        FXMLLoader fxmlLoader = new FXMLLoader(pathFxml);
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        stage.setScene(scene);
//        stage.setFullScreen(true);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        URL pathFxml = SceneManager.class.getResource("/com/zascapay/client/" + fxml);

        FXMLLoader loader = new FXMLLoader(pathFxml);
        Parent root = loader.load();
        stage.getScene().setRoot(root);
    }
}
