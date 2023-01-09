module com.example.appointmentmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens com.example.appointmentmanager to javafx.fxml;
    exports Model;
    exports com.example.appointmentmanager;
}