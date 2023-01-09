package com.example.appointmentmanager;

import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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
    }

    public static void main(String[] args) {
        JDBC.openConnection();
        launch();
    }
}