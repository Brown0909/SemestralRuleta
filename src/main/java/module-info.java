module com.brown0909.semestralruleta {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.brown0909.semestralruleta to javafx.fxml;
    exports com.brown0909.semestralruleta;
}