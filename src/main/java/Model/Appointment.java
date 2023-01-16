package Model;

import DBAccess.Appointments;
import DBAccess.Contacts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Appointment {
    Integer appointmentID;
    String title;
    String description;
    String location;
    int contactID;
    String contactName;
    String type;
    String dateStartDate;
    String dateStartTime;
    String dateEndDate;
    String dateEndTime;
    int customerID;
    String customerName;
    int userID;

    public Appointment(Integer appointmentID, String title, String description, String location, int contactID, String type, String date_start_date, String dateStartTime, String date_end_date, String dateEndTime, int customerID, int userID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contactID = contactID;
        this.contactName = Contacts.contactDictionary.get(contactID);
        this.type = type;
        this.dateStartDate = date_start_date;
        this.dateStartTime = dateStartTime;
        this.dateEndDate = date_end_date;
        this.dateEndTime = dateEndTime;
        this.customerID = customerID;
        this.customerName = Appointments.lookupAppointmentCustomer(customerID);
        this.userID = userID;
    }

    public Integer getAppointmentID() {
        return appointmentID;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getLocation() {
        return location;
    }
    public int getContactID() {
        return contactID;
    }
    public String getContactName() {
        return contactName;
    }
    public int getCustomerID() {
        return customerID;
    }
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
    public String getType() {
        return type;
    }

    public String getCustomerName() {
        return customerName;
    }
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    public int getUserID() {
        return userID;
    }
    public void setAppointmentID(Integer appointmentID) {
        this.appointmentID = appointmentID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCustomerName(String name) {
        this.customerName = name;
    }

    public String getDateStartDate() {
        return dateStartDate;
    }

    public void setDateStartDate(String dateStartDate) {
        this.dateStartDate = dateStartDate;
    }

    public String getDateStartTime() {
        return dateStartTime;
    }

    public void setDateStartTime(String dateStartTime) {
        this.dateStartTime = dateStartTime;
    }

    public String getDateEndDate() {
        return dateEndDate;
    }

    public void setDateEndDate(String dateEndDate) {
        this.dateEndDate = dateEndDate;
    }

    public String getDateEndTime() {
        return dateEndTime;
    }

    public void setDateEndTime(String dateEndTime) {
        this.dateEndTime = dateEndTime;
    }


    public void setUserID(int userID) {
        this.userID = userID;
    }
//    public void addAppointment(){
//        allAppointments.add()
//    }
}
