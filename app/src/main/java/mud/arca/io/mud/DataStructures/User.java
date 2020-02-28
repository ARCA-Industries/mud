package mud.arca.io.mud.DataStructures;

import java.util.ArrayList;
import java.util.Date;

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

    /**
     * Return an ArrayList of Days from startDate to endDate (inclusive).
     * @param startDate
     * @param endDate
     */
    public ArrayList<Day> fetchDays(Date startDate, Date endDate) {
        ArrayList<Day> ret = new ArrayList<>();

        // This could definitely be more efficient if we assume the dayData is sorted by Date.
        for (Day d : dayData) {
            Date curDate = d.getDate();
            if (Util.dateLTE(startDate, curDate) && Util.dateLTE(curDate, endDate)) {
                ret.add(d);
            }
        }
        return ret;
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
