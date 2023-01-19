package com.example.appointmentmanager;

import helper.AlertBox;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller class of the secondary gui form for viewing single Contact Calendars of ../resources/contact-view.fxml
 */
public class contactController implements Initializable {

    /**
     * An observablelist populated by an external function to display all the appointments of a single contact.
     */
    public static ObservableList contactAppointments;

    /** an AnchorPane element of the gui implementation **/
    public AnchorPane anchorPane;
    /** a TableColumn element of the gui implementation **/
    public TableColumn cAID;
    @FXML
    /** a Button element of the gui implementation **/
    private TableColumn<?, ?> cCID;
    @FXML
    /** a TableColumn element of the gui implementation **/
    private TableColumn<?, ?> cDescription;
    @FXML
    /** a TableColumn element of the gui implementation **/
    private TableColumn<?, ?> cEnd;
    @FXML
    /** a TableColumn element of the gui implementation **/
    private TableColumn<?, ?> cStart;
    @FXML
    /** a TableColumn element of the gui implementation **/
    private TableColumn<?, ?> cTitle;
    @FXML
    /** a TableColumn element of the gui implementation **/
    private TableColumn<?, ?> cType;
    @FXML
    /** a TableView element of the gui implementation **/
    private TableView<?> contactTableView;

    /**
     * Initialize tableviews and enables or disables the relevant report form objects.
     * @param url is a default javafx parameter
     * @param resourceBundle is a default javafx parameter
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        cAID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        cTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        cDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        cType.setCellValueFactory(new PropertyValueFactory<>("type"));
        cStart.setCellValueFactory(new PropertyValueFactory<>("dateStart"));
        cEnd.setCellValueFactory(new PropertyValueFactory<>("dateEnd"));
        cCID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        contactTableView.setItems(contactAppointments);
        if (contactAppointments.size() == 0 || contactAppointments.isEmpty()){
            AlertBox.display("Information!", "This contact has zero appointments.");
        }
    }

    /**
     * A button onAction function to close the report window.
     */
    public void exitApp() {
        Stage stage;
        stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }
}

