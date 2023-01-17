package com.example.appointmentmanager;

import DBAccess.Appointments;
import Model.Appointment;
import helper.AlertBox;
import helper.JDBC;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Calendar;

import static com.example.appointmentmanager.HelloApplication.window;

public class HelloControllerFR {

    public TextField usernameTextField;
    public TextField passwordTextField;
    public Label usernameLabel;
    public Label passwordLabel;
    public Label loginMainLabel;
    public AnchorPane anchorPane;
    //    Locale.setDefault(new Locale("fr"));

    public void logInFR(ActionEvent actionEvent) throws SQLException, IOException {
        String sql = String.format("SELECT * FROM users WHERE User_Name = '%s';", usernameTextField.getText());
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        rs.next();
        try {
            if (passwordTextField.getText().equals(rs.getString("Password"))) {
                AppointmentsController.customerID = rs.getInt("User_ID");
                System.out.println(AppointmentsController.customerID);
                System.out.println(AppointmentsController.clientTimeZone);
                System.out.println(Calendar.getInstance().getTimeZone().getRawOffset());
                openApts();
            } else {
                AlertBox.display("Error", "La combinaison utilisateur/mot de passe fourni est introuvable.");
            }
        } catch (SQLException e) {
            AlertBox.display("Error", "La combinaison utilisateur/mot de passe fourni est introuvable.");
        }
    }

    public void exitApp(ActionEvent actionEvent) {
        Stage stage;
        stage = (Stage) anchorPane.getScene().getWindow();
        System.out.println("Programme fermé par l'utilisateur !");
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

    public void openApts() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("appointments.fxml"));
        Scene scene2;
        scene2 = new Scene(fxmlLoader.load(), 1017, 734);
        window.setScene(scene2);
    }
}