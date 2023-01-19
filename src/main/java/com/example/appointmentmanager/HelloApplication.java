package com.example.appointmentmanager;

import helper.JDBC;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.IOException;
import java.util.Locale;

/**
 * The original application file that opens specific fxml depending on the user's computer system's language.
 * The languages are English or French.
 */
public class HelloApplication extends Application {
    /**
     * The primary window for the gui application across fxml files.
     */
    public static Stage window;

    /**
     * Determines the user's system language and opens the appropriate fxml file.
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        if (Locale.getDefault().getLanguage().equals("en")){
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            window = stage;
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            window.setTitle("Hello!");
            window.setScene(scene);
            window.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    System.out.println("Window Closed by User");
                    HelloController.pr.close();
                }
            });
            window.show();
        } else if (Locale.getDefault().getLanguage().equals("fr")){
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-viewFR.fxml"));
            window = stage;
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            window.setTitle("Bonjour!");
            window.setScene(scene);
            window.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    System.out.println("Fenêtre fermée par l'utilisateur");
                    HelloController.pr.close();
                }
            });
            window.show();
        } else {
            System.out.println("No supported language found.");
        }
    }

    public static void main(String[] args) {
        JDBC.openConnection();
        launch();
    }
}