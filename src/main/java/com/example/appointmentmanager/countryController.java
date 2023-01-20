package com.example.appointmentmanager;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller class of the secondary gui form for viewing the number of customers per country of
 * ../resources/country-view.fxml.
 */
public class countryController implements Initializable{

    /**
     * An observablelist populated by an external function to display the number of customers per country name.
     */
    public static ObservableList countryCount;
    /** an AnchorPane element of the gui implementation **/
    public AnchorPane anchorPane;
    /** a TableView element of the gui implementation **/
    public TableView countryTableView;
    /** a TableColumn element of the gui implementation **/
    public TableColumn cAmount;
    /** a TableColumn element of the gui implementation **/
    public TableColumn cCountry;

    /**
     * Initialize tableviews and enables or disables the relevant report form objects.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        cAmount.setCellValueFactory(new PropertyValueFactory<>("numberOf"));
        cCountry.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        countryTableView.setItems(countryCount);
    }

    public void exitApp() {
        Stage stage;
        stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }
}
