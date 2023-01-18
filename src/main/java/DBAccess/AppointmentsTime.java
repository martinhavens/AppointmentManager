package DBAccess;

import Model.Appointment;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class AppointmentsTime {

    public static boolean checkDate(String dateCheck) throws SQLException {
        String sql = String.format("SELECT ISDATE('%s');", dateCheck);
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        int x = -1 ;
        while (rs.next()){
            System.out.println("x");
            x =rs.getInt("Result");
            System.out.println("x");
        }

        if (x != -1 && x == 1){
            return false;
        }
        else {
            System.out.println("y");
            return true;
        }
    }

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

    public static DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    public static DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss");

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
            System.out.println(start);
            System.out.println(end);
            System.out.println(start);
            System.out.println(a.getDateStart());


            System.out.println(start.isEqual(a.getDateStart()));
            System.out.println(end.isBefore(a.getDateEnd()));
            System.out.println(start.isAfter(a.getDateStart()));
            System.out.println(start.isBefore(a.getDateEnd()));
            if ((start.isEqual(a.getDateStart()) && end.isBefore(a.getDateEnd())) || (start.isAfter(a.getDateStart()) && start.isBefore(a.getDateEnd()))){
                return true;
            }
        }

        return false;
    }

}
