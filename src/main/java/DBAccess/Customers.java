package DBAccess;

import Model.Customer;
import helper.AlertBox;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * A class to perform several operations related to retrieving Customers.
 */
public class Customers {

    /**
     * A hashmap to retrieve a division ID given a division name in the form as a string.
     */
    public static HashMap<String, Integer> divisionsMap = new HashMap<>();

    /**
     * A hashmap to retrieve a country ID given a country name in the form as a string.
     */
    public static HashMap<String, Integer> countriesMap = new HashMap<>();

    /**
     * A function that records all customers in the database and returns an observable list.
     * @return Returns an observable list to store all customers within the database.
     * @throws SQLException
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {

        ObservableList<Customer> alist = FXCollections.observableArrayList();
        PreparedStatement ps;
        try {
            String sql = "SELECT * from customers";
            try {
                ps = JDBC.getConnection().prepareStatement(sql);
            } catch (NullPointerException e) {
                AlertBox.display("Error", "No connection to the database found!");
                return null;
            }
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Integer customerID = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                Integer division = rs.getInt("Division_ID");
                Customer c = new Customer(customerID, name, address, postalCode, phone, division);
                alist.add(c);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return alist;
    }

    /**
     * A function that gets all the country names from the database as a string.
     * @return Returns an observablelist of strings to populate a combobox on the main gui interface.
     * @throws SQLException
     */
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

    /**
     * A function that gets all the division names from the database as a string.
     * @return Returns an observablelist of strings to populate a combobox on the main gui interface.
     * @throws SQLException
     */
    public static ObservableList<String> getAllDivisions() throws SQLException {
        ObservableList<String> dlist = FXCollections.observableArrayList();

        String sql = "SELECT * FROM first_level_divisions;";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            String dname = rs.getString("Division");
            if (dname.equals("QuÃ©bec")){
                dname = "Quebec";
            }
            Integer dID = rs.getInt("Division_ID");
            dlist.add(dname);
            divisionsMap.put(dname, dID);
        }
        return dlist;
    }

    /**
     * A function that translates a division ID into a division name as a string when creating an appointment object.
     * @param divisionID The division ID obtained from reading the appointment database.
     * @return Returns a string of the appropriate division as a string for the input division ID.
     */
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

    /**
     * A function that translates a division ID into a country name as a string.
     * @param divisionID The division ID.
     * @return Returns a string of the appropriate country as a string for the input division ID.
     */
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

    /**
     *
     * A function to delete a customer from the database with already having the customer object ready to delete.
     * @param tempCustomer The selected customer object to delete.
     * @return Returns a boolean whether or not delete is successful. Returns true if delete is successful, false if it failed.
     */
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

    /**
     * A function to add an customer to the database without having created the Customer object already.
     * @param ID A Customer ID
     * @param name A Customer Name
     * @param address A Customer Address
     * @param phone A Customer Phone Number
     * @param postal A Customer Postal Code
     * @param division A Customer Division
     * @throws SQLException
     */
    public static void addCustomer(Integer ID, String name, String address, String phone, String postal, Integer division) throws SQLException {
        String sql = String.format("INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES (%d, '%s', '%s', '%s', '%s', '%d');", ID, name, address, postal, phone, division);
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.executeUpdate();
    }

    /**
     * A function to add a customer to the database with already having created the Customer object.
     * @param newCustomer A new customer object to add to the database.
     * @throws SQLException
     */
    public static void addCustomer(Customer newCustomer) throws SQLException {
        Integer ID = newCustomer.getCustomerID();
        String name = newCustomer.getName();
        String address = newCustomer.getAddress();
        String phone = newCustomer.getPhoneNumber();
        String postal = newCustomer.getPostalCode();
        String division = newCustomer.getDivision();
        String sql = String.format("INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES (%d, '%s', '%s', '%s', '%s', '%d');", ID, name, address, postal, phone, Customers.divisionsMap.get(division));
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.executeUpdate();
    }

    /**
     * A function to update the database by removing the old customer object and adding the new customer object.
     * @param oldCustomer The old customer to delete from the database.
     * @param newCustomer The new customer to add to the database.
     * @throws SQLException
     */
    public static void updateCustomer(Customer oldCustomer, Customer newCustomer) throws SQLException {
        Customers.deleteCustomer(oldCustomer);
        Customers.addCustomer(newCustomer);
    }
}
