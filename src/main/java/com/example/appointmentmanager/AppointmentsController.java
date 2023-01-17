package com.example.appointmentmanager;
import DBAccess.Appointments;
//import DBAccess.AppointmentsTime;
import DBAccess.AppointmentsTime;
import DBAccess.Contacts;
import DBAccess.Customers;
import Model.Appointment;
import Model.Contact;
import Model.Customer;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import helper.AlertBox;
import helper.JDBC;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;

import java.net.URL;
import java.nio.file.FileSystems;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Objects;
import java.util.ResourceBundle;

public class AppointmentsController implements Initializable {
    public TextField aAID;
    public Button saveAppointment;
    public Button cancelAppointment;
    public Button saveCustomer;
    public Button cancelCustomer;
    public TextField cCID;
    public TextField cName;
    public TextField cAddress;
    public TextField cPostal;
    public TextField cPhone;
    public ComboBox cDivision;
    @FXML
    public ComboBox cCountry;
    public Button modifyCustomer;
    public Button modifyAppointment;
    public Button addAppointment;
    public Button addCustomer;
    public Button deleteAppointment;
    public Button deleteCustomer;
    public DatePicker endDatePick;
    public DatePicker startDatePick;
    public ComboBox startTimePick;
    public ComboBox endTimePick;
    public RadioButton monthlyFilter;
    public RadioButton weeklyFilter;
    public Button filterHigher;
    public Button filterLower;
    public ToggleGroup filterGroup;
    public Label referenceFrame;
    public String clientTimeZone = Calendar.getInstance().getTimeZone().getDisplayName(); // Eastern Standard Time

    Integer selectedIndex;
    ObservableList<Appointment> tempAppointments = Appointments.getAllAppointments();
    ObservableList<Customer> tempCustomers = Customers.getAllCustomers();
    ObservableList<String> divisions = FXCollections.observableArrayList();
    public TableView<Appointment> aTableView;
    public TableView<Customer> cTableView;
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
    public TableColumn<Customer, Integer> cTID;
    public TableColumn<Customer, String> cTName;
    public TableColumn<Customer, String> cTAddress;
    public TableColumn<Customer, String> cTPostalCode;
    public TableColumn<Customer, String> cTPhoneNumber;
    public TableColumn<Customer, Integer> cTDivision;
    public TableColumn cTCountry;
    public DatePicker datePick;
    public TextField aTitle;
    public TextField aDescription;
    public TextField aLocation;
    public ComboBox<String> aContact;
    public TextField aType;
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
//        aTStart.setCellValueFactory(cellData -> cellData.getValue().getDateStartDate().concat(" ").concat(cellData.getValue().getDateStartTime()));
//        aTStart.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getDateStartDate() + " " + cellData.getValue().getDateStartTime()));
//        aTEnd.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getDateEndDate() + " " + cellData.getValue().getDateEndTime()));
        aTEnd.setCellValueFactory(new PropertyValueFactory<>("dateEnd"));

