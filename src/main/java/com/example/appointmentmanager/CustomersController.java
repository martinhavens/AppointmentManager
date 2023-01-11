package com.example.appointmentmanager;

import javafx.event.ActionEvent;

import java.sql.SQLException;

public class CustomersController extends AppointmentsController{
    public CustomersController() throws SQLException {
    }

    public static void letsPrint(ActionEvent actionEvent) {
        System.out.println("hey");
    }


}
