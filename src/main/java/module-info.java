module com.zascapay.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires opencv;
    requires javafx.graphics;
//    requires com.zascapay.client;

    exports com.zascapay.client;

    opens com.zascapay.client to javafx.fxml;
    opens com.zascapay.client.controller to javafx.fxml;
    opens com.zascapay.client.util to javafx.fxml;
    opens com.zascapay.client.component to javafx.fxml;


}