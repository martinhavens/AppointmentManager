package helper;

import javafx.scene.Scene;
import javafx.stage.*;
import  javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

/**
 * Creates a dialogue window when called.
 */
public class AlertBox {
    /**
     * Displays a dialogue window with the selected title and message with a button to close the window.
     * A lambda function allows easy access to the event handler while being accompanied with a written
     * functions to set as a button's GUI action event.
     * @param title sets the title of the alert box.
     * @param message sets the message contents of the box.
     */
    public static void display(String title, String message) {
        Stage window = new Stage();
        window.setTitle(title);
        window.setMinWidth(200);

        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("Close the window");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

}
