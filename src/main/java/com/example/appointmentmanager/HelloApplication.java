package com.example.appointmentmanager;

import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

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

        if (Locale.getDefault().getLanguage() == "en"){
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            window = stage;
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            window.setTitle("Hello!");
            window.setScene(scene);
            window.show();
        } else if (Locale.getDefault().getLanguage() == "fr"){
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-viewFR.fxml"));
            window = stage;
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            window.setTitle("Hello!");
            window.setScene(scene);
            window.show();
        } else {

        }



        if (clientTimeZone.equals("Eastern Standard Time")){

        } else if (clientTimeZone.equals("Western Standard Time")){

        } else if (clientTimeZone.equals("Universal Standard Time")){

        }

    }

    public static void main(String[] args) {
        JDBC.openConnection();
        launch();
    }
}