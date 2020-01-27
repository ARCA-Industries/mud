package mud.arca.io.mud.DataStructures;

import java.util.ArrayList;

public class User {

    public String name;
    public ArrayList<Day> dayData;

    public int getAverageMood() {

        return 0; //Still figuring out return method for getAverageMood()
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
}
