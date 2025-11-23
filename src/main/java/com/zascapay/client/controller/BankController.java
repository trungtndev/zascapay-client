package com.zascapay.client.controller;

import com.zascapay.client.component.data.Item;
import com.zascapay.client.service.OrderService;
import com.zascapay.client.service.PaymentContext;
import com.zascapay.client.service.PaymentService;
import com.zascapay.client.service.TemporaryCart;
import com.zascapay.client.service.dto.request.OrderItem;
import com.zascapay.client.service.dto.request.OrderRequest;
import com.zascapay.client.service.dto.request.PaymentRequest;
import com.zascapay.client.service.dto.response.OrderResponse;
import com.zascapay.client.service.dto.response.PaymentResponse;
import com.zascapay.client.util.SceneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(BankController.class.getName());

    @FXML
    private Button backButton;
    @FXML
    private Button helpButton;
    // bind the ImageView from fxml so we can set the QR image dynamically
    @FXML
    private ImageView qrImage;

    // optional labels to display bank details dynamically
    @FXML
    private Label bankNameLabel;
    @FXML
    private Label accountLabel;
    @FXML
    private Label ownerLabel;
    @FXML
    private Label contentLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        URL headSetUrl = getClass().getResource("/images/headset.png");
        if (headSetUrl != null && helpButton != null) {
            Image ivImg = new Image(headSetUrl.toString());
            ImageView iv = new ImageView(ivImg);
            iv.setFitWidth(64);
            iv.setFitHeight(64);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);
            helpButton.setGraphic(iv);
        }

        // load QR image resource into qrImage (if present)
        URL qrUrl = getClass().getResource("/images/qr.png");
        if (qrUrl != null && qrImage != null) {
            qrImage.setImage(new Image(qrUrl.toString()));
            qrImage.setFitWidth(192);
            qrImage.setFitHeight(192);
            qrImage.setPreserveRatio(true);
            qrImage.setSmooth(true);
        }

        // when user clicks on the QR image, we assume the customer has completed bank transfer
        // so we place order and mark payment method = "bank_transfer" (or "bank")
        if (qrImage != null) {
            qrImage.setOnMouseClicked(event -> {
                System.out.println("BankController: QR clicked -> place order with bank payment");
            });
        }

        // set example static labels for now (could be dynamic later)
        if (bankNameLabel != null) bankNameLabel.setText("ABC");
        if (accountLabel != null) accountLabel.setText("123456789");
        if (ownerLabel != null) ownerLabel.setText("Nguyễn Văn A");
        if (contentLabel != null) contentLabel.setText("[Mã đơn hàng]-[Tên KH]");
        placeOrderForBank();

    }

    private void placeOrderForBank() {
        // disable back while processing
        if (backButton != null) {
            backButton.setDisable(true);
        }

        // snapshot cart
        List<Item> itemsSnapshot = new ArrayList<>(TemporaryCart.getInstance().getItems());
        if (itemsSnapshot.isEmpty()) {
            LOGGER.warning("BankController: cart empty, nothing to order");
            if (backButton != null) backButton.setDisable(false);
            return;
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (Item it : itemsSnapshot) {
            if (it.getProductId() <= 0) {
                LOGGER.info("BankController: skipping item without productId: " + it.getName());
                continue;
            }
            orderItems.add(new OrderItem(it.getProductId(), it.getQuantity()));
        }

        if (orderItems.isEmpty()) {
            LOGGER.warning("BankController: no orderable items (missing productId)");
            if (backButton != null) backButton.setDisable(false);
            return;
        }

        OrderRequest req = new OrderRequest(orderItems);
        LOGGER.info("BankController: placing order (bank) -> " + req);

        new Thread(() -> {
            try {
                OrderService orderService = new OrderService();
                OrderResponse orderResp = orderService.placeOrder(req);
                if (orderResp != null) {
                    LOGGER.info("BankController: order placed successfully: " + orderResp);

                    // Build payment request with method "bank" (adapt to your backend: e.g., "bank_transfer")
                    Map<String, String> metadata = new HashMap<>();
                    metadata.put("note", "bank_transfer");
                    PaymentRequest payReq = new PaymentRequest(orderResp.getOrderId(), "bank_transfer", metadata);
                    LOGGER.info("BankController: sending payment request: " + payReq);

                    PaymentService paymentService = new PaymentService();
                    PaymentResponse paymentResp = paymentService.makePayment(payReq);

                    if (paymentResp != null) {
                        PaymentContext.setLastPayment(paymentResp);
                        LOGGER.info("BankController: payment response: " + paymentResp);

                        boolean isSuccess = paymentResp.isSuccess();
                        boolean isPending = paymentResp.isPending();

                        if (isSuccess || isPending) {
                            TemporaryCart.getInstance().checkout();

                            Platform.runLater(() -> {
                                try {
                                    // For bank transfer, you might want to show thank.fxml for pending
                                    if (isSuccess) {
                                        SceneManager.switchTo("success.fxml");
                                    } else {
                                        SceneManager.switchTo("thank.fxml");
                                    }
                                } catch (IOException e) {
                                    LOGGER.log(Level.SEVERE, "BankController: failed to switch scene after payment", e);
                                }
                            });
                        } else {
                            LOGGER.warning("BankController: payment failed/unknown status for order " + orderResp.getOrderId() + ": " + paymentResp.getStatus());
                            Platform.runLater(() -> {
                                if (backButton != null) backButton.setDisable(false);
                                Alert a = new Alert(Alert.AlertType.ERROR);
                                a.setTitle("Payment failed");
                                a.setHeaderText(null);
                                a.setContentText("Payment failed or returned unexpected status for order " + orderResp.getOrderId() + ". Please contact support.");
                                a.showAndWait();
                            });
                        }
                    } else {
                        LOGGER.warning("BankController: payment request returned null (HTTP error) for order " + orderResp.getOrderId());
                        Platform.runLater(() -> {
                            if (backButton != null) backButton.setDisable(false);
                            Alert a = new Alert(Alert.AlertType.ERROR);
                            a.setTitle("Payment failed");
                            a.setHeaderText(null);
                            a.setContentText("Failed to process bank payment for order " + orderResp.getOrderId() + ". Please contact support.");
                            a.showAndWait();
                        });
                    }
                } else {
                    LOGGER.warning("BankController: order failed");
                    Platform.runLater(() -> {
                        if (backButton != null) backButton.setDisable(false);
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setTitle("Order failed");
                        a.setHeaderText(null);
                        a.setContentText("Failed to place order. Please try again.");
                        a.showAndWait();
                    });
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "BankController: error while placing order/payment", e);
                Platform.runLater(() -> {
                    if (backButton != null) backButton.setDisable(false);
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Order error");
                    a.setHeaderText(null);
                    a.setContentText("An error occurred while placing order/payment: " + e.getMessage());
                    a.showAndWait();
                });
            }
        }).start();
    }

    @FXML
    void onBackAction() throws IOException {
        System.out.println("Back to Payment Method");
        SceneManager.switchTo("payment-method.fxml");
    }

    @FXML
    void onHelpAction() {
        System.out.println("Help clicked");
    }
}
