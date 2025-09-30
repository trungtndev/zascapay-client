package com.zascapay.client.controller;

import com.zascapay.client.component.CartItemCell;
import com.zascapay.client.component.data.Item;
import com.zascapay.client.service.ScanService;
import com.zascapay.client.service.dto.response.Detection;
import com.zascapay.client.service.dto.response.ScanResponse;
import com.zascapay.client.util.SceneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class ScanProductController implements Initializable {
    @FXML
    private Button helpButton;
    @FXML
    private Button scanButton;
    @FXML
    private Button reScanButton;

    @FXML
    public Canvas overlayCanvas;
    public Label totalLabel;
    public Label summaryItems;

    @FXML
    private Button continueButton;

    @FXML
    private ListView<Item> itemListShow;

    @FXML
    private Pane cameraPane;

    @FXML
    private ImageView cameraView;

    private ScanService scanService = new ScanService();


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

        URL reScanUrl = getClass().getResource("/images/re-scan.png");
        ImageView iv3 = new ImageView(reScanUrl.toString());
        iv3.setFitWidth(64);
        iv3.setFitHeight(64);
        iv3.setPreserveRatio(true);
        iv3.setSmooth(true);
        reScanButton.setGraphic(iv3);

        itemListShow.setCellFactory(listView -> new CartItemCell());


        cameraView.fitWidthProperty().bind(cameraPane.widthProperty());
        cameraView.fitHeightProperty().bind(cameraPane.heightProperty());

        overlayCanvas.widthProperty().bind(cameraPane.widthProperty());
        overlayCanvas.heightProperty().bind(cameraPane.heightProperty());
    }

    @FXML
    void onScanAction() throws IOException {
        Image testImage = new Image(getClass().getResource("/images/demo.jpg").toExternalForm());
        cameraView.setImage(testImage);
        scanButton.setDisable(true);

        File file = new File(getClass().getResource("/images/demo.jpg").getPath());

        new Thread(() -> {
            try {
//                ScanService service = new ScanService();
                ScanResponse res = scanService.scan(file);

                if (res != null) {
                    byte[] decodedBytes = Base64.getDecoder().decode(res.getImage());
                    InputStream is = new ByteArrayInputStream(decodedBytes);
                    Image annotatedImage = new Image(is);

                    Platform.runLater(() -> {
                        cameraView.setImage(annotatedImage);
                        for (Detection det : res.getObjects()) {
                            itemListShow.getItems().add(
                                new Item(
                                        det.getClassName(),
                                        String.valueOf(det.getPrice()),
                                        getClass().getResource("/images/empty.png").toString())
                            );
                        }
                        scanButton.setDisable(false); // bật lại khi xong
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> scanButton.setDisable(false));
            }
        }).start();

    }

    @FXML
    void onReScanAction() throws IOException {
        cameraView.setImage(null);
    }

    @FXML
    void onHelpAction() throws IOException {
    }

    @FXML
    void onContinueAction() throws IOException {
        SceneManager.switchTo("cart-overview.fxml");
    }


}
