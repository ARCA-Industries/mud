package mud.arca.io.mud.DataStructures;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static mud.arca.io.mud.DataStructures.VarType.FLOAT;

// This class contains data to show demo graphs.
public class DemoUser {

    User demoUser = new User("DemoUser");

    Variable varSleep = new Variable("Sleep", FLOAT);

    public static void init() {
        String dt = "2008-01-01";  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        //c.setTime(sdf.parse(dt));
        //c.add(Calendar.DATE, 1);  // number of days to add
        //dt = sdf.format(c.getTime());  // dt is now the new date
        System.out.println("qqqq2222");
    }

}
