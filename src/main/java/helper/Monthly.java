package helper;

import java.util.HashMap;

/**
 * A Class only used to create a list of a single contact's number of monthly appointments by type.
 */
public class Monthly {
    /**
     * A string passed in to track month.
     */
    String month;

    /**
     * A string passed in to track type.
     */
    String type;

    /**
     * A HashMap to track the number of Months+Type of Appointment by incrementing with each object created.
     */
    public static HashMap<String, Integer> numberOfMap = new HashMap<>();

    /**
     * Monthly constructor to track a single contact's number of monthly appointments by type.
     * @param month Month to be added to the object.
     * @param type Appointment type to be added to the object.
     */
    public Monthly(String month, String type){
        this.month = month;
        this.type = type;
        if (numberOfMap.get(month+type) != null){
            numberOfMap.put(month+type, numberOfMap.get(month+type)+1);
        } else {
            numberOfMap.put(month+type, 1);
        }
    }

    /**
     * This function allows the CellValueFactory of the tablecolumn to grab month.
     * @return String of the month.
     */
    public String getMonth(){
        return month;
    }

    /**
     * This function allows the CellValueFactory of the tablecolumn to grab type.
     * @return String of the type.
     */
    public String getType(){
        return type;
    }

    /**
     * This function allows the CellValueFactory of the tablecolumn to grab numberOf.
     * @return Number of Appointment Types during a month.
     */
    public int getNumberOf(){
        return numberOfMap.get(month+type);
    }
}
