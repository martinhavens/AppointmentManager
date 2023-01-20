package com.example.appointmentmanager;
import DBAccess.Appointments;
import helper.*;
import DBAccess.Contacts;
import DBAccess.Customers;
import Model.Appointment;
import Model.Contact;
import Model.Customer;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * The controller class of the main gui form of ../resources/appointments.fxml
 */
public class AppointmentsController implements Initializable {

    /** a ZoneId of the current user's computer environment used for LocalDateTime operations **/
    public static ZoneId clientTimeZone = ZoneId.of(Calendar.getInstance().getTimeZone().getID());
    /** a ZoneId of the appointment office used for LocalDateTime operations to Eastern Time**/
    public static ZoneId clinicTimeZone = ZoneId.of("America/New_York");
    /** An Integer for performing checks against the database for upcoming appointments for the user **/
    public static Integer userID;
    /** an Integer to track the current tableview index being modified across different method calls and scopes **/
    Integer selectedIndex;
    /** an observable list to track all the appointments and maintain a tableview **/
    ObservableList<Appointment> tempAppointments = Appointments.getAllAppointments();
    /** an observable list to track all the customers and maintain a tableview **/
    ObservableList<Customer> tempCustomers = Customers.getAllCustomers();
    /** an observable list to contain divisions while filtering occurs from country selection **/
    ObservableList<String> divisions = FXCollections.observableArrayList();

    /** a Button element of the gui implementation **/
    public TextField aAID;
    /** a Button element of the gui implementation **/
    public Button saveAppointment;
    /** a Button element of the gui implementation **/
    public Button cancelAppointment;
    /** a Button element of the gui implementation **/
    public Button saveCustomer;
    /** a Button element of the gui implementation **/
    public Button cancelCustomer;
    /** a TextField element of the gui implementation **/
    public TextField cCID;
    /** a TextField element of the gui implementation **/
    public TextField cName;
    /** a TextField element of the gui implementation **/
    public TextField cAddress;
    /** a TextField element of the gui implementation **/
    public TextField cPostal;
    /** a TextField element of the gui implementation **/
    public TextField cPhone;
    /** a ComboBox element of the gui implementation **/
    public ComboBox cDivision;
    /** a ComboBox element of the gui implementation **/
    @FXML
    public ComboBox cCountry;
    /** a Button element of the gui implementation **/
    public Button modifyCustomer;
    /** a Button element of the gui implementation **/
    public Button modifyAppointment;
    /** a Button element of the gui implementation **/
    public Button addAppointment;
    /** a Button element of the gui implementation **/
    public Button addCustomer;
    /** a Button element of the gui implementation **/
    public Button deleteAppointment;
    /** a Button element of the gui implementation **/
    public Button deleteCustomer;
    /** a DatePicker element of the gui implementation **/
    public DatePicker endDatePick;
    /** a DatePicker element of the gui implementation **/
    public DatePicker startDatePick;
    /** a ComboBox element of the gui implementation **/
    public ComboBox startTimePick;
    /** a ComboBox element of the gui implementation **/
    public ComboBox endTimePick;
    /** a RadioButton element of the gui implementation **/
    public RadioButton monthlyFilter;
    /** a RadioButton element of the gui implementation **/
    public RadioButton weeklyFilter;
    /** a Button element of the gui implementation **/
    public Button filterHigher;
    /** a Button element of the gui implementation **/
    public Button filterLower;
    /** a ToggleGroup element of the gui implementation **/
    public ToggleGroup filterGroup;
    /** a Label element of the gui implementation **/
    public Label referenceFrame;
    /** a Label element of the gui implementation **/
    public Label currentTimeZoneLabel;
    /** a Label element of the gui implementation **/
    public Label clinicTimeZoneLabel;
    /** a TableView element of the gui implementation **/
    public TableView<Appointment> aTableView;
    /** a TableView element of the gui implementation **/
    public TableView<Customer> cTableView;
    /** a TableColumn element of the gui implementation **/
    public TableColumn<Appointment, Integer> aTID;
    /** a TableColumn element of the gui implementation **/
    public TableColumn<Appointment, String>  aTTitle;
    /** a TableColumn element of the gui implementation **/
    public TableColumn<Appointment, String>  aTDesc;
    /** a TableColumn element of the gui implementation **/
    public TableColumn<Appointment, String>  aTLocation;
    /** a TableColumn element of the gui implementation **/
    public TableColumn<Contact, String>  aTContactID;
    /** a TableColumn element of the gui implementation **/
    public TableColumn<Appointment, String>  aTType;
    /** a TableColumn element of the gui implementation **/
    public TableColumn<Appointment, String>  aTStart;
    /** a TableColumn element of the gui implementation **/
    public TableColumn<Appointment, String>  aTEnd;
    /** a TableColumn element of the gui implementation **/
    public TableColumn<Appointment, Integer>  aTCID;
    /** a TableColumn element of the gui implementation **/
    public TableColumn<Appointment, Integer>  atUID;
    /** a TableColumn element of the gui implementation **/
    public TableColumn<Customer, Integer> cTID;
    /** a TableColumn element of the gui implementation **/
    public TableColumn<Customer, String> cTName;
    /** a TableColumn element of the gui implementation **/
    public TableColumn<Customer, String> cTAddress;
    /** a TableColumn element of the gui implementation **/
    public TableColumn<Customer, String> cTPostalCode;
    /** a TableColumn element of the gui implementation **/
    public TableColumn<Customer, String> cTPhoneNumber;
    /** a TableColumn element of the gui implementation **/
    public TableColumn<Customer, Integer> cTDivision;
    /** a TableColumn element of the gui implementation **/
    public TableColumn cTCountry;
    /** a TextField element of the gui implementation **/
    public TextField aTitle;
    /** a TextField element of the gui implementation **/
    public TextField aDescription;
    /** a TextField element of the gui implementation **/
    public TextField aLocation;
    /** a ComboBox element of the gui implementation **/
    public ComboBox<String> aContact;
    /** a TextField element of the gui implementation **/
    public TextField aType;
    /** a TextField element of the gui implementation **/
    public TextField aCID;
    /** a TextField element of the gui implementation **/
    public TextField aUserID;
    /** a Label element of the gui implementation **/
    public Label dynamicLabel;
    /** a ComboBox element of the gui implementation **/
    public ComboBox contactScheduleComboBox;
    /** a TableView element of the gui implementation **/
    public TableView monthlyReportTableView;
    /** a TableColumn element of the gui implementation **/
    public TableColumn mMonth;
    /** a TableColumn element of the gui implementation **/
    public TableColumn mType;
    /** a TableColumn element of the gui implementation **/
    public TableColumn mNumber;

