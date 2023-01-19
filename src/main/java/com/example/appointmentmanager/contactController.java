package com.example.appointmentmanager;

import helper.AlertBox;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class contactController implements Initializable {

    public static ObservableList contactAppointments;
    public AnchorPane anchorPane;
    public TableColumn cAID;


    @FXML
    private TableColumn<?, ?> cCID;
    @FXML
    private TableColumn<?, ?> cDescription;
    @FXML
    private TableColumn<?, ?> cEnd;
    @FXML
    private TableColumn<?, ?> cStart;
    @FXML
    private TableColumn<?, ?> cTitle;
    @FXML
    private TableColumn<?, ?> cType;
    @FXML
    private TableView<?> contactTableView;

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


    public void exitApp() {
        Stage stage;
        stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }
}

