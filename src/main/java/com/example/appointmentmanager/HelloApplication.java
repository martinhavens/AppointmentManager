package com.example.appointmentmanager;

import helper.JDBC;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Locale;

public class HelloApplication extends Application {

    public static Stage window;

    @Override
    public void start(Stage stage) throws IOException {
        ZoneId clientZoneID = ZoneId.systemDefault(); // America/New_York
        String clientLanguage = Locale.getDefault().getLanguage(); // en
        String clientTimeZone = Calendar.getInstance().getTimeZone().getDisplayName(); // Eastern Standard Time

        System.out.println(clientZoneID);
        System.out.println(clientLanguage);
        System.out.println(clientTimeZone);



        if (Locale.getDefault().getLanguage().equals("en")){
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            window = stage;
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            window.setTitle("Hello!");
            window.setScene(scene);
            window.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    System.out.println("Window Event handler works");
                    HelloController.pr.close();
                }
            });
            window.show();
        } else if (Locale.getDefault().getLanguage().equals("fr")){
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-viewFR.fxml"));
            window = stage;
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            window.setTitle("Hello!");
            window.setScene(scene);
            window.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    HelloController.pr.close();
                }
            });
            window.show();
        }
    }

    public static void main(String[] args) {
        JDBC.openConnection();
        launch();
    }
}