package mud.arca.io.mud.Settings;

import androidx.appcompat.app.AppCompatActivity;

import mud.arca.io.mud.DataStructures.Variable;
import mud.arca.io.mud.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;


public class VariableManagement extends AppCompatActivity {

    /**
     * Local Arraylist for variables
     * At some point we should use the database instead
     * - Robert
     */
    public static ArrayList<Variable> variables_test = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variable_management);

        // Dummy Variables for Table Testing
/*
        variables_test.add(new Variable("Pizzas Eaten", "Integer"));
        variables_test.add(new Variable("Hours Worked", "Floating Point"));
        variables_test.add(new Variable("Went To School", "Boolean"));
        variables_test.add(new Variable("default test", " "));
*/

        populateTable();



    }

    /**
     * Method populateTable()
     * Populates our variable table layout with variables stored in an array list.
     *
     */
    public void populateTable() {
        TableLayout inflate = findViewById(R.id.variable_table);
        for (int i = 0; i < variables_test.size(); i++) {
            TableRow row = new TableRow(VariableManagement.this);
            TextView txtcol1 = new TextView(VariableManagement.this);
            TextView txtcol2 = new TextView(VariableManagement.this);
            txtcol1.setText(variables_test.get(i).getName());
            txtcol2.setText(variables_test.get(i).varTypeToString());
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
     * Creates variables to 
     * @param v for our onClick
     */
    public void createVariable(View v) {

        EditText userVarName = (EditText)findViewById(R.id.variable_name);
        Spinner userVarType = (Spinner)findViewById(R.id.variable_type);
        // 1. Get user input

        String variableName = userVarName.getText().toString();
        String variableType = userVarType.getSelectedItem().toString();



        // 2. Create variable object based on input

        Variable custom = new Variable(variableName, variableType);

        // 3. Add to management table

        variables_test.add(custom);
        userVarName.getText().clear();
        refreshTable(v);

    }
}