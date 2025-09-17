package com.zascapay.client.controller;

import com.zascapay.client.util.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ScanProductController implements Initializable {
    @FXML
    public Canvas overlayCanvas;
    @FXML
    private Button helpButton;
    @FXML
    private Button scanButton;
    @FXML
    private Button reScanButton;

    @FXML
    private Button continueButton;

//    @FXML
//    private ListView<Item> itemList;

    @FXML
    private Pane cameraPane;

    @FXML
    private ImageView cameraView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        URL headSetUrl = getClass().getResource("/images/headset.png");
        ImageView iv = new ImageView(headSetUrl.toString());
        iv.setFitWidth(64);
        iv.setFitHeight(64);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        helpButton.setGraphic(iv);

        URL scanUrl = getClass().getResource("/images/scan.png");
        ImageView iv2 = new ImageView(scanUrl.toString());
        iv2.setFitWidth(64);
        iv2.setFitHeight(64);
        iv2.setPreserveRatio(true);
        iv2.setSmooth(true);
        scanButton.setGraphic(iv2);
//
        URL reScanUrl = getClass().getResource("/images/re-scan.png");
        ImageView iv3 = new ImageView(reScanUrl.toString());
        iv3.setFitWidth(64);
        iv3.setFitHeight(64);
        iv3.setPreserveRatio(true);
        iv3.setSmooth(true);
        reScanButton.setGraphic(iv3);

    }

    @FXML
    void onHelpAction() throws IOException {
        System.out.println("Help button clicked in ScanProductController");

    }

    @FXML
    void onScanAction() throws IOException {
        Image testImage = new Image(getClass().getResource("/images/headset.png").toExternalForm());
        cameraView.setImage(testImage);
        System.out.println("Scan button clicked in ScanProductController");
    }

    @FXML
    void onReScanAction() throws IOException {
        cameraView.setImage(null);
        System.out.println("Re-Scan button clicked in ScanProductController");
    }

    @FXML
    void onContinueAction() throws IOException {
        System.out.println("Continue button clicked in ScanProductController");
        SceneManager.switchTo("cart-overview.fxml");
    }


}
