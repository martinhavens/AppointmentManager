package DBAccess;

import Model.Contact;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public abstract class Contacts {

    public static HashMap<Integer, String> contactDictionary = setupDictionary(false);
    public static HashMap<String, Integer> reverseContactDictionary = setupDictionary(true);
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
