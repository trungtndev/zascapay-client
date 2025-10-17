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

public class CashController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(CashController.class.getName());

    @FXML
    private Button backButton;
    @FXML
    private Button helpButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        URL headSetUrl = getClass().getResource("/images/headset.png");
        if (headSetUrl != null) {
            ImageView iv = new ImageView(headSetUrl.toString());
            iv.setFitWidth(64);
            iv.setFitHeight(64);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);
            helpButton.setGraphic(iv);
        }

        // Begin placing order for cash payment when this screen is shown
        placeOrderForCash();
    }

    private void placeOrderForCash() {
        // disable back while processing
        backButton.setDisable(true);

        // take a snapshot of cart items
        List<Item> itemsSnapshot = new ArrayList<>(TemporaryCart.getInstance().getItems());
        if (itemsSnapshot.isEmpty()) {
            System.out.println("CashController: cart empty, nothing to order");
            backButton.setDisable(false);
            return;
        }

        // build order items
        List<OrderItem> orderItems = new ArrayList<>();
        for (Item it : itemsSnapshot) {
            if (it.getProductId() <= 0) {
                System.out.println("CashController: skipping item without productId: " + it.getName());
                continue;
            }
            orderItems.add(new OrderItem(it.getProductId(), it.getQuantity()));
        }

        if (orderItems.isEmpty()) {
            System.out.println("CashController: no orderable items (missing productId)");
            backButton.setDisable(false);
            return;
        }

        OrderRequest req = new OrderRequest(orderItems);
        System.out.println("CashController: placing order -> " + req);

        // call service on background thread
        new Thread(() -> {
            try {
                OrderService svc = new OrderService();
                // place order and get OrderResponse (contains order_id)
                OrderResponse orderResp = svc.placeOrder(req);
                if (orderResp != null) {
                    LOGGER.info("CashController: order placed successfully: " + orderResp);

                    // build payment request: method="cash", metadata {"note":"staff_capture"}
                    Map<String, String> metadata = new HashMap<>();
                    metadata.put("note", "staff_capture");
                    System.out.println("CashController: payment request: " + orderResp.getOrderId());
                    PaymentRequest payReq = new PaymentRequest(orderResp.getOrderId(), "cash", metadata);

                    PaymentService paymentService = new PaymentService();
                    // PaymentService.makePayment now returns a PaymentResponse or null on failure
                    PaymentResponse paymentResp = paymentService.makePayment(payReq);

                    if (paymentResp != null) {
                        // store payment in context for later screens (invoice display)
                        PaymentContext.setLastPayment(paymentResp);
                        String status = paymentResp.getStatus();
                        LOGGER.info("CashController: payment response: " + paymentResp);

                        boolean isSuccess = paymentResp.isSuccess();
                        boolean isPending = paymentResp.isPending();

                        if (isSuccess || isPending) {
                            TemporaryCart.getInstance().checkout();

                            Platform.runLater(() -> {
                                try {
                                    if (isSuccess) {
                                        SceneManager.switchTo("success.fxml");
                                    } else {
                                        SceneManager.switchTo("thank.fxml");
                                    }
                                } catch (IOException e) {
                                    LOGGER.log(Level.SEVERE, "Failed to switch scene after payment", e);
                                }
                            });
                        } else {
                            LOGGER.warning("CashController: payment failed/unknown status for order " + orderResp.getOrderId() + ": " + status);
                            Platform.runLater(() -> {
                                backButton.setDisable(false);
                                Alert a = new Alert(Alert.AlertType.ERROR);
                                a.setTitle("Payment failed");
                                a.setHeaderText(null);
                                a.setContentText("Payment failed or returned unexpected status for order " + orderResp.getOrderId() + ". Please contact support.");
                                a.showAndWait();
                            });
                        }
                    } else {
                        LOGGER.warning("CashController: payment request returned null (HTTP error) for order " + orderResp.getOrderId());
                        Platform.runLater(() -> {
                            backButton.setDisable(false);
                            Alert a = new Alert(Alert.AlertType.ERROR);
                            a.setTitle("Payment failed");
                            a.setHeaderText(null);
                            a.setContentText("Failed to process payment for order " + orderResp.getOrderId() + ". Please contact support.");
                            a.showAndWait();
                        });
                    }
                } else {
                    LOGGER.warning("CashController: order failed");
                    Platform.runLater(() -> {
                        backButton.setDisable(false);
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setTitle("Order failed");
                        a.setHeaderText(null);
                        a.setContentText("Failed to place order. Please try again.");
                        a.showAndWait();
                    });
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "An error occurred while placing order", e);
                Platform.runLater(() -> {
                    backButton.setDisable(false);
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Order error");
                    a.setHeaderText(null);
                    a.setContentText("An error occurred while placing order: " + e.getMessage());
                    a.showAndWait();
                });
            }
        }).start();
    }

    @FXML
    void onBackAction() throws IOException {
        System.out.println("Back to Scan Product");
        SceneManager.switchTo("payment-method.fxml");

    }

    @FXML
    void onHelpAction() {
        System.out.println("Help clicked");
    }


}