        aTCID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        atUID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        cTID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        cTName.setCellValueFactory(new PropertyValueFactory<>("name"));
        cTAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        cTPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        cTPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        cTDivision.setCellValueFactory(new PropertyValueFactory<>("division"));
        cTCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        try {
            aTableView.setItems(Appointments.getAllAppointments());
            cTableView.setItems(Customers.getAllCustomers());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        appointmentDisable();
        customerDisable();
        modifyCustomer.setDisable(true);
        deleteCustomer.setDisable(true);

        cTableView.getSelectionModel().selectedIndexProperty().addListener((obs, oldV, newV) -> {
            if (newV != null){
                modifyCustomer.setDisable(false);
                deleteCustomer.setDisable(false);
            }
            else {
                modifyCustomer.setDisable(true);
                deleteCustomer.setDisable(true);
            }
        });
        modifyAppointment.setDisable(true);
        deleteAppointment.setDisable(true);

        aTableView.getSelectionModel().selectedIndexProperty().addListener((obse, oldVa, newVa) -> {
            if (newVa != null){
                modifyAppointment.setDisable(false);
                deleteAppointment.setDisable(false);
            }
            else {
                modifyAppointment.setDisable(true);
                deleteAppointment.setDisable(true);
            }
        });
        filterLower.setDisable(true);
        filterHigher.setDisable(true);
        try {
            Customers.getAllDivisions();
            Customers.getAllCountries();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void modifyCustomer(ActionEvent actionEvent) throws SQLException {
        if (cTableView.getSelectionModel().isEmpty()){
            AlertBox.display("Error Message", "No Customer is Selected!");
            return;
        }
        cName.requestFocus();
        dynamicLabel.setText("Modifying Customer:");
        Customer tempC =  cTableView.getItems().get(cTableView.getSelectionModel().getSelectedIndex());
        selectedIndex = cTableView.getSelectionModel().getSelectedIndex();
        cName.setText(tempC.getName());
        cCID.setText((String.valueOf(tempC.getCustomerID())));
        cAddress.setText(String.valueOf(tempC.getAddress()));
        cPhone.setText(String.valueOf(tempC.getPhoneNumber()));
        cPostal.setText(String.valueOf(tempC.getPostalCode()));
        cCountry.setItems(Customers.getAllCountries());
        int i = 0;
        cCountry.getSelectionModel().selectFirst();
        while (!cCountry.getItems().get(i).equals(Customers.lookupCustomerCountry(tempC.getDivisionID()))){
            i++;
            cCountry.getSelectionModel().selectNext();
        }
        i = 0;
        cDivision.getSelectionModel().selectFirst();
        while (!cDivision.getItems().get(i).equals(tempC.getDivision())){
            i++;
            cDivision.getSelectionModel().selectNext();
        }
        customerEnable();
        addCustomer.setDisable(true);
        deleteCustomer.setDisable(true);
    }
    public void customerEnable(){
        cName.setDisable(false);
        cAddress.setDisable(false);
        cPhone.setDisable(false);
        cPostal.setDisable(false);
        cCountry.setDisable(false);
        cDivision.setDisable(false);
        saveCustomer.setDisable(false);
        cancelCustomer.setDisable(false);
    }
    public void customerDisable(){
        cName.setDisable(true);
        cCID.setDisable(true);
        cAddress.setDisable(true);
        cPhone.setDisable(true);
        cPostal.setDisable(true);
        cCountry.setDisable(true);
        cDivision.setDisable(true);
        saveCustomer.setDisable(true);
        cancelCustomer.setDisable(true);
        cName.setText("");
        cCID.setText("");
        cAddress.setText("");
        cPhone.setText("");
        cPostal.setText("");
        cCountry.setItems(null);
        cDivision.setItems(null);
        modifyCustomer.setDisable(true);
        deleteCustomer.setDisable(true);
        addCustomer.setDisable(false);
    }
    public void addCustomer(ActionEvent actionEvent) throws SQLException {
        customerDisable();
        customerEnable();
        int cid_c;
        int N = tempCustomers.size();
        if (N == 0) {
            cid_c = 1;
            cCID.setText(Integer.toString(cid_c));
        } else {
            int[] arr = new int[N + 1];
            for (int i=0; i<N; i++) {
                Customer tempCustomer = tempCustomers.get(i);
                arr[i] = tempCustomer.getCustomerID();
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
            cid_c = ans;
            cCID.setText(Integer.toString(cid_c));
        }
        dynamicLabel.setText("Adding a Customer:");
        cCountry.setItems(Customers.getAllCountries());
        cDivision.setItems(Customers.getAllDivisions());
        cTableView.getSelectionModel().clearSelection();
        modifyCustomer.setDisable(true);
        deleteCustomer.setDisable(true);
    }
    public void createCustomer(ActionEvent actionEvent) throws SQLException {
        int a_id_c = 0;
        Integer ID;
        String name;

        String address;
        String phone;
        String postal;
        String country;
        String division;

        int customerID;
        int userID;
        if (Objects.equals(dynamicLabel.getText(), "Modifying Customer:")) {
            if (Customers.getAllCustomers().size() == 0) {
                a_id_c = 1;
            } else {
                Customer id_ce = cTableView.getItems().get(selectedIndex);
                a_id_c = id_ce.getCustomerID();
            }
        } else if (dynamicLabel.getText().equals("Adding a Customer:")) {
            int N = Customers.getAllCustomers().size();
            if (N == 0) {
                a_id_c = 1;
            } else {
                int[] arr = new int[N + 1];
                for (int i = 0; i < N; i++) {
                    Customer tempPart = tempCustomers.get(i);
                    arr[i] = tempPart.getCustomerID();
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

        if (cName.getText().isEmpty()) {
            AlertBox.display("Error Message", "Name is empty!");
            return;
        }
        if (cAddress.getText().isEmpty()) {
            AlertBox.display("Error Message", "Address is empty!");
            return;
        }
        if (cPhone.getText().isEmpty()) {
            AlertBox.display("Error Message", "Phone Number is empty!");
            return;
        }

        if (cPostal.getText().isEmpty()){
            AlertBox.display("Error Message", "Postal Code is empty!");
            return;
        }
        if (cCountry.getValue() == null){
            AlertBox.display("Error Message", "Country is not selected!");
            return;
        }
        if (cDivision.getValue() == null){
            AlertBox.display("Error Message", "Division is not selected!");
            return;
        }
        try {
            ID = Integer.parseInt(cCID.getText());
        } catch (NumberFormatException nfe) {
            AlertBox.display("Error Message", "Customer ID is Invalid!");
            return;
        }
        try {
            name = cName.getText();
        } catch (NumberFormatException nfe) {
            AlertBox.display("Error Message", "Name is Invalid!");
            return;
        }
        try {
            address = cAddress.getText();
        } catch (NumberFormatException nfe) {
            AlertBox.display("Error Message", "Address is Invalid!");
            return;
        }
        try {
            phone = cPhone.getText();
        } catch (NumberFormatException nfe) {
            AlertBox.display("Error Message", "Phone is Invalid!");
            return;
        }
        try {
            country = (String)cCountry.getSelectionModel().getSelectedItem();
        } catch (NumberFormatException nfe) {
            AlertBox.display("Error Message", "Country is Invalid!");
            return;
        }
        try {
            division = (String)cDivision.getSelectionModel().getSelectedItem();
        } catch (NumberFormatException nfe) {
            AlertBox.display("Error Message", "Division is Invalid!");
            return;
        }
        try {
            postal = cPostal.getText();
        } catch (NumberFormatException nfe) {
            AlertBox.display("Error Message", "Postal Code is Invalid!");
            return;
        }
        Customer c = null;
        if (dynamicLabel.getText().equals("Adding a Customer:")){
            try {
                c = new Customer(ID, name, address, phone, postal, Customers.divisionsMap.get(division));
                Customers.addCustomer(ID, name, address, phone, postal, Customers.divisionsMap.get(division));
            } catch (Exception e) {
                Customers.deleteCustomer(c);
                e.printStackTrace();
                return;
            }
        }

        else if (dynamicLabel.getText().equals("Modifying Customer:")){
            try {
                c = new Customer(ID, name, address, phone, postal, Customers.divisionsMap.get(division));
                Customers.updateCustomer(cTableView.getItems().get(selectedIndex), c);
            } catch (Exception e) {
                e.printStackTrace();
                Customers.deleteCustomer(c);
                e.printStackTrace();
                return;
            }
        }
        cTableView.setItems(Customers.getAllCustomers());
        tempCustomers = Customers.getAllCustomers();
        customerDisable();

    }
    public void cancelCustomer(ActionEvent actionEvent) throws SQLException {
        divisions = Customers.getAllDivisions();
        cName.clear();
        cCID.clear();
        cAddress.clear();
        cPhone.clear();
        cPostal.clear();
        cCountry.setItems(null);
        cDivision.setItems(null);
        customerDisable();
        addCustomer.setDisable(false);
        cTableView.getSelectionModel().clearSelection();
        modifyCustomer.setDisable(true);
        deleteCustomer.setDisable(true);
        dynamicLabel.setText("Select an Option:");
    }
    public void deleteCustomer(ActionEvent actionEvent) throws SQLException {
        if (!cTableView.getSelectionModel().isEmpty()) {
            if (Customers.deleteCustomer(Customers.getAllCustomers().get(cTableView.getSelectionModel().getSelectedIndex()))){
                AlertBox.display("Alert", "Customer was successfully deleted.");
            }
            else {
                AlertBox.display("Alert", "Customer could not be deleted while having appointments.");
            }
        }
        else {
            AlertBox.display("Error Message", "No Customer was Selected.");
        }
        cTableView.getSelectionModel().clearSelection();
        modifyCustomer.setDisable(true);
        deleteCustomer.setDisable(true);
        cTableView.setItems(Customers.getAllCustomers());
        tempCustomers = Customers.getAllCustomers();
    }
    public void filterDivisions() throws SQLException {
        divisions.clear();
        //cDivision.setItems(Customers.getAllDivisions());
        Integer countryInt = Customers.countriesMap.get(cCountry.getSelectionModel().getSelectedItem());
        String sql = String.format("SELECT Division from first_level_divisions WHERE COUNTRY_ID ='%d';", countryInt);
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        String divisionName = null;
        while (rs.next()){
            divisionName = rs.getString("Division");
            if (divisionName.equals("QuÃ©bec")){
                divisionName = "Quebec";
            }
            divisions.add(divisionName);
        }
        cDivision.setItems(divisions);
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
        startDatePick.setValue(tempA.getDateStart().toLocalDate());
        startTimePick.setValue(tempA.getDateStart().toLocalTime());
        endTimePick.setValue(tempA.getDateEnd().toLocalTime());
        endDatePick.setValue(tempA.getDateEnd().toLocalDate());
        aCID.setText(String.valueOf(tempA.getCustomerID()));
        aUserID.setText(String.valueOf(tempA.getUserID()));
        aType.setText(String.valueOf(tempA.getType()));
        aContact.setItems(Contacts.getAllContactNames());
        startTimePick.setItems(AppointmentsTime.timeOptions());
        endTimePick.setItems(AppointmentsTime.timeOptions());
        aContact.getSelectionModel().select(Contacts.contactDictionary.get(tempA.getContactID()));
        addAppointment.setDisable(true);
        deleteAppointment.setDisable(true);
        appointmentEnable();
        addAppointment.setDisable(true);
        deleteAppointment.setDisable(true);
    }
    public void appointmentEnable(){
        aTitle.setDisable(false);
//        aAID.setDisable(false);
        aDescription.setDisable(false);
        aLocation.setDisable(false);
        aCID.setDisable(false);
        aUserID.setDisable(false);
        aContact.setDisable(false);
        startTimePick.setDisable(false);
        endTimePick.setDisable(false);
        startDatePick.setDisable(false);
        endDatePick.setDisable(false);
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
        aCID.setText("");
        aUserID.setText("");
        aContact.setItems(null);
        startTimePick.setItems(null);
        endTimePick.setItems(null);
        aType.setText("");
        aTitle.setDisable(true);
        aAID.setDisable(true);
        aDescription.setDisable(true);
        aLocation.setDisable(true);
        aCID.setDisable(true);
        aUserID.setDisable(true);
        aContact.setDisable(true);
        startTimePick.setDisable(true);
        endTimePick.setDisable(true);
        startDatePick.setValue(null);
        endDatePick.setValue(null);
        startDatePick.setDisable(true);
        endDatePick.setDisable(true);
        aContact.setItems(null);
        aType.setDisable(true);
        saveAppointment.setDisable(true);
        cancelAppointment.setDisable(true);
        addAppointment.setDisable(false);
        deleteAppointment.setDisable(false);
        modifyAppointment.setDisable(false);
    }
    public void addAppointment(ActionEvent actionEvent) {
        appointmentDisable();
        appointmentEnable();
        int aid_c;
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
        startTimePick.setItems(AppointmentsTime.timeOptions());
        endTimePick.setItems(AppointmentsTime.timeOptions());
        aTableView.getSelectionModel().clearSelection();
        modifyAppointment.setDisable(true);
        deleteAppointment.setDisable(true);
    }
    public void createAppointment(ActionEvent actionEvent) throws SQLException {
        int a_id_c = 0;
        Integer ID;
        String title;
        String description;
        String location;
        int contactID;
        String type;
        LocalDateTime dateStart;
        LocalDateTime dateEnd;

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


        if (startDatePick.getValue() == null){
            AlertBox.display("Error Message", "Date Start is empty!");
            return;
        }
        if (endDatePick.getValue() == null){
            AlertBox.display("Error Message", "Date End is empty!");
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
        System.out.println(startDatePick.getValue());
        System.out.println(startTimePick.getValue());
//        LocalDateTime newStart = LocalDateTime.of(startDatePick.getValue(), startTimePick.getValue());
        dateStart = LocalDateTime.of(startDatePick.getValue(), LocalTime.parse(startTimePick.getValue().toString()));
        dateEnd = LocalDateTime.of(endDatePick.getValue(), LocalTime.parse(endTimePick.getValue().toString()));

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
                e.printStackTrace();
                return;
            }
        }
        aTableView.setItems(Appointments.getAllAppointments());
        tempAppointments = Appointments.getAllAppointments();
        appointmentDisable();
    }
    public void cancelAppointment(ActionEvent actionEvent) {
        appointmentDisable();
        modifyAppointment.setDisable(true);
        deleteAppointment.setDisable(true);
        aTableView.getSelectionModel().clearSelection();
    }
    public void deleteAppointment(ActionEvent actionEvent) throws SQLException {
            if (!aTableView.getSelectionModel().isEmpty()) {
                    if (Appointments.deleteAppointment(Appointments.getAllAppointments().get(aTableView.getSelectionModel().getSelectedIndex()))){
                        AlertBox.display("Alert", "Appointment was successfully deleted.");
                    }
                    else {
                        AlertBox.display("Alert", "Appointment could not be deleted while having associated parts.");
                    }
                }
            else {
                AlertBox.display("Error Message", "No Appointment was Selected.");
            }
            aTableView.setItems(Appointments.getAllAppointments());
            tempAppointments = Appointments.getAllAppointments();
    }

    public void appointmentsMonthly(ActionEvent actionEvent) throws SQLException {
        filterLower.setDisable(false);
        filterHigher.setDisable(false);
        tempAppointments = Appointments.getAllAppointments();
        LocalDateTime min = null;
        int c = 0;
        if (referenceFrame.getText().equals("All")){
            for (Appointment a : tempAppointments){
                if (min == null) {
                    min = a.getDateStart();
                }
                if (a.getDateStart().isBefore(min)){
                    min = a.getDateStart();
                } else { }
            }
        } else {
            min = LocalDate.parse(referenceFrame.getText()).atStartOfDay();
        }

        ObservableList tempItems = FXCollections.observableArrayList();
        for (Appointment a : tempAppointments){
            if (a.getDateStart().getMonth() == min.getMonth() && a.getDateStart().getYear() == min.getYear()){
                tempItems.add(a);
            }
        }
        aTableView.setItems(tempItems);
        referenceFrame.setText(String.valueOf(min.toLocalDate()));
        filterLower.setOnAction(e -> filterLowerMonth());
        filterHigher.setOnAction(e -> filterHigherMonth());
    }

    public void appointmentsWeekly(ActionEvent actionEvent) throws SQLException {
        filterLower.setDisable(false);
        filterHigher.setDisable(false);
        tempAppointments = Appointments.getAllAppointments();
        LocalDateTime min = null;
        int c = 0;
        if (referenceFrame.getText().equals("All")) {
            for (Appointment a : tempAppointments) {
                if (min == null) {
                    min = a.getDateStart();
                } else {
                    c = a.getDateStart().compareTo(min);
                }
                if (c < 0) {
                    min = a.getDateStart();
                } else { }
            }
        } else {
            min = LocalDate.parse(referenceFrame.getText()).atStartOfDay();
        }
        ObservableList tempItems = FXCollections.observableArrayList();
        for (Appointment a : tempAppointments){
            if (a.getDateStart().getMonth().equals(min.getMonth()) && (a.getDateStart().getYear() == min.getYear()) && (a.getDateStart().isBefore(min.plusDays(7))) && a.getDateStart().isAfter(min.plusDays(-1))){
                tempItems.add(a);
            }
        }
        aTableView.setItems(tempItems);
        referenceFrame.setText(String.valueOf(min.toLocalDate()));
        filterLower.setOnAction(e -> filterLowerWeek());
        filterHigher.setOnAction(e -> filterHigherWeek());
    }

    public static LocalDate mini;

    public void filterHigherMonth() {
        referenceFrame.setText(String.valueOf(LocalDate.parse(referenceFrame.getText()).plusMonths(1)));
        ObservableList tempItems = FXCollections.observableArrayList();
        for (Appointment a : tempAppointments){
            if ( a.getDateStart().isEqual(LocalDate.parse(referenceFrame.getText()).atStartOfDay())  || (a.getDateStart().isBefore(LocalDate.parse(referenceFrame.getText()).plusMonths(1).atStartOfDay()) && a.getDateStart().isAfter(LocalDate.parse(referenceFrame.getText()).atStartOfDay()))){
                tempItems.add(a);
            }
        }
        if (tempItems.size() == 0){
            aTableView.setItems(null);
        } else {
            aTableView.setItems(tempItems);
        }
    }

    public void filterLowerMonth() {
        referenceFrame.setText(String.valueOf(LocalDate.parse(referenceFrame.getText()).plusMonths(-1)));
        ObservableList tempItems = FXCollections.observableArrayList();
        for (Appointment a : tempAppointments){
            if ( a.getDateStart().isEqual(LocalDate.parse(referenceFrame.getText()).atStartOfDay()) || (a.getDateStart().isBefore(LocalDate.parse(referenceFrame.getText()).plusMonths(1).atStartOfDay()) && a.getDateStart().isAfter(LocalDate.parse(referenceFrame.getText()).atStartOfDay()))){
                tempItems.add(a);
            }
        }
        if (tempItems.size() == 0){
            aTableView.setItems(null);
        } else {
            aTableView.setItems(tempItems);
        }
    }
    public void filterHigherWeek() {
        referenceFrame.setText(String.valueOf(LocalDate.parse(referenceFrame.getText()).plusDays(7)));
        ObservableList tempItems = FXCollections.observableArrayList();
        for (Appointment a : tempAppointments){
            if ( a.getDateStart().isEqual(LocalDate.parse(referenceFrame.getText()).atStartOfDay()) || (a.getDateStart().isBefore(LocalDate.parse(referenceFrame.getText()).plusDays(7).atStartOfDay()) && a.getDateStart().isAfter(LocalDate.parse(referenceFrame.getText()).atStartOfDay()))){
                tempItems.add(a);
            }
        }
        if (tempItems.size() == 0){
            aTableView.setItems(null);
        } else {
            aTableView.setItems(tempItems);
        }
    }
    public void filterLowerWeek() {
        referenceFrame.setText(String.valueOf(LocalDate.parse(referenceFrame.getText()).plusDays(-7)));
        ObservableList tempItems = FXCollections.observableArrayList();
        for (Appointment a : tempAppointments){
            if ( a.getDateStart().isEqual(LocalDate.parse(referenceFrame.getText()).atStartOfDay()) || (a.getDateStart().isAfter(LocalDate.parse(referenceFrame.getText()).atStartOfDay()) && a.getDateStart().isBefore(LocalDate.parse(referenceFrame.getText()).plusDays(7).atStartOfDay()))){
                tempItems.add(a);
            }
        }
        if (tempItems.size() == 0){
            aTableView.setItems(null);
        } else {
            aTableView.setItems(tempItems);
        }
    }

    public void clearFilter(ActionEvent actionEvent) {
        weeklyFilter.setSelected(false);
        monthlyFilter.setSelected(false);
        aTableView.setItems(tempAppointments);
        referenceFrame.setText("All");
        filterLower.setDisable(true);
        filterHigher.setDisable(true);
    }

}
