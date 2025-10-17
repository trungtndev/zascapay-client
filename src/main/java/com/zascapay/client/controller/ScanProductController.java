package com.zascapay.client.controller;

import com.zascapay.client.component.CartItemCell;
import com.zascapay.client.component.data.Item;
import com.zascapay.client.service.ScanService;
import com.zascapay.client.service.TemporaryCart;
import com.zascapay.client.service.dto.response.ProductResponse;
import com.zascapay.client.service.dto.response.ScanResponse;
import com.zascapay.client.util.SceneManager;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
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

        // Bind the ListView to the application-scoped temporary cart so items persist across scenes
        itemListShow.setCellFactory(listView -> new CartItemCell());
        itemListShow.setItems(TemporaryCart.getInstance().getItems());

        // Update summary labels when cart changes
        TemporaryCart.getInstance().getItems().addListener((ListChangeListener<Item>) change -> updateCartSummary());
        updateCartSummary();

        cameraView.fitWidthProperty().bind(cameraPane.widthProperty());
        cameraView.fitHeightProperty().bind(cameraPane.heightProperty());

        overlayCanvas.widthProperty().bind(cameraPane.widthProperty());
        overlayCanvas.heightProperty().bind(cameraPane.heightProperty());
    }

    private void updateCartSummary() {
        // Ensure UI update runs on FX thread
        Platform.runLater(() -> {
            int count = TemporaryCart.getInstance().getItemCount();
            double total = TemporaryCart.getInstance().getTotal();
//            summaryItems.setText(count + " items");
            // Format total with 2 decimals; adapt if you use a different display format
            totalLabel.setText(String.format("%.2f", total));
        });
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
                        for (ProductResponse det : res.getProducts()) {
                            // Add to the application-scoped TemporaryCart so items remain until checkout
                            String productName = det.getProductName();
                            if (productName == null) productName = "(no name)";
                            String priceStr = det.getPrice(); // keep null if API returned null
                            System.out.println("ScanProductController: adding item -> name='" + productName + "', price='" + priceStr + "'");
                            TemporaryCart.getInstance().addItem(
                                new Item(
                                        det.getProductId(),
                                        productName,
                                        priceStr,
                                        getClass().getResource("/images/empty.png").toString(),
                                        1
                                )
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
