package com.example.appointmentmanager;

import DBAccess.Appointments;
import DBAccess.Contacts;
import Model.Appointment;
import Model.Contact;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import helper.AlertBox;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class AppointmentsController implements Initializable {
    public TextField aAID;
    public Button saveAppointment;
    public Button cancelAppointment;
    public Button saveCustomer;
    public Button cancelCustomer;
    Integer selectedIndex;
    ObservableList<Appointment> tempAppointments = Appointments.getAllAppointments();
    public TableView<Appointment> aTableView;
    public TableView cTableView;
    public TableColumn<Appointment, Integer> aTID;
    public TableColumn<Appointment, String>  aTTitle;
    public TableColumn<Appointment, String>  aTDesc;
    public TableColumn<Appointment, String>  aTLocation;
    public TableColumn<Contact, String>  aTContactID;
    public TableColumn<Appointment, String>  aTType;
    public TableColumn<Appointment, String>  aTStart;
    public TableColumn<Appointment, String>  aTEnd;
    public TableColumn<Appointment, Integer>  aTCID;
    public TableColumn<Appointment, Integer>  atUID;
    public TableColumn cTID;
    public TableColumn cTName;
    public TableColumn cTAddress;
    public TableColumn cTPostalCode;
    public TableColumn cTPhoneNumber;
    public TableColumn cTDivision;
    public TableColumn cTCountry;
    public DatePicker datePick;
    public TextField aTitle;
    public TextField aDescription;
    public TextField aLocation;
    public ComboBox<String> aContact;
    public TextField aType;
    public TextField aStart;
    public TextField aEnd;
    public TextField aCID;
    public TextField aUserID;
    public Label dynamicLabel;

    public AppointmentsController() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        aTID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        aTTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        aTDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        aTLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        aTContactID.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        aTType.setCellValueFactory(new PropertyValueFactory<>("type"));
        aTStart.setCellValueFactory(new PropertyValueFactory<>("dateStart"));
        aTEnd.setCellValueFactory(new PropertyValueFactory<>("dateEnd"));
        aTCID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        atUID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        try {
            aTableView.setItems(Appointments.getAllAppointments());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        appointmentDisable();
    }


    public void printDate(ActionEvent actionEvent) {
        datePick.show();
        datePick.getValue();
        datePick.valueProperty().get();
        System.out.println(datePick.getValue());
        System.out.println(datePick.valueProperty().get());

//        datePick.valueProperty().getValue().
    }

    public void modifyAppointment(ActionEvent actionEvent) throws SQLException {
        if (aTableView.getSelectionModel().isEmpty()){
            AlertBox.display("Error Message", "No Appointment is Selected!");
            return;
        }
        aTitle.requestFocus();
        dynamicLabel.setText("Modifying Appointment:");
        Appointment tempA =  aTableView.getItems().get(aTableView.getSelectionModel().getSelectedIndex());
        selectedIndex = aTableView.getSelectionModel().getSelectedIndex();
        aTitle.setText(tempA.getTitle());
        aAID.setText((String.valueOf(tempA.getAppointmentID())));
        aDescription.setText(String.valueOf(tempA.getDescription()));
        aLocation.setText(String.valueOf(tempA.getLocation()));
        aStart.setText(String.valueOf(tempA.getDateStart()));
        aEnd.setText(String.valueOf(tempA.getDateEnd()));
        aCID.setText(String.valueOf(tempA.getCustomerID()));
        aUserID.setText(String.valueOf(tempA.getUserID()));
        aType.setText(String.valueOf(tempA.getType()));
        aContact.setItems(Contacts.getAllContactNames());
        aContact.getSelectionModel().select(Contacts.contactDictionary.get(tempA.getContactID()));
        appointmentEnable();
    }
    public void appointmentEnable(){
        aTitle.setDisable(false);
//        aAID.setDisable(false);
        aDescription.setDisable(false);
        aLocation.setDisable(false);
        aStart.setDisable(false);
        aEnd.setDisable(false);
        aCID.setDisable(false);
        aUserID.setDisable(false);
        aContact.setDisable(false);
        aType.setDisable(false);
        saveAppointment.setDisable(false);
        cancelAppointment.setDisable(false);
    }
    public void appointmentDisable(){
        dynamicLabel.setText("Select an Option:");
        selectedIndex = null;
        aTitle.setText("");
        aAID.setDisable(false);
        aAID.setText("");
        aDescription.setText("");
        aLocation.setText("");
        aStart.setText("");
        aEnd.setText("");
        aCID.setText("");
        aUserID.setText("");
        aContact.setItems(null);
        aType.setText("");
        aTitle.setDisable(true);
        aAID.setDisable(true);
        aDescription.setDisable(true);
        aLocation.setDisable(true);
        aStart.setDisable(true);
        aEnd.setDisable(true);
        aCID.setDisable(true);
        aUserID.setDisable(true);
        aContact.setDisable(true);
        aContact.setItems(null);
        aType.setDisable(true);
        saveAppointment.setDisable(true);
        cancelAppointment.setDisable(true);
    }
    public void addAppointment(ActionEvent actionEvent) {
        appointmentDisable();
        appointmentEnable();
        int aid_c;
        System.out.println(tempAppointments.size());
        int N = tempAppointments.size();
        if (N == 0) {
            aid_c = 1;
            aAID.setText(Integer.toString(aid_c));
        } else {
            int[] arr = new int[N + 1];
            for (int i=0; i<N; i++) {
                Appointment tempAppointment = tempAppointments.get(i);
                arr[i] = tempAppointment.getAppointmentID();
            }
            int j;
            int[] temp2 = new int[N + 1];
            for (j = 0; j <= N; j++) {
                temp2[j] = 0;
            }

            for (j = 0; j < N; j++) {
                temp2[arr[j] - 1] = 1;
            }

            int ans = 0;
            for (j = 0; j <= N; j++) {
                if (temp2[j] == 0)
                    ans = j + 1;
            }
            aid_c = ans;
            aAID.setText(Integer.toString(aid_c));
        }
        dynamicLabel.setText("Adding an Appointment:");
        aContact.setItems(Contacts.getAllContactNames());
        aTableView.getSelectionModel().clearSelection();
    }

    public void createAppointment(ActionEvent actionEvent) throws SQLException {
        int a_id_c = 0;
        Integer ID;
        String title;
        String description;
        String location;
        int contactID;
        String type;
        String dateStart;
        String dateEnd;
        int customerID;
        int userID;
        if (Objects.equals(dynamicLabel.getText(), "Modifying Appointment:")) {
            if (Appointments.getAllAppointments().size() == 0) {
                a_id_c = 1;
            } else {
                Appointment id_ce = aTableView.getItems().get(selectedIndex);
                a_id_c = id_ce.getAppointmentID();
            }
        } else if (dynamicLabel.getText().equals("Adding an Appointment:")) {
            int N = Appointments.getAllAppointments().size();
            if (N == 0) {
                a_id_c = 1;
            } else {
                int[] arr = new int[N + 1];
                for (int i = 0; i < N; i++) {
                    Appointment tempPart = tempAppointments.get(i);
                    arr[i] = tempPart.getAppointmentID();
                }
                int j;
                int[] temp2 = new int[N + 1];
                for (j = 0; j <= N; j++) {
                    temp2[j] = 0;
                }
                for (j = 0; j < N; j++) {
                    temp2[arr[j] - 1] = 1;
                }
                int ans = 0;
                for (j = 0; j <= N; j++) {
                    if (temp2[j] == 0)
                        ans = j + 1;
                }
                a_id_c = ans;
            }
        }

        if (aTitle.getText().isEmpty()) {
            AlertBox.display("Error Message", "Title is empty!");
            return;
        }
        if (aDescription.getText().isEmpty()) {
            AlertBox.display("Error Message", "Description is empty!");
            return;
        }
        if (aLocation.getText().isEmpty()) {
            AlertBox.display("Error Message", "Location is empty!");
            return;
        }
        if (aStart.getText().isEmpty()) {
            AlertBox.display("Error Message", "Start is empty!");
            return;
        }
        if (aEnd.getText().isEmpty()) {
            AlertBox.display("Error Message", "End is empty!");
            return;
        }
        if (aCID.getText().isEmpty()) {
            AlertBox.display("Error Message", "Customer ID is empty!");
            return;
        }
        if (aUserID.getText().isEmpty()) {
            AlertBox.display("Error Message", "User ID is empty!");
            return;
        }
        if (aType.getText().isEmpty()) {
            AlertBox.display("Error Message", "Type is empty!");
            return;
        }
        if (aContact.getSelectionModel().isEmpty()) {
            AlertBox.display("Error Message", "No Contact is selected!");
            return;
        }
        try {
            ID = Integer.parseInt(aAID.getText());
        } catch (NumberFormatException nfe) {
            AlertBox.display("Error Message", "Appointment ID is Invalid!");
            return;
        }
        try {
            title = aTitle.getText();
        } catch (NumberFormatException nfe) {
            AlertBox.display("Error Message", "Title is Invalid!");
            return;
        }
        try {
            description = aDescription.getText();
        } catch (NumberFormatException nfe) {
            AlertBox.display("Error Message", "Description is Invalid!");
            return;
        }
        try {
            location = aLocation.getText();
        } catch (NumberFormatException nfe) {
            AlertBox.display("Error Message", "Location is Invalid!");
            return;
        }
        try {
            contactID = Contacts.reverseContactDictionary.get(aContact.getSelectionModel().getSelectedItem());
        } catch (NumberFormatException nfe) {
            AlertBox.display("Error Message", "Contact ID is Invalid!");
            return;
        }
        try {
            type = aType.getText();
        } catch (NumberFormatException nfe) {
            AlertBox.display("Error Message", "Type is Invalid!");
            return;
        }
        try {
            dateStart = aStart.getText();
        } catch (NumberFormatException nfe) {
            AlertBox.display("Error Message", "Start is Invalid!");
            return;
        }
        try {
            dateEnd = aEnd.getText();
        } catch (NumberFormatException nfe) {
            AlertBox.display("Error Message", "End is Invalid!");
            return;
        }
        try {
            customerID = Integer.parseInt(aCID.getText());
        } catch (NumberFormatException nfe) {
            AlertBox.display("Error Message", "Customer ID is Invalid!");
            return;
        }
        try {
            userID = Integer.parseInt(aUserID.getText());
        } catch (NumberFormatException nfe) {
            AlertBox.display("Error Message", "User ID is Invalid!");
            return;
        }

        Appointment c = null;
        if (dynamicLabel.getText().equals("Adding an Appointment:")){
            try {
                c = new Appointment(ID, title, description, location, contactID, type, dateStart, dateEnd, customerID, userID);
                Appointments.addAppointment(ID, title, description, location, contactID, type, dateStart, dateEnd, customerID, userID);
            } catch (MysqlDataTruncation e) {
                AlertBox.display("Error Message", "The date must be in the format of 'YYYY-MM-DD HH:MN:SS'. 0 <= YYYY < 10000, 0 < MM < 13, 00 < DD < 31, 00 <= HH < 25, 00 <= MN < 60, 00 <= SS < 60.");
                Appointments.deleteAppointment(c);
                return;
            }
        }
        else if (dynamicLabel.getText().equals("Modifying Appointment:")){
            try {
                c = new Appointment(ID, title, description, location, contactID, type, dateStart, dateEnd, customerID, userID);
                Appointments.updateAppointment(aTableView.getItems().get(selectedIndex), c);
            } catch (MysqlDataTruncation e) {
                AlertBox.display("Error Message", "The date must be in the format of 'YYYY-MM-DD HH:MN:SS'. 0 <= YYYY < 10000, 0 < MM < 13, 00 < DD < 31, 00 <= HH < 25, 00 <= MN < 60, 00 <= SS < 60.");
                Appointments.deleteAppointment(c);
                return;
            }
        }
        aTableView.setItems(Appointments.getAllAppointments());
        tempAppointments = Appointments.getAllAppointments();
        appointmentDisable();
    }

    public void cancelAppointment(ActionEvent actionEvent) {
        appointmentDisable();
        aTableView.getSelectionModel().clearSelection();
    }




    public void createCustomer(ActionEvent actionEvent) {
    }

    public void cancelCustomer(ActionEvent actionEvent) {
    }

    public void deleteAppointment(ActionEvent actionEvent) throws SQLException {
            if (!aTableView.getSelectionModel().isEmpty()) {
                    if (Appointments.deleteAppointment(Appointments.getAllAppointments().get(aTableView.getSelectionModel().getSelectedIndex()))){
                        AlertBox.display("Alert", "Appointment was successfully deleted.");
                    }
                    else {
                        AlertBox.display("Alert", "Product could not be deleted while having associated parts.");
                    }
                }
            else {
                AlertBox.display("Error Message", "No Product was Selected.");
            }
            aTableView.setItems(Appointments.getAllAppointments());
            tempAppointments = Appointments.getAllAppointments();
    }
}
