package helper;

import java.util.HashMap;

public class Monthly {
    String month;
    String type;
    int numberOf;
    public static HashMap<String, Integer> numberOfMap = new HashMap<>();

    public Monthly(String month, String type){
        this.month = month;
        this.type = type;
        if (numberOfMap.get(month+type) != null){
            numberOfMap.put(month+type, numberOfMap.get(month+type)+1);
        } else {
            numberOfMap.put(month+type, 1);
        }
    }

    public String getMonth(){
        return month;
    }

    public String getType(){
        return type;
    }

    public int getNumberOf(){
        return numberOfMap.get(month+type);
    }
}
