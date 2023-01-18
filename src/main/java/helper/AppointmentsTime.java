package helper;

import Model.Appointment;
import com.example.appointmentmanager.AppointmentsController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public abstract class AppointmentsTime {

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
            if (((a.getDateStart().isAfter(start) || a.getDateStart().isEqual(start)) && (a.getDateStart().isBefore(end) || a.getDateStart().isEqual(end))) || (a.getDateStart().isBefore(start) || a.getDateStart().isEqual(start)) && (a.getDateEnd().isBefore(end) || a.getDateStart().isEqual(end)) || (a.getDateStart().isBefore(start) || a.getDateStart().isEqual(start)) && (a.getDateEnd().isAfter(end) || a.getDateEnd().isEqual(end))){
                return true;
            }
        }

        return false;
    }
    public static ObservableList<Integer> checkForAppointmentsOnLogin(Integer userID) throws SQLException {
        ObservableList<Integer> alist = FXCollections.observableArrayList();
        String sql = String.format("SELECT * FROM Appointments WHERE User_ID = '%d';", userID);
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            if (LocalDateTime.now().toLocalDate().isEqual(rs.getTimestamp("Start").toLocalDateTime().toLocalDate()) && rs.getTimestamp("Start").toLocalDateTime().atZone(AppointmentsController.clientTimeZone).toLocalTime().until(LocalDateTime.now().atZone(AppointmentsController.clientTimeZone).toLocalTime(), ChronoUnit.MINUTES) >= -15 && rs.getTimestamp("Start").toLocalDateTime().atZone(AppointmentsController.clientTimeZone).toLocalTime().until(LocalDateTime.now().atZone(AppointmentsController.clientTimeZone).toLocalTime(), ChronoUnit.MINUTES) <= 0){
                alist.add(rs.getInt("Appointment_ID"));
            }
        }
        return alist;
    }

}