    public AppointmentsController() throws SQLException {
    }

    /**
     * Initialize tableviews and enables or disables the relevant main form objects.
     * Lambda with listeners to enable modify and delete buttons when a tableview element is selected.
     * @param url is a default javafx parameter
     * @param resourceBundle is a default javafx parameter
     */
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
        currentTimeZoneLabel.setText(String.valueOf(clientTimeZone));
        clinicTimeZoneLabel.setText(String.valueOf(clinicTimeZone));

        try {
            aTableView.setItems(Appointments.getAllAppointments());
            cTableView.setItems(Customers.getAllCustomers());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        contactScheduleComboBox.setItems(Contacts.getAllContactNames());

        appointmentDisable();
        customerDisable();
        filterLower.setDisable(true);
        filterHigher.setDisable(true);
        modifyCustomer.setDisable(true);
        deleteCustomer.setDisable(true);
        modifyAppointment.setDisable(true);
        deleteAppointment.setDisable(true);

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

        try {
            tempCustomers = Customers.getAllCustomers();
            tempAppointments = Appointments.getAllAppointments();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

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

        try {
            Customers.getAllDivisions();
            Customers.getAllCountries();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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

    /**
     * A button onAction function that populates and enables all relevant gui field to modify a customer.
     * And changes the GUI dynamic text.
     * @throws SQLException
     */
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

    /**
     * A helper function to enable all relevant customer fields
     */
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

    /**
     * A helper function to disable all relevant customer fields
     */
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

    /**
     * Enables the relevant customer fields, determines the new customer ID, and changes the GUI dynamic text.
     * @throws SQLException
     */
    public void addCustomer() throws SQLException {
        addAppointment.setDisable(true);
        customerDisable();
        customerEnable();
        int cid_c = 0;
        int max = 0;
        int N = tempCustomers.size();
        if (N == 0) {
            cid_c = 1;
        } else {
            for (int i = 0; i < N; i++) {
                Customer tempCustomer = tempCustomers.get(i);
                if (tempCustomer.getCustomerID() > max) {
                    max = tempCustomer.getCustomerID();
                }
            }

            int[] tempA = new int[max];
            for (int i = 0; i < max; i++) {
                tempA[i] = 0;
            }
            for (int i = 0; i < N; i++) {
                tempA[tempCustomers.get(i).getCustomerID() - 1] = 1;
            }
            boolean set = true;
            for (int i = 0; i < max; i++) {
                if (tempA[i] == 0){
                    cid_c = i + 1;
                    set = false;
                    break;
                }
            }
            if (set){
                cid_c = max + 1;
            }
        }
        cCID.setText(Integer.toString(cid_c));
        dynamicLabel.setText("Adding a Customer:");
        cCountry.setItems(Customers.getAllCountries());
        cDivision.setItems(Customers.getAllDivisions());
        cTableView.getSelectionModel().clearSelection();
        modifyCustomer.setDisable(true);
        deleteCustomer.setDisable(true);
    }

    /**
     * A button onAction function call which validates customer field inputs,
     * creates or modifies a customer depending on the dynamic label text, and adds to or updates the tableview.
     * @throws SQLException
     */
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
        addAppointment.setDisable(false);
    }

    /**
     * A button onAction call that disables all relevant fields, and resets the GUI dynamic label text to default.
     * @throws SQLException
     */
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

    /**
     * A button onAction event that verifies a tableview object is selection, calls a deleteCustomer helper
     * function externally, and displays the result of the deletion, before updating the tableview if needed.
     * @throws SQLException
     */
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

    /**
     * A combobox onAction function call that filters the division combobox items based on whichever country
     * is selected from the country combobox.
     * @throws SQLException
     */
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

    /**
     * A button onAction function that populates and enables all relevant gui field to modify an appointment.
     * And changes the GUI dynamic text.
     */
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

    /**
     * A helper function to enable all relevant appointment fields
     */
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

    /**
     * A helper function to disable all relevant appointment fields
     */
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

    /**
     * Enables the relevant appointment fields, determines the new appointment ID, and changes the GUI dynamic text.
     */
    public void addAppointment() {
        addCustomer.setDisable(true);
        appointmentDisable();
        appointmentEnable();
        int max = 0;
        int aid_c = 0;
        int N = tempAppointments.size();
        if (N == 0){
            aid_c = 1;
        } else {
            for (int i = 0; i < N; i++) {
                Appointment tempAppointment = tempAppointments.get(i);
                if (tempAppointment.getAppointmentID() > max) {
                    max = tempAppointment.getAppointmentID();
                }
            }

            int[] tempA = new int[max];
            for (int i = 0; i < max; i++) {
                tempA[i] = 0;
            }
            for (int i = 0; i < N; i++) {
                tempA[tempAppointments.get(i).getAppointmentID() - 1] = 1;
            }
            boolean set = true;
            for (int i = 0; i < max; i++) {
                if (tempA[i] == 0){
                    aid_c = i + 1;
                    set = false;
                    break;
                }
            }
            if (set){
                aid_c = max + 1;
            }
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

    /**
     * A button onAction function call which validates appointment field inputs,
     * creates or modifies a appointment depending on the dynamic label text, and adds to or updates the tableview.
     * @throws SQLException
     */
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

        try { customerID = Integer.parseInt(aCID.getText()); }
        catch (NumberFormatException nfe) {
            AlertBox.display("Error Message", "Customer ID is Invalid!"); return; }
        boolean cIDVALID = false;
        for (Customer c : tempCustomers) {
            if (c.getCustomerID() == Integer.parseInt(aCID.getText())) {
                cIDVALID = true;
            }
        }
        if (!cIDVALID) {
            AlertBox.display("Error Message", "Enter a valid customer ID from the table above!"); return; }



        dateStart = LocalDateTime.of(startDatePick.getValue(), LocalTime.parse(startTimePick.getValue().toString())).atZone(clientTimeZone);
        dateEnd = LocalDateTime.of(endDatePick.getValue(), LocalTime.parse(endTimePick.getValue().toString())).atZone(clientTimeZone);

        if (dateStart.getDayOfWeek() == java.time.DayOfWeek.of(6) || dateStart.getDayOfWeek() == java.time.DayOfWeek.of(7)){
            AlertBox.display("Error Message", "The office does not accept weekend appointments!"); return; }
        if (dateEnd.getDayOfWeek() == java.time.DayOfWeek.of(6) || dateEnd.getDayOfWeek() == java.time.DayOfWeek.of(7)){
            AlertBox.display("Error Message", "The office does not accept weekend appointments!"); return; }
        if (dateStart.withZoneSameInstant(clinicTimeZone).getHour() < 8 || dateStart.withZoneSameInstant(clinicTimeZone).getHour() > 22){
            AlertBox.display("Error Message", "The office doesn't schedule outside of 8AM-5PM EST!"); return; }
        if (dateStart.isAfter(dateEnd) || dateStart.isEqual(dateEnd)){
            AlertBox.display("Error Message", "End must be after the Start"); return; }

        try { userID = Integer.parseInt(aUserID.getText()); }
            catch (NumberFormatException nfe) {
                AlertBox.display("Error Message", "User ID is Invalid!"); return; }

        String sql = String.format("SELECT * FROM users WHERE User_ID ='%d';", userID);
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){ } else {
            AlertBox.display("Error Message", "Enter a valid User ID!"); return; }

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

    /**
     * A button onAction call that disables all relevant fields, and resets the GUI dynamic label text to default.
     */
    public void cancelAppointment() {
        addCustomer.setDisable(false);
        appointmentDisable();
        modifyAppointment.setDisable(true);
        deleteAppointment.setDisable(true);
        aTableView.getSelectionModel().clearSelection();
    }

    /**
     * A button onAction event that verifies a tableview object is selection, calls a deleteAppointment helper
     * function externally, and displays the result of the deletion, before updating the tableview if needed.
     * @throws SQLException
     */
    public void deleteAppointment() throws SQLException {
            if (!aTableView.getSelectionModel().isEmpty()) {
                    if (Appointments.deleteAppointment(Appointments.getAllAppointments().get(aTableView.getSelectionModel().getSelectedIndex()))){
                        aTableView.getSelectionModel().clearSelection();
                        AlertBox.display("Alert", "Appointment was successfully deleted.");
                    }
                    else {
                        AlertBox.display("Alert", "Appointment could not be deleted.");
                    }
                }
            else {
                AlertBox.display("Error Message", "No Appointment was Selected.");
            }
            aTableView.setItems(Appointments.getAllAppointments());
            tempAppointments = Appointments.getAllAppointments();
    }

    /**
     * A radiobutton onAction function that enables the relevant GUI elements, determines the appointment current in the
     * database with the earliest calendar date, and sets the initial appointment filter from the earliest appointment
     * date to one month in the future. And sets the appropriate function calls to the filter advancement buttons via
     * lambda functions.
     * @throws SQLException
     */
    public void appointmentsMonthly() throws SQLException {
        customerDisable();
        appointmentDisable();
        tempAppointments = Appointments.getAllAppointments();
        if (tempAppointments.isEmpty()){
            AlertBox.display("Alert!", "There are no appointments to filter.");
            monthlyFilter.setSelected(false);
            return;
        }
        filterLower.setDisable(false);
        filterHigher.setDisable(false);
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

    /**
     * A radiobutton onAction function that enables the relevant GUI elements, determines the appointment current in the
     * database with the earliest calendar date, and sets the initial appointment filter from the earliest appointment
     * date to one week in the future. And sets the appropriate function calls to the filter advancement buttons via
     * lambda functions.
     * @throws SQLException
     */
    public void appointmentsWeekly() throws SQLException {
        customerDisable();
        appointmentDisable();
        tempAppointments = Appointments.getAllAppointments();
        if (tempAppointments.isEmpty()){
            AlertBox.display("Alert!", "There are no appointments to filter.");
            weeklyFilter.setSelected(false);
            return;
        }
        filterLower.setDisable(false);
        filterHigher.setDisable(false);
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

    /**
     * A button onAction function that gets the current filter reference date and advances the filter forward one month,
     * afterwards updating the tableview.
     */
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

    /**
     * A button onAction function that gets the current filter reference date and advances the filter backwards one month,
     * afterwards updating the tableview.
     */
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

    /**
     * A button onAction function that gets the current filter reference date and advances the filter forwards one week,
     * afterwards updating the tableview.
     */
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

    /**
     * A button onAction function that gets the current filter reference date and advances the filter forward one week,
     * afterwards updating the tableview.
     */
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

    /**
     * A button onAcion function that clears the filter reference date, and resets the tableview to show all appointments.
     */
    public void clearFilter() {
        weeklyFilter.setSelected(false);
        monthlyFilter.setSelected(false);
        aTableView.setItems(tempAppointments);
        referenceFrame.setText("All");
        filterLower.setDisable(true);
        filterHigher.setDisable(true);
    }

    /**
     * A button onAction function that creates a new window of relevant appointments for the contact selected by in
     * the contactScheduleComboBox gui element.
     * @throws SQLException
     * @throws IOException
     */
    public void viewContactSchedule() throws SQLException, IOException {
        if (contactScheduleComboBox.getSelectionModel().isEmpty()){
            AlertBox.display("Error!", "No contact has been selected!");
            return;
        }
        String tempContact = (String) contactScheduleComboBox.getSelectionModel().getSelectedItem();
        String sql = String.format("SELECT * from appointments WHERE Contact_ID='%d';", Contacts.reverseContactDictionary.get(tempContact));
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        ObservableList tempList = FXCollections.observableArrayList();
        while (rs.next()) {
            Integer app_id = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String desc = rs.getString("Description");
            String location = rs.getString("Location");
            int contactID = rs.getInt("Contact_ID");
            String type = rs.getString("Type");
            LocalDateTime dateStart = rs.getTimestamp("Start").toLocalDateTime().atZone(AppointmentsController.clientTimeZone).toLocalDateTime();
            LocalDateTime dateEnd = rs.getTimestamp("End").toLocalDateTime().atZone(AppointmentsController.clientTimeZone).toLocalDateTime();
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            Appointment c = new Appointment(app_id, title, desc, location, contactID, type, dateStart, dateEnd, customerID, userID);
            tempList.add(c);
        }
        contactController.contactAppointments = tempList;
        Parent root;
        root = FXMLLoader.load(HelloApplication.class.getResource("contact-view.fxml"));
        Stage stage = new Stage();
        stage.setTitle(String.format("Viewing Schedule for %s", tempContact));
        stage.setScene(new Scene(root, 721, 336));
        stage.show();
    }

    /**
     * A button onAction function that refreshes the items on the monthly-report tableview items, located at bottom right of the gui.
     * @throws SQLException
     */
    public void refreshMonthlyReport() throws SQLException {
        Monthly.numberOfMap.clear();
        for (String s : Monthly.numberOfMap.keySet()){
            Monthly.numberOfMap.put(s, 0);
        }
        ObservableList tempList = FXCollections.observableArrayList();

        for (Appointment a : Appointments.getAllAppointments()){
            String month = a.getDateStart().getMonth().toString();
            String type = a.getType();
            if (Monthly.numberOfMap.get(month+type) == null){
                tempList.add(new Monthly(month, type));
            }
            else {
                new Monthly(month, type);
            }
        }
        if (tempList.isEmpty()){
            AlertBox.display("Alert!", "There are no appointments.");
            monthlyReportTableView.setItems(null);
            return;
        } else {
            mMonth.setCellValueFactory(new PropertyValueFactory<>("month"));
            mType.setCellValueFactory(new PropertyValueFactory<>("type"));
            mNumber.setCellValueFactory(new PropertyValueFactory<>("numberOf"));
            monthlyReportTableView.setItems(tempList);
        }
    }

    /**
     * A function that adds the number of customers with common countries to set as the items for a new tableview
     * in a separate window.
     * @throws IOException
     * @throws SQLException
     */
    public void viewCountryReport( ) throws IOException, SQLException {
        ObservableList tempList = FXCollections.observableArrayList();
        for (Customer c : Customers.getAllCustomers()){
            String country = c.getCountry();
            if (CountryCount.numberOfMap.get(country) == null){
                tempList.add(new CountryCount(country));
            }
            else {
                new CountryCount(country);
            }
        }
        if (tempList.isEmpty()){
            AlertBox.display("Alert!", "There are no appointments.");
            return;
        } else {
            countryController.countryCount = tempList;
            Parent root;
            root = FXMLLoader.load(HelloApplication.class.getResource("country-view.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Report");
            stage.setScene(new Scene(root, 222, 286));
            stage.show();
            CountryCount.numberOfMap.clear();
        }

    }
}
