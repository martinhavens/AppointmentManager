package helper;

import Model.Appointment;
import com.example.appointmentmanager.AppointmentsController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Locale;

/**
 * A helper class to provide various time-related queries and variables.
 */
public abstract class AppointmentsTime {

    /**
     * An externally called function to populate a combobox element of the main gui.
     */
    public static ObservableList<String> timeOptions(){
        ObservableList<String> timeList = FXCollections.observableArrayList();

        for (int i = 0; i<24; i++){
            for (int j = 0; j<60; j++){
                if (i < 10){
                    if (j < 10){
                        timeList.add(String.format("0%d:0%d", i, j));
                    } else {
                        timeList.add(String.format("0%d:%d", i, j));
                    }
                } else {
                    if (j < 10) {
                        timeList.add(String.format("%d:0%d", i, j));
                    } else {
                        timeList.add(String.format("%d:%d", i, j));
                    }
                }
            }
        }
        return timeList;
    }

    /**
     * An externally called function that checks whether a newly created Appointment object overlaps with any existing
     * appointments which have the same customer ID.
     * @param a A newly created appointment to be checked for overlaps.
     * @return A boolean where false indicates no overlap found, true indicates an overlap is found.
     * @throws SQLException
     */
    public static boolean isOverlapping(Appointment a) throws SQLException {
        String sql = String.format("SELECT * FROM Appointments WHERE Customer_ID = '%d';", a.getCustomerID());
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            if (rs.getInt("Appointment_ID") == a.getAppointmentID()){
                continue;
            }

            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();

            if ((start.isBefore(a.getDateEnd()) && a.getDateStart().isBefore(end))) {
                return true;
            }
        }
        return false;
    }

    /**
     * An integer userID to check against appointments currently in the database to be within 15 minutes of the
     * user's computer system's current time.
     * @param userID An integer userID to check against the current appointments.
     * @return Returns a list of appointments within 15 minutes. An empty list means there are no upcoming appointments.
     * @throws SQLException
     */
    public static HashMap<Integer, String> checkForAppointmentsOnLogin(Integer userID) throws SQLException {
        HashMap<Integer, String> alist = new HashMap<>();
        String sql = String.format("SELECT * FROM Appointments WHERE User_ID = '%d';", userID);
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            if (LocalDateTime.now().toLocalDate().isEqual(rs.getTimestamp("Start").toLocalDateTime().toLocalDate()) && rs.getTimestamp("Start").toLocalDateTime().atZone(AppointmentsController.clientTimeZone).toLocalTime().until(LocalDateTime.now().atZone(AppointmentsController.clientTimeZone).toLocalTime(), ChronoUnit.MINUTES) >= -15 && rs.getTimestamp("Start").toLocalDateTime().atZone(AppointmentsController.clientTimeZone).toLocalTime().until(LocalDateTime.now().atZone(AppointmentsController.clientTimeZone).toLocalTime(), ChronoUnit.MINUTES) <= 0){
                alist.put(rs.getInt("Appointment_ID"), (rs.getTimestamp("Start").toLocalDateTime().atZone(AppointmentsController.clientTimeZone).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm", Locale.ENGLISH))));
            }
        }
        return alist;
    }

}
