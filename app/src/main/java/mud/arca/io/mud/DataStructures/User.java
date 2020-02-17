package mud.arca.io.mud.DataStructures;

import java.util.ArrayList;

public class User {
    private String name;
    private ArrayList<Day> dayData;
    private ArrayList<Variable> varData;

    private static User currentUser = null;

    public static User getCurrentUser() {
        if (currentUser == null) {
            currentUser = new MockUser();
        }
        return currentUser;
    }

    public User(String name) {
        this.name = name;
        this.dayData = new ArrayList<>();
        this.varData = new ArrayList<>();
    }

    public int getAverageMood() {
        // TODO: implementation
        return 0;
    }

    public void setName(String n){
        name = n;
    }

    public ArrayList<Day> getDayData() {
        return dayData;
    }

    public void setDayData(ArrayList<Day> dayData) {
        this.dayData = dayData;
    }

    public ArrayList<Variable> getVarData() {
        return varData;
    }

    public void setVarData(ArrayList<Variable> varData) {
        this.varData = varData;
    }
}
