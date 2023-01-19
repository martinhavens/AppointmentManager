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
            System.out.println(rs.getInt("Appointment_ID"));
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
//            System.out.println(rs.getTimestamp("Start").toLocalDateTime());System.out.println();
//            a.getDateStart().isBefore(start) || a.getDateStart().isAfter(start) || a.getDateStart().isEqual(start)) && ((a.getDateEnd().isBefore(end) || a.getDateEnd().isEqual(end) || a.getDateEnd().isAfter(end)) && (a.getDateEnd().isAfter(start) && (a.getDateEnd().isBefore(end) || a.getDateEnd().isEqual(end)))) || (a.getDateStart().isBefore(start) || a.getDateStart().isAfter(start) || a.getDateStart().isEqual(start)) && ((a.getDateEnd().isBefore(end) || a.getDateEnd().isEqual(end) || a.getDateEnd().isAfter(end)) && (a.getDateStart().isBefore(end) && (a.getDateStart().isAfter(end) || a.getDateStart().isEqual(start))))            System.out.println(a.getDateStart().isBefore(start) );
//            System.out.println("a.getDateStart().isAfter(start) " + a.getDateStart().isAfter(start));
//            System.out.println("a.getDateStart().isEqual(start) "+ a.getDateStart().isEqual(start));
//            System.out.println("a.getDateEnd().isBefore(end) "+ a.getDateEnd().isBefore(end));
//            System.out.println("a.getDateEnd().isEqual(end) " +a.getDateEnd().isEqual(end));
//            System.out.println("a.getDateEnd().isAfter(end) " +a.getDateEnd().isAfter(end));
//            System.out.println("a.getDateEnd().isAfter(start) "+a.getDateEnd().isAfter(start));
//            System.out.println("a.getDateStart().isBefore(end) "+a.getDateStart().isBefore(end));
            System.out.println("a.getDateStart() "+a.getDateStart());
            System.out.println("a.getDateEnd() "+a.getDateEnd());
            System.out.println(start);
            System.out.println(end);
//            System.out.println("a.getDateStart().isAfter(end) "+a.getDateStart().isAfter(end));
//            System.out.println((a.getDateStart().isBefore(start) || a.getDateStart().isAfter(start) || a.getDateStart().isEqual(start)) && ((a.getDateEnd().isBefore(end) || a.getDateEnd().isEqual(end) || a.getDateEnd().isAfter(end)) && (a.getDateEnd().isAfter(start) )));
//            System.out.println((a.getDateStart().isBefore(start) || a.getDateStart().isAfter(start) || a.getDateStart().isEqual(start)) && ((a.getDateEnd().isBefore(end) || a.getDateEnd().isEqual(end) || a.getDateEnd().isAfter(end)) && (a.getDateStart().isBefore(end) )));
            System.out.println(start.isBefore(a.getDateEnd()));
            System.out.println(a.getDateStart().isBefore(end));
//            System.out.println();
//            System.out.println();
//            (a.getDateStart().isBefore(start) || a.getDateStart().isAfter(start) || a.getDateStart().isEqual(start)) && ((a.getDateEnd().isBefore(end) || a.getDateEnd().isEqual(end) || a.getDateEnd().isAfter(end)) && (a.getDateEnd().isAfter(start) && (a.getDateEnd().isBefore(end) || a.getDateEnd().isEqual(end))));
//            (a.getDateStart().isBefore(start) || a.getDateStart().isAfter(start) || a.getDateStart().isEqual(start)) && ((a.getDateEnd().isBefore(end) || a.getDateEnd().isEqual(end) || a.getDateEnd().isAfter(end)) && (a.getDateStart().isBefore(end) && (a.getDateStart().isAfter(end) || a.getDateStart().isEqual(start))));
//            if (((a.getDateStart().isAfter(start) || a.getDateStart().isEqual(start)) && (a.getDateStart().isBefore(end) || a.getDateStart().isEqual(end))) || ((a.getDateStart().isBefore(start) || a.getDateStart().isEqual(start)) && (a.getDateEnd().isAfter(end) || a.getDateEnd().isEqual(end))) || ((a.getDateStart().isBefore(start) || a.getDateStart().isEqual(start)) && (a.getDateEnd().isBefore(end) || a.getDateEnd().isEqual(end)))){
//            if (((a.getDateStart().isBefore(start) || a.getDateStart().isAfter(start) || a.getDateStart().isEqual(start)) && ((a.getDateEnd().isBefore(end) || a.getDateEnd().isEqual(end) || a.getDateEnd().isAfter(end)) && a.getDateEnd().isAfter(start))) || ((a.getDateStart().isBefore(start) || a.getDateStart().isAfter(start) || a.getDateStart().isEqual(start)) && ((a.getDateEnd().isBefore(end) || a.getDateEnd().isEqual(end) || a.getDateEnd().isAfter(end)) && a.getDateStart().isBefore(end)))){
//            if ((((a.getDateStart().isBefore(start) || a.getDateStart().isAfter(start) || a.getDateStart().isEqual(start)) && ((a.getDateEnd().isBefore(end) || a.getDateEnd().isEqual(end) || a.getDateEnd().isAfter(end)) && (a.getDateEnd().isAfter(start) && (a.getDateEnd().isBefore(end) || a.getDateEnd().isEqual(end))))) || ((a.getDateStart().isBefore(start) || a.getDateStart().isAfter(start) || a.getDateStart().isEqual(start)) && ((a.getDateEnd().isBefore(end) || a.getDateEnd().isEqual(end) || a.getDateEnd().isAfter(end)) && (a.getDateStart().isBefore(end) && (a.getDateStart().isAfter(end) || a.getDateStart().isEqual(start))))))){
//            start1.before(end2) && start2.before(end1)
            if ((start.isBefore(a.getDateEnd()) && a.getDateStart().isBefore(end))) {
                return true;
            }
        }
        return false;
    }
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
