package DBAccess;

import Model.Appointment;
import com.example.appointmentmanager.AppointmentsController;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * A class to perform several operations related to retrieving appointments.
 */
public class Appointments {

    /**
     * A function that records all appointments in the database and returns an observable list.
     * @return Returns an observable list to store all appointments within the database.
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {

        ObservableList<Appointment> alist = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * from appointments";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
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
                alist.add(c);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return alist;
    }

    /**
     * A function called during Appointment object creation to translate customerID to a string of the customer name.
     * @param customerID Integer of the customer ID.
     * @return Returns a string of the customer name.
     */
    public static String lookupAppointmentCustomer(Integer customerID){
        String sql = null;
        try {
            sql = String.format("SELECT Customer_Name from customers WHERE Customer_ID='%d';", customerID);

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                sql = rs.getString("Customer_Name");

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return(sql);
    }

    /**
     * A function to add an appointment to the database without having created the Appointment object already.
     * @param ID An Appointment ID
     * @param title An Appointment Title
     * @param description An Appointment Description
     * @param location An Appointment Location
     * @param contactID An Appointment Contact ID
     * @param type An Appointment Type
     * @param dateStart An Appointment Start Date and Time
     * @param dateEnd An Appointment End Date and Time
     * @param customerID An Appointment Customer ID
     * @param userID An Appointment User ID
     * @throws SQLException
     */
    public static void addAppointment(Integer ID, String title, String description, String location, int contactID, String type, LocalDateTime dateStart, LocalDateTime dateEnd, int customerID, int userID) throws SQLException { //, dateStart.atZone(ZoneId.ofOffset("UTC", UTC)), dateEnd.atZone(ZoneId.ofOffset("UTC", UTC))
        String sql = String.format("INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES (%d, '%s', '%s', '%s', '%s', ?, ?, %d, %d, %d);", ID, title, description, location, type, customerID, userID, contactID);
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ZonedDateTime dateS = dateStart.atZone(AppointmentsController.clientTimeZone);
        ZonedDateTime dateE = dateEnd.atZone(AppointmentsController.clientTimeZone);
        ps.setTimestamp(1, Timestamp.valueOf(dateS.toLocalDateTime()));
        ps.setTimestamp(2, Timestamp.valueOf(dateE.toLocalDateTime()));
        ps.executeUpdate();
    }

    /**
     * A function to add an appointment to the database with already having created the Appointment object.
     * @param newAppointment A new appointment object to add to the database.
     * @throws SQLException
     */
    public static void addAppointment(Appointment newAppointment) throws SQLException {
        Integer ID = newAppointment.getAppointmentID();
        String title = newAppointment.getTitle();
        String description = newAppointment.getDescription();
        String location = newAppointment.getLocation();
        int contactID = newAppointment.getContactID();
        String type = newAppointment.getType();

        LocalDateTime dateStart = newAppointment.getDateStart();
        LocalDateTime dateEnd = newAppointment.getDateEnd();

        int customerID = newAppointment.getCustomerID();
        int userID = newAppointment.getUserID();
        String sql = String.format("INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES (%d, '%s', '%s', '%s', '%s', ?, ?, %d, %d, %d);", ID, title, description, location, type, customerID, userID, contactID);
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setTimestamp(1, Timestamp.valueOf(dateStart));
        ps.setTimestamp(2, Timestamp.valueOf(dateEnd));
        ps.executeUpdate();
    }

    /**
     * A function to delete an appointment from the database with already having the appointment object ready to delete.
     * @param selectedAppointment The selected appointment object to delete.
     * @return Returns a boolean whether or not delete is successful. Returns true if delete is successful, false if it failed.
     * @throws SQLException
     */
    public static boolean deleteAppointment(Appointment selectedAppointment) throws SQLException {
        String sql = String.format("DELETE FROM appointments where Appointment_ID='%d';", selectedAppointment.getAppointmentID());
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    }

    /**
     * A function to update the database by removing the old appointment object and adding the new appointment object.
     * @param oldAppointment The old appointment to delete from the database.
     * @param newAppointment The new appointment to add to the database.
     * @throws SQLException
     */
    public static void updateAppointment(Appointment oldAppointment, Appointment newAppointment) throws SQLException {
        Appointments.deleteAppointment(oldAppointment);
        Appointments.addAppointment(newAppointment);
    }
}
