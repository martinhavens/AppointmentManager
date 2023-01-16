package DBAccess;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                        timeList.add(String.format("0%d:0%d:00", i, j));
                    } else {
                        timeList.add(String.format("0%d:%d:00", i, j));
                    }
                } else {
                    if (j < 10) {
                        timeList.add(String.format("%d:0%d:00", i, j));
                    } else {
                        timeList.add(String.format("%d:%d:00", i, j));
                    }
                }
            }
        }
        return timeList;
    }

}
