module com.zascapay.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires opencv;
    requires javafx.graphics;
    requires javafx.base;
    requires retrofit2;
    requires okhttp3;
    requires retrofit2.converter.gson;
    requires java.sql;
    requires gson;
    requires okhttp3.logging; // thêm dòng này

//    requires com.zascapay.client;
//    requires com.zascapay.client;

    exports com.zascapay.client;
    opens com.zascapay.client.service.dto.response;
    opens com.zascapay.client.service.dto.request to gson, retrofit2.converter.gson;

    opens com.zascapay.client to javafx.fxml;
    opens com.zascapay.client.controller to javafx.fxml;
    opens com.zascapay.client.util to javafx.fxml;
    opens com.zascapay.client.component to javafx.fxml;


}