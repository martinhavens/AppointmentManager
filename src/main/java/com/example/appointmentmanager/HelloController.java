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

/**
 * The log-in form for the English case of the gui.
 */
public class HelloController {

    /** a Button element of the gui implementation **/
    public TextField usernameTextField;
    /** a TextField element of the gui implementation **/
    public TextField passwordTextField;
    /** a Label element of the gui implementation **/
    public Label usernameLabel;
    /** a Label element of the gui implementation **/
    public Label passwordLabel;
    /** a Label element of the gui implementation **/
    public Label loginMainLabel;
    /** an AnchorPane element of the gui implementation **/
    public AnchorPane anchorPane;

    /**
     * a File object to contain all of the attempted logins.
     */
    public static File myObj = new File("./loginlog.txt");
    /**
     * A FileWriter object to append to the File object that contains all of the attempted logins.
     */
    public static FileWriter fr;

    /**
     * A FileWriter object to append to the File object that contains all of the attempted logins.
     */
    static {
        try {
            fr = new FileWriter(myObj, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A PrintWriter object to append to the File object that contains all of the attempted logins.
     */
    public static PrintWriter pr = new PrintWriter(fr);

    /**
     * A function using the PrinterWriter object to append the String parameter into the File object that contains all
     * of the attempted logins.
     * @param s
     */
    public void loginRecord(String s) {
        pr.println(s);
    }

    /**
     * A button onAction function that validates log-in attempt entries and records the appropriate log-in attempt
     * result into the File object that holds all of the login attempts.
     * @throws SQLException
     * @throws IOException
     */
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

    /**
     * A button onAction function that closes the entire program while at the log-in screen.
     */
    public void exitApp() {
        pr.close();
        Stage stage;
        stage = (Stage) anchorPane.getScene().getWindow();
        System.out.println("Program closed by user!");
        stage.close();
    }

    /**
     * A function that is called whenever a successful username and password combination is entered into the log-in form.
     * @throws IOException
     */
    public void openApts() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("appointments.fxml"));
        Scene scene2;
        scene2 = new Scene(fxmlLoader.load(), 1017, 754);
        window.setScene(scene2);
    }
}