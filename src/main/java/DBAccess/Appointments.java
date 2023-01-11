package DBAccess;

import Model.Appointment;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class Appointments {

    public static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

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
                String dateStart = rs.getString("Start");
                String dateEnd = rs.getString("End");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                Appointment c = new Appointment(app_id, title, desc, location, contactID, type, dateStart, dateEnd, customerID, userID);
                allAppointments.add(c);
                alist.add(c);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return alist;
    }

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

    public static void addAppointment(Integer ID, String title, String description, String location, int contactID, String type, String dateStart, String dateEnd, int customerID, int userID) throws SQLException {
        String sql = String.format("INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s', %d, %d, %d);", ID, title, description, location, type, dateStart, dateEnd, customerID, userID, contactID);
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        int rowsAffected = ps.executeUpdate();
    }

    public static void addAppointment(Appointment newAppointment) throws SQLException {
        Integer ID = newAppointment.getAppointmentID();
        String title = newAppointment.getTitle();
        String description = newAppointment.getDescription();
        String location = newAppointment.getLocation();
        int contactID = newAppointment.getContactID();
        String type = newAppointment.getType();
        String dateStart = newAppointment.getDateStart();
        String dateEnd = newAppointment.getDateEnd();
        int customerID = newAppointment.getCustomerID();
        int userID = newAppointment.getUserID();
        String sql = String.format("INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s', %d, %d, %d);", ID, title, description, location, type, dateStart, dateEnd, customerID, userID, contactID);
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        int rowsAffected = ps.executeUpdate();
    }

    public static boolean deleteAppointment(Appointment selectedAppointment) throws SQLException {
        String sql = String.format("DELETE FROM appointments where Appointment_ID='%d';", selectedAppointment.getAppointmentID());
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    }

    public static void updateAppointment(Appointment oldAppointment, Appointment newAppointment) throws SQLException {
        Appointments.deleteAppointment(oldAppointment);
        Appointments.addAppointment(newAppointment);
    }

    //public static int insertAppointment()

}
