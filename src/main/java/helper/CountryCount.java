package helper;

import java.util.HashMap;

/**
 * A Class only used to manage a tableview of the list of countries and the number of customer associated per country.
 */
public class CountryCount {
    /**
     * A string passed in to track country name.
     */
    String countryName;

    /**
     * A HashMap to track the number of country's names of Customers by incrementing with each object created.
     */
    public static HashMap<String, Integer> numberOfMap = new HashMap<>();

    /**
     * Country count constructor to track the number of customers per country.
     * @param s Country to be added to the object.
     */
    public CountryCount(String s){
        this.countryName = s;
        if (numberOfMap.get(s) != null){
            numberOfMap.put(s, numberOfMap.get(s)+1);
        } else {
            numberOfMap.put(s, 1);
        }
    }

    /**
     * This function allows the CellValueFactory of the tablecolumn to grab country name.
     * @return String of the country name.
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * This function allows the CellValueFactory of the tablecolumn to grab numberOf.
     * @return Number of customers for a country name.
     */
    public int getNumberOf() {
        return numberOfMap.get(countryName);
    }
}
