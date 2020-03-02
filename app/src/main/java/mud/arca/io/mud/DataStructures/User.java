package mud.arca.io.mud.DataStructures;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import mud.arca.io.mud.database.DatabaseHelper;

public class User {
    private ArrayList<Day> dayData;
    private ArrayList<Variable> varData;

    private static User currentUser = null;

    public static User getCurrentUser() {
        if (currentUser == null) {
            currentUser = new User();
        }
        return currentUser;
    }

    public User() {
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

//        Util.debug(Util.formatDateWithYear(startDate));
//        Util.debug(Util.formatDateWithYear(endDate));
//        Util.debug(String.valueOf(Util.dateLTE(startDate, endDate)));

        // This could definitely be more efficient if we assume the dayData is sorted by Date.
        for (Day d : dayData) {
            Date curDate = d.getDate();
            //Util.debug(Util.formatDateWithYear(curDate));
            if (curDate != null && Util.dateLTE(startDate, curDate) && Util.dateLTE(curDate, endDate)) {
                ret.add(d);
            }
        }
        return ret;
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


    public interface UserUpdateListener {
        void onUserUpdate(User user);
    }

    public void updateUserData(UserUpdateListener listener) {
        // Keep track of number of callbacks that have finished.
        // Then, only notify the listener when all have finished.
        AtomicInteger callbacksFinished = new AtomicInteger();

        DatabaseHelper.loadDayData(days -> {
            this.dayData = new ArrayList<>(days);

            if (callbacksFinished.get() == 0) {
                callbacksFinished.addAndGet(1);
            } else {
                listener.onUserUpdate(this);
            }
        });

        DatabaseHelper.loadVariableData(variables -> {
            System.out.println("variables = " + variables);
            this.varData = new ArrayList<>(variables);

            if (callbacksFinished.get() == 0) {
                callbacksFinished.addAndGet(1);
            } else {
                listener.onUserUpdate(this);
            }
        });
    }


}
