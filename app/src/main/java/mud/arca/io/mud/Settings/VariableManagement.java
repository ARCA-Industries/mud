package mud.arca.io.mud.Settings;

import androidx.appcompat.app.AppCompatActivity;

import mud.arca.io.mud.DataStructures.User;
import mud.arca.io.mud.DataStructures.Variable;
import mud.arca.io.mud.R;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.errorprone.annotations.Var;

import java.util.ArrayList;


public class VariableManagement extends AppCompatActivity {

    /**
     * Local Arraylist to access user variables
     * Still not connected to the database...
     * - Robert
     */
    public ArrayList<Variable> user_variables =
            User.getCurrentUser().getVarData();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variable_management);

        populateTable();

    }

    /**
     * Method populateTable()
     * Populates our variable table layout with variables stored in an array list.
     *
     */
    public void populateTable() {
        TableLayout inflate = findViewById(R.id.variable_table);
        for (int i = 0; i < user_variables.size(); i++) {
            TableRow row = new TableRow(VariableManagement.this);
            TextView txtcol1 = new TextView(VariableManagement.this);
            TextView txtcol2 = new TextView(VariableManagement.this);
            txtcol1.setText(user_variables.get(i).getName());
            txtcol2.setText(user_variables.get(i).varTypeToString());
            row.addView(txtcol1);
            row.addView(txtcol2);
            inflate.addView(row);
        }
    }

    /**
     * Method refreshTable
     * Refreshes our table so that it displays all current variables stored
     * @param v for our onClick
     */
    public void refreshTable(View v) {
        TableLayout table = findViewById(R.id.variable_table);
        table.removeViews(1, table.getChildCount()-1);
        populateTable();
    }


    /**
     * Method createVariable
     * Creates variables to save to user.
     * @param v for our onClick
     */
    public void createVariable(View v) {

        EditText userVarName = findViewById(R.id.variable_name);
        Spinner userVarType = findViewById(R.id.variable_type);

        // 1. Get user input

        String variableName = userVarName.getText().toString();
        String variableType = userVarType.getSelectedItem().toString();

        // 2. Create variable object based on input

        Variable custom = new Variable(variableName, variableType);

        // 3. Add to management table

        user_variables.add(custom);
        User.getCurrentUser().setVarData(user_variables);
        userVarName.getText().clear();
        refreshTable(v);

        // TODO: Remove this. Temporarily populates the database with mock variable data
        android.widget.Toast.makeText(getApplicationContext(), "Populating database with mock data...", android.widget.Toast.LENGTH_LONG).show();
        mud.arca.io.mud.database.DatabaseHelper.temp_saveMockVariables();
    }

    /**
     * Method deleteVariable
     * Deletes variables
     * Probably should make it more user friendly
     * @param v for our onClick
     */
    public void deleteVariable(View v) {

        EditText userVarName = findViewById(R.id.variable_name);
        String variableName = userVarName.getText().toString();

        for (int i = 0; i < user_variables.size(); i++) {
            if(user_variables.get(i).getName().equals(variableName)) {
                user_variables.remove(user_variables.get(i));
            }
        }

        User.getCurrentUser().setVarData(user_variables);
        userVarName.getText().clear();
        refreshTable(v);
    }
}