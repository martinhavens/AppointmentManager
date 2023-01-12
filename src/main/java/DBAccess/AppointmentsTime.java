package DBAccess;

import helper.JDBC;

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

}
