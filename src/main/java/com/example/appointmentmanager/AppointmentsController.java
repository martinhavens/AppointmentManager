package com.example.appointmentmanager;
import DBAccess.Appointments;
import helper.AppointmentsTime;
import DBAccess.Contacts;
import DBAccess.Customers;
import Model.Appointment;
import Model.Contact;
import Model.Customer;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import helper.AlertBox;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.ServerSocket;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.util.Calendar;
import java.util.HashMap;
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
    public static ZoneId clientTimeZone = ZoneId.of(Calendar.getInstance().getTimeZone().getID()); // Eastern Standard Time
    public static ZoneId clinicTimeZone = ZoneId.of("America/New_York"); // Eastern Standard Time
    public Label currentTimeZoneLabel;
    public Label clinicTimeZoneLabel;
    public static Integer userID;
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

        currentTimeZoneLabel.setText(String.valueOf(clientTimeZone));
        clinicTimeZoneLabel.setText(String.valueOf(clinicTimeZone));
        try {
            StringBuilder alert = new StringBuilder();
            HashMap loginList = AppointmentsTime.checkForAppointmentsOnLogin(userID);
            if (loginList.size() > 0){
                loginList.keySet();
                for (Object s : loginList.keySet()){
                        alert.append(String.format("Appointment ID %d at %s !  ", s ,loginList.get(s)));
                }
                AlertBox.display("Alert!", String.format("Appointments within 15 minutes are:  %s", alert));
            } else if (loginList.size() == 0){
                AlertBox.display("Alert!", "Found no appointments for user within 15 minutes.");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void modifyCustomer() throws SQLException {
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
    public void addCustomer() throws SQLException {
        addAppointment.setDisable(true);
        customerDisable();
        customerEnable();
        int cid_c;
        int N = tempCustomers.size();
        if (N == 0) {
            cid_c = 1;
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
        }
        cCID.setText(Integer.toString(cid_c));
        dynamicLabel.setText("Adding a Customer:");
        cCountry.setItems(Customers.getAllCountries());
        cDivision.setItems(Customers.getAllDivisions());
        cTableView.getSelectionModel().clearSelection();
        modifyCustomer.setDisable(true);
        deleteCustomer.setDisable(true);
    }
    public void createCustomer() throws SQLException {
        Integer ID;
        String name;
        String address;
        String phone;
        String postal;
        String division;

        if (cName.getText().isEmpty()) {
            AlertBox.display("Error Message", "Name is empty!"); return; }
        if (cAddress.getText().isEmpty()) {
            AlertBox.display("Error Message", "Address is empty!"); return; }
        if (cPhone.getText().isEmpty()) {
            AlertBox.display("Error Message", "Phone Number is empty!");return; }
        if (cPostal.getText().isEmpty()){
            AlertBox.display("Error Message", "Postal Code is empty!"); return; }
        if (cCountry.getValue() == null){
            AlertBox.display("Error Message", "Country is not selected!"); return; }
        if (cDivision.getValue() == null){
            AlertBox.display("Error Message", "Division is not selected!"); return; }

        try { ID = Integer.parseInt(cCID.getText()); }
            catch (NumberFormatException nfe) {
                AlertBox.display("Error Message", "Customer ID is Invalid!"); return; }
        try { name = cName.getText(); }
            catch (NumberFormatException nfe) {
                AlertBox.display("Error Message", "Name is Invalid!"); return; }
        try { address = cAddress.getText(); }
            catch (NumberFormatException nfe) {
                    AlertBox.display("Error Message", "Address is Invalid!"); return; }
        try { phone = cPhone.getText(); }
            catch (NumberFormatException nfe) {
                AlertBox.display("Error Message", "Phone is Invalid!"); return; }
        try { division = (String)cDivision.getSelectionModel().getSelectedItem(); }
            catch (NumberFormatException nfe) {
            AlertBox.display("Error Message", "Division is Invalid!"); return; }
        try { postal = cPostal.getText(); }
            catch (NumberFormatException nfe) {
                AlertBox.display("Error Message", "Postal Code is Invalid!"); return; }

        Customer c = null;
        if (dynamicLabel.getText().equals("Adding a Customer:")){
            try {
                c = new Customer(ID, name, address, phone, postal, Customers.divisionsMap.get(division));
                Customers.addCustomer(ID, name, address, phone, postal, Customers.divisionsMap.get(division));
            } catch (Exception e) {
                assert c != null;
                Customers.deleteCustomer(c);
                e.printStackTrace();
                return;
            }
        } else if (dynamicLabel.getText().equals("Modifying Customer:")){
            try {
                c = new Customer(ID, name, address, phone, postal, Customers.divisionsMap.get(division));
                Customers.updateCustomer(cTableView.getItems().get(selectedIndex), c);
            } catch (Exception e) {
                e.printStackTrace();
                assert c != null;
                Customers.deleteCustomer(c);
                return;
            }
        }
        cTableView.setItems(Customers.getAllCustomers());
        tempCustomers = Customers.getAllCustomers();
        customerDisable();
        dynamicLabel.setText("Select an Option:");
    }
    public void cancelCustomer() throws SQLException {
        addAppointment.setDisable(false);
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
    public void deleteCustomer() throws SQLException {
        if (!cTableView.getSelectionModel().isEmpty()) {
            if (Customers.deleteCustomer(Customers.getAllCustomers().get(cTableView.getSelectionModel().getSelectedIndex()))){
                AlertBox.display("Alert", "Customer was successfully deleted.");
            } else { AlertBox.display("Alert", "Customer could not be deleted while having appointments."); }
        } else { AlertBox.display("Error Message", "No Customer was Selected."); }
        cTableView.getSelectionModel().clearSelection();
        modifyCustomer.setDisable(true);
        deleteCustomer.setDisable(true);
        cTableView.setItems(Customers.getAllCustomers());
        tempCustomers = Customers.getAllCustomers();
    }
    public void filterDivisions() throws SQLException {
        divisions.clear();
        Integer countryInt = Customers.countriesMap.get(cCountry.getSelectionModel().getSelectedItem());
        String sql = String.format("SELECT Division from first_level_divisions WHERE COUNTRY_ID ='%d';", countryInt);
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        String divisionName;
        while (rs.next()){
            divisionName = rs.getString("Division");
            if (divisionName.equals("QuÃ©bec")){
                divisionName = "Quebec";
            }
            divisions.add(divisionName);
        }
        cDivision.setItems(divisions);
    }
    public void modifyAppointment() {
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
        startTimePick.setValue(tempA.getDateStart().atZone(clientTimeZone).toLocalTime());
        endTimePick.setValue(tempA.getDateEnd().atZone(clientTimeZone).toLocalTime());
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
        startTimePick.setValue(null);
        endTimePick.setValue(null);
        startTimePick.setDisable(true);
        endTimePick.setDisable(true);
        aType.setText("");
        aTitle.setDisable(true);
        aAID.setDisable(true);
        aDescription.setDisable(true);
        aLocation.setDisable(true);
        aCID.setDisable(true);
        aUserID.setDisable(true);
        aContact.setDisable(true);
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
    public void addAppointment() {
        addCustomer.setDisable(true);
        appointmentDisable();
        appointmentEnable();
        int aid_c;
        int N = tempAppointments.size();
        if (N == 0) {
            aid_c = 1;
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
        }
        aAID.setText(Integer.toString(aid_c));
        dynamicLabel.setText("Adding an Appointment:");
        aContact.setItems(Contacts.getAllContactNames());
        startTimePick.setItems(AppointmentsTime.timeOptions());
        endTimePick.setItems(AppointmentsTime.timeOptions());
        aTableView.getSelectionModel().clearSelection();
        modifyAppointment.setDisable(true);
        deleteAppointment.setDisable(true);
    }
    public void createAppointment() throws SQLException {
        Integer ID;
        String title;
        String description;
        String location;
        int contactID;
        String type;
        ZonedDateTime dateStart;
        ZonedDateTime dateEnd;

        int customerID;
        int userID;

        if (aTitle.getText().isEmpty()) {
            AlertBox.display("Error Message", "Title is empty!");
            return;
        }
        if (aDescription.getText().isEmpty()) {
            AlertBox.display("Error Message", "Description is empty!"); return; }
        if (aLocation.getText().isEmpty()) {
            AlertBox.display("Error Message", "Location is empty!"); return; }
        if (startDatePick.getValue() == null){
            AlertBox.display("Error Message", "Date Start is empty!"); return; }
        if (endDatePick.getValue() == null){
            AlertBox.display("Error Message", "Date End is empty!"); return; }
        if (aCID.getText().isEmpty()) {
            AlertBox.display("Error Message", "Customer ID is empty!"); return; }
        if (aUserID.getText().isEmpty()) {
            AlertBox.display("Error Message", "User ID is empty!"); return; }
        if (aType.getText().isEmpty()) {
            AlertBox.display("Error Message", "Type is empty!"); return; }
        if (aContact.getSelectionModel().isEmpty()) {
            AlertBox.display("Error Message", "No Contact is selected!"); return; }

        try { ID = Integer.parseInt(aAID.getText()); }
            catch (NumberFormatException nfe) {
                AlertBox.display("Error Message", "Appointment ID is Invalid!"); return; }
        try { title = aTitle.getText(); }
            catch (NumberFormatException nfe) {
                AlertBox.display("Error Message", "Title is Invalid!"); return; }
        try { description = aDescription.getText(); }
            catch (NumberFormatException nfe) {
                AlertBox.display("Error Message", "Description is Invalid!"); return; }
        try { location = aLocation.getText(); }
            catch (NumberFormatException nfe) {
                AlertBox.display("Error Message", "Location is Invalid!"); return; }
        try { contactID = Contacts.reverseContactDictionary.get(aContact.getSelectionModel().getSelectedItem()); }
            catch (NumberFormatException nfe) {
                AlertBox.display("Error Message", "Contact ID is Invalid!"); return; }
        try { type = aType.getText(); }
            catch (NumberFormatException nfe) {
                AlertBox.display("Error Message", "Type is Invalid!"); return; }

        dateStart = LocalDateTime.of(startDatePick.getValue(), LocalTime.parse(startTimePick.getValue().toString())).atZone(clientTimeZone);
        dateEnd = LocalDateTime.of(endDatePick.getValue(), LocalTime.parse(endTimePick.getValue().toString())).atZone(clientTimeZone);

        if (dateStart.getDayOfWeek() == java.time.DayOfWeek.of(6) || dateStart.getDayOfWeek() == java.time.DayOfWeek.of(7)){
            AlertBox.display("Error Message", "The office does not accept weekend appointments!"); return; }
        if (dateEnd.getDayOfWeek() == java.time.DayOfWeek.of(6) || dateEnd.getDayOfWeek() == java.time.DayOfWeek.of(7)){
            AlertBox.display("Error Message", "The office does not accept weekend appointments!"); return; }
        if (dateStart.withZoneSameInstant(clinicTimeZone).getHour() < 8 || dateStart.withZoneSameInstant(clinicTimeZone).getHour() > 22){
            AlertBox.display("Error Message", "The office doesn't schedule outside of 8AM-5PM EST!"); return; }

        try { customerID = Integer.parseInt(aCID.getText()); }
            catch (NumberFormatException nfe) {
                AlertBox.display("Error Message", "Customer ID is Invalid!"); return; }
        try { userID = Integer.parseInt(aUserID.getText()); }
            catch (NumberFormatException nfe) {
                AlertBox.display("Error Message", "User ID is Invalid!"); return; }

        Appointment c = null;
        if (dynamicLabel.getText().equals("Adding an Appointment:")){
            try {
                c = new Appointment(ID, title, description, location, contactID, type, dateStart.withZoneSameInstant(clientTimeZone).toLocalDateTime(), dateEnd.withZoneSameInstant(clientTimeZone).toLocalDateTime(), customerID, userID);
                if (!AppointmentsTime.isOverlapping(c)){
                    Appointments.addAppointment(ID, title, description, location, contactID, type, dateStart.withZoneSameInstant(clientTimeZone).toLocalDateTime(), dateEnd.withZoneSameInstant(clientTimeZone).toLocalDateTime(), customerID, userID);
                }
                else {
                    AlertBox.display("Error Message", "Appointments cannot be overlapping!"); return;
                }
            } catch (MysqlDataTruncation e) {
                AlertBox.display("Error Message", "The date must be in the format of 'YYYY-MM-DD HH:MN:SS'. 0 <= YYYY < 10000, 0 < MM < 13, 00 < DD < 31, 00 <= HH < 25, 00 <= MN < 60, 00 <= SS < 60.");
                Appointments.deleteAppointment(c);
                return;
            }
        }
        else if (dynamicLabel.getText().equals("Modifying Appointment:")){
            try {
                c = new Appointment(ID, title, description, location, contactID, type, dateStart.withZoneSameInstant(clientTimeZone).toLocalDateTime(), dateEnd.withZoneSameInstant(clientTimeZone).toLocalDateTime(), customerID, userID);
                if (!AppointmentsTime.isOverlapping(c)){
                    Appointments.updateAppointment(aTableView.getItems().get(selectedIndex), c);
                } else {
                    AlertBox.display("Error Message", "Appointments cannot be overlapping!"); return;
                }
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
    public void cancelAppointment() {
        addCustomer.setDisable(false);
        appointmentDisable();
        modifyAppointment.setDisable(true);
        deleteAppointment.setDisable(true);
        aTableView.getSelectionModel().clearSelection();
    }
    public void deleteAppointment() throws SQLException {
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
    public void appointmentsMonthly() throws SQLException {
        customerDisable();
        appointmentDisable();
        filterLower.setDisable(false);
        filterHigher.setDisable(false);
        tempAppointments = Appointments.getAllAppointments();
        LocalDateTime min = null;
        if (referenceFrame.getText().equals("All")){
            for (Appointment a : tempAppointments){
                if (min == null) {
                    min = a.getDateStart();
                }
                if (a.getDateStart().isBefore(min)){
                    min = a.getDateStart();
                }
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
    public void appointmentsWeekly() throws SQLException {
        customerDisable();
        appointmentDisable();
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
                }
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
    public void clearFilter() {
        weeklyFilter.setSelected(false);
        monthlyFilter.setSelected(false);
        aTableView.setItems(tempAppointments);
        referenceFrame.setText("All");
        filterLower.setDisable(true);
        filterHigher.setDisable(true);
    }
}
