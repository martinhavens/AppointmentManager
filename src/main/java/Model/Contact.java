package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A class to create Contact objects.
 */
public class Contact {

    public int getContactID() {
        return contactID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    int contactID;
    String name;
    String email;

    public static ObservableList<Contact> allContacts = FXCollections.observableArrayList();

    public Contact(int contact_id, String name, String email) {
        this.contactID = contact_id;
        this.name = name;
        this.email = email;
        allContacts.add(this);
    }
}
