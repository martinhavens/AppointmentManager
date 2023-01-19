package DBAccess;

import Model.Contact;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * A class to perform actions related to contacts.
 */
public abstract class Contacts {

    /**
     * A HashMap to retrieve the contact's name as a string given the contact's ID.
     */
    public static HashMap<Integer, String> contactDictionary = setupDictionary(false);
    /**
     * A HashMap to retrieve the contact's ID as an ID given the contact's name as a String.
     */
    public static HashMap<String, Integer> reverseContactDictionary = setupDictionary(true);

    /**
     * A function to initialize the Contacts class hashmaps
     * @param reverse Determine whether the hashmap will require a string input or an integer input
     * @return Returns a hashmap to retrieve the contact ID or contact name given the contact name or contact ID, respectively.
     */
    public static HashMap setupDictionary(boolean reverse){
        HashMap contactDiction = new HashMap<>();
        try {
            if (reverse) {
                for (Contact C: getAllContacts()){
                    int id = C.getContactID();
                    String name = C.getName();
                    contactDiction.put(name, id);
                }
            }
            else {
                for (Contact C: getAllContacts()){
                    int id = C.getContactID();
                    String name = C.getName();
                    contactDiction.put(id, name);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return contactDiction;
    }

    /**
     * A function to create contact objects from the database and allow the internal setupDictionary function to operate.
     * @return Returns a list of all contacts to allow setupDictionary to iterate for all contacts.
     * @throws SQLException
     */
    public static ObservableList<Contact> getAllContacts() throws SQLException {
        ObservableList<Contact> clist = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * from contacts";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Integer contact_id = rs.getInt("Contact_ID");
                String name = rs.getString("Contact_Name");
                String email = rs.getString("Email");
                Contact c = new Contact(contact_id, name, email);
                Contact.allContacts.add(c);
                clist.add(c);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return clist;
    }

    /**
     * A function used to iterate through all contacts in the database and populate a combobox on the main gui interface.
     * @return Returns an observablelist of strings to populate a combobox on the main gui interface.
     */
    public static ObservableList<String> getAllContactNames() {

        ObservableList<String> clist = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * from contacts";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Integer contact_id = rs.getInt("Contact_ID");
                String name = rs.getString("Contact_Name");
                String email = rs.getString("Email");
                Contact c = new Contact(contact_id, name, email);
                Contact.allContacts.add(c);
                clist.add(c.getName());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return clist;
    }
}
