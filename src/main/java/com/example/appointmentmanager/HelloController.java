package com.example.appointmentmanager;

import helper.AlertBox;
import helper.JDBC;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

import static com.example.appointmentmanager.HelloApplication.window;

public class HelloController {

    public TextField usernameTextField;
    public TextField passwordTextField;
    public Label usernameLabel;
    public Label passwordLabel;
    public Label loginMainLabel;
    public AnchorPane anchorPane;
    public static File myObj = new File("./loginlog.txt");
    public static FileWriter fr;

    static {
        try {
            fr = new FileWriter(myObj, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PrintWriter pr = new PrintWriter(fr);

    public void loginRecord(String s) {
        pr.println(s);
    }

    public void logInEN() throws SQLException, IOException {
        String sql = String.format("SELECT * FROM users WHERE User_Name = '%s';", usernameTextField.getText());
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        rs.next();
        try {
            if (passwordTextField.getText().equals(rs.getString("Password"))) {
                loginRecord("Success:Valid User:Valid Pass" + " : Username: " + usernameTextField.getText() + " : " + LocalDateTime.now(ZoneId.of(Calendar.getInstance().getTimeZone().getID())).truncatedTo(ChronoUnit.SECONDS) + " : " +Calendar.getInstance().getTimeZone().getID());
                AppointmentsController.userID = rs.getInt("User_ID");
                pr.close();
                openApts();
            } else {
                loginRecord("Failure:Valid User:Invalid Password" +" : Username: " + usernameTextField.getText() + " : " + LocalDateTime.now(ZoneId.of(Calendar.getInstance().getTimeZone().getID())).truncatedTo(ChronoUnit.SECONDS)+ " : " + Calendar.getInstance().getTimeZone().getID());
                AlertBox.display("Error", "Provided User/Password combination not found..");
            }
        } catch (SQLException e) {
            loginRecord("Failure:Invalid User:Invalid Password" + " : Username: " + usernameTextField.getText() + " : " + LocalDateTime.now(ZoneId.of(Calendar.getInstance().getTimeZone().getID())).truncatedTo(ChronoUnit.SECONDS) + " : " + Calendar.getInstance().getTimeZone().getID());
            AlertBox.display("Error", "Username not found.");
        }

    }

    public void exitApp() {
        pr.close();
        Stage stage;
        stage = (Stage) anchorPane.getScene().getWindow();
        System.out.println("Program closed by user!");
        stage.close();
    }

    public void openApts() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("appointments.fxml"));
        Scene scene2;
        scene2 = new Scene(fxmlLoader.load(), 1017, 734);
        window.setScene(scene2);
    }
}