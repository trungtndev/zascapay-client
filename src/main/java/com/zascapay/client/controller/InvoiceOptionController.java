package com.zascapay.client.controller;

import com.zascapay.client.component.data.Item;
import com.zascapay.client.service.PaymentContext;
import com.zascapay.client.service.TemporaryCart;
import com.zascapay.client.service.dto.response.PaymentItem;
import com.zascapay.client.service.dto.response.PaymentResponse;
import com.zascapay.client.util.SceneManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InvoiceOptionController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(InvoiceOptionController.class.getName());

    @FXML
    private ToggleGroup invoiceGroup;
    @FXML
    private Button helpButton;

    // invoice UI injections
    @FXML
    private VBox itemsBox;
    @FXML
    private Label totalLabel;
    @FXML
    private Label paymentMethodLabel;
    @FXML
    private Label orderIdLabel;
    @FXML
    private Label dateLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        URL headSetUrl = getClass().getResource("/images/headset.png");
        ImageView iv = new ImageView(headSetUrl.toString());
        iv.setFitWidth(64);
        iv.setFitHeight(64);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        helpButton.setGraphic(iv);

        // populate invoice area: prefer PaymentContext, fallback to TemporaryCart
        try {
            PaymentResponse p = PaymentContext.getLastPayment();
            if (p != null) {
                populateFromPayment(p);
            } else {
                populateFromCart();
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to populate invoice option view", e);
        }
    }

    private void populateFromPayment(PaymentResponse p) {
        Platform.runLater(() -> {
            itemsBox.getChildren().clear();
            BigDecimal total = BigDecimal.ZERO;
            List<PaymentItem> items = p.getItems();
            if (items != null) {
                for (PaymentItem it : items) {
                    HBox row = new HBox();
                    row.setSpacing(8);

                    Label name = new Label(it.getName() != null ? it.getName() : "");
                    name.getStyleClass().add("invoice-item");

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    Label qty = new Label(String.valueOf(it.getQuantity()));
                    qty.getStyleClass().add("invoice-qty");

                    // Prefer using provided line_total from API (string), otherwise compute from unit_price if available
                    BigDecimal line = BigDecimal.ZERO;
                    if (it.getLineTotal() != null && !it.getLineTotal().trim().isEmpty()) {
                        line = parseAmount(it.getLineTotal());
                    } else {
                        String unitPriceStr = it.getUnitPrice() != null ? it.getUnitPrice() : "0";
                        BigDecimal unitPrice = parseAmount(unitPriceStr);
                        line = unitPrice.multiply(BigDecimal.valueOf(it.getQuantity()));
                    }
                    total = total.add(line);

                    Label lineLabel = new Label(formatAmount(line));
                    lineLabel.getStyleClass().add("invoice-price");

                    // row: name | spacer | qty | lineTotal
                    row.getChildren().addAll(name, spacer, qty, lineLabel);
                    itemsBox.getChildren().add(row);
                }
            }

            // Use PaymentResponse.amount if present; fallback to computed total
            BigDecimal totalToShow = total;
            if (p.getAmount() != null && !p.getAmount().trim().isEmpty()) {
                try {
                    totalToShow = parseAmount(String.valueOf(p.getAmount()));
                } catch (Exception ignored) {}
            }
            totalLabel.setText(formatAmount(totalToShow));
            paymentMethodLabel.setText(p.getMethod() != null ? p.getMethod().toUpperCase() : "N/A");
            orderIdLabel.setText(String.valueOf(p.getOrderId()));
            String date = p.getCreatedAt() != null ? p.getCreatedAt() : p.getProcessedAt();
            dateLabel.setText(date != null ? date : "-");
        });
    }

    private void populateFromCart() {
        Platform.runLater(() -> {
            itemsBox.getChildren().clear();
            BigDecimal total = BigDecimal.ZERO;
            List<Item> items = TemporaryCart.getInstance().getItems();
            for (Item it : items) {
                HBox row = new HBox();
                row.setSpacing(8);

                Label name = new Label(it.getName() != null ? it.getName() : "");
                name.getStyleClass().add("invoice-item");

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Label qty = new Label(String.valueOf(it.getQuantity()));
                qty.getStyleClass().add("invoice-qty");

                BigDecimal unitPrice = parseAmount(it.getPrice());
                BigDecimal line = unitPrice.multiply(BigDecimal.valueOf(it.getQuantity()));
                total = total.add(line);

                Label lineLabel = new Label(formatAmount(line));
                lineLabel.getStyleClass().add("invoice-price");

                row.getChildren().addAll(name, spacer, qty, lineLabel);
                itemsBox.getChildren().add(row);
            }

            // For cart fallback, use TemporaryCart.getTotal() which returns double
            double cartTotal = TemporaryCart.getInstance().getTotal();
            totalLabel.setText(formatAmount(BigDecimal.valueOf(cartTotal)));
            paymentMethodLabel.setText("N/A");
            orderIdLabel.setText("-");
            dateLabel.setText("-");
        });
    }

    private BigDecimal parseAmount(String s) {
        if (s == null) return BigDecimal.ZERO;
        try {
            try { return new BigDecimal(s); } catch (Exception ignored) {}
            String norm = s.replaceAll("\\.", "").replaceAll(",", ".");
            return new BigDecimal(norm);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private String formatAmount(BigDecimal v) {
        try { return v.stripTrailingZeros().toPlainString(); } catch (Exception e) { return v.toString(); }
    }

    public String getSelectedPayment() {
        Toggle selectedToggle = invoiceGroup.getSelectedToggle();
        if (selectedToggle != null) {
            return ((RadioButton) selectedToggle).getId();
        }
        return null;
    }
    @FXML
    void onContinueAction() throws IOException {
        System.out.println("Continue to Payment");

        String payment = getSelectedPayment();
        if (payment == null) {
            System.out.println("Chưa chọn phương thức!");
            return;
        }
        if (payment.equals("eInvoiceOption")) {
            SceneManager.switchTo("invoice-display.fxml");
        }
        else {
            SceneManager.switchTo("thank.fxml");
        }

    }

    public void onHelpAction(ActionEvent actionEvent) {
        System.out.println("Help clicked");
    }
}
