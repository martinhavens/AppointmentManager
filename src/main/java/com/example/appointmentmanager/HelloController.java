package com.example.appointmentmanager;

import DBAccess.Appointments;
import Model.Appointment;
import helper.JDBC;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import static com.example.appointmentmanager.HelloApplication.window;

public class HelloController {

    public TextField usernameTextField;
    public TextField passwordTextField;
    public Label usernameLabel;
    public Label passwordLabel;
    public Label loginMainLabel;
    public AnchorPane anchorPane;
    //    Locale.setDefault(new Locale("fr"));

    public void logIn(ActionEvent actionEvent) {

    }

    public void exitApp(ActionEvent actionEvent) {
        Stage stage;
        stage = (Stage) anchorPane.getScene().getWindow();
        System.out.println("Program closed by user!");
        stage.close();
    }

    public void closeConnection(ActionEvent actionEvent) {
        JDBC.closeConnection();
    }


    public void showMe(ActionEvent actionEvent) throws SQLException {
        ObservableList<Appointment> alist = Appointments.getAllAppointments();
        for (Appointment A: alist){
            System.out.println(A);
        }
    }

    public void openApts(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("appointments.fxml"));
        Scene scene2;
        scene2 = new Scene(fxmlLoader.load(), 1017, 734);
        window.setScene(scene2);
    }
}