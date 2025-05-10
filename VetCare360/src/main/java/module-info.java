module com.vetcare360 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.vetcare360.app to javafx.fxml;
    opens com.vetcare360.controller to javafx.fxml;
    opens com.vetcare360.model to javafx.base;
    exports com.vetcare360.app;
    exports com.vetcare360.controller;
}
