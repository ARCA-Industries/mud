package mud.arca.io.mud.Settings;

import androidx.appcompat.app.AppCompatActivity;

import mud.arca.io.mud.DataStructures.Variable;
import mud.arca.io.mud.R;

import android.os.Bundle;

import java.util.ArrayList;


public class VariableManagement extends AppCompatActivity {

    public static ArrayList<Variable> variables_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variable_management);

    }

    public void createVariable() {

        // 1. Get user input

        // 1a. Validate user input

        // 2. Create variable object based on input

        // 3. Add to management table

    }
}
