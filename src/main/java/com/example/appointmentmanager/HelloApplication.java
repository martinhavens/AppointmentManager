package com.example.appointmentmanager;

import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

public class HelloApplication extends Application {

    public static Stage window;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        window = stage;
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        window.setTitle("Hello!");
        window.setScene(scene);
        window.show();

        TimeZone tz = Calendar.getInstance().getTimeZone();
        System.out.println("TimeZone: "+tz.getDisplayName());
        System.out.println("ID: "+tz.getID());
    }

    public static void main(String[] args) {
        JDBC.openConnection();
        launch();
    }
}