package DBAccess;

import Model.Appointment;
import Model.Customer;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Customers {
    public static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    public static HashMap<String, Integer> divisionsMap = new HashMap<>();
    public static HashMap<String, Integer> countriesMap = new HashMap<>();


    public static ObservableList<Customer> getAllCustomers() throws SQLException {

        ObservableList<Customer> alist = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * from customers";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Integer customerID = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                Integer division = rs.getInt("Division_ID");
                Customer c = new Customer(customerID, name, address, postalCode, phone, division);
                allCustomers.add(c);
                alist.add(c);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return alist;
    }

    public static ObservableList<String> getAllCountries() throws SQLException {
        ObservableList<String> clist = FXCollections.observableArrayList();

        String sql = "SELECT Country, Country_ID FROM countries;";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            String cname = rs.getString("Country");
            int cID = rs.getInt("Country_ID");
            clist.add(cname);
            countriesMap.put(cname, cID);
        }
        return clist;
    }

    public static ObservableList<String> getAllDivisions() throws SQLException {
        ObservableList<String> dlist = FXCollections.observableArrayList();

        String sql = "SELECT * FROM first_level_divisions;";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            String dname = rs.getString("Division");
            Integer dID = rs.getInt("Division_ID");
            dlist.add(dname);
            divisionsMap.put(dname, dID);
        }
        return dlist;
    }

    public static String lookupCustomerDivision(Integer divisionID){
        String sql = null;
        try {
            sql = String.format("SELECT Division from first_level_divisions WHERE Division_ID='%d';", divisionID);

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                sql = rs.getString("Division");

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return(sql);
    }

    public static String lookupCustomerCountry(Integer divisionID){
        String sql = null;
        try {
            sql = String.format("SELECT COUNTRY_ID from first_level_divisions WHERE Division_ID='%d';", divisionID);

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            Integer countryID = null;
            while (rs.next()){
                countryID = rs.getInt("Country_ID");

            }
            sql = String.format("SELECT Country from countries WHERE Country_ID='%d';", countryID);
            ps = JDBC.getConnection().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()){
                sql = rs.getString("Country");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return(sql);
    }

    public static boolean deleteCustomer(Customer tempCustomer) throws SQLException {
        int custID = tempCustomer.getCustomerID();
        String sql = String.format("SELECT Customer_ID FROM appointments WHERE Customer_ID ='%d';", custID);

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            return false;
        }
        else {
            sql = String.format("DELETE FROM customers WHERE Customer_ID ='%d';", custID);
            ps = JDBC.getConnection().prepareStatement(sql);
            ps.executeUpdate();
            return true;
        }
    }

}
