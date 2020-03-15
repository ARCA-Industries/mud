package mud.arca.io.mud.Settings;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.RecyclerView;
import mud.arca.io.mud.DataStructures.Variable;
import mud.arca.io.mud.R;
import mud.arca.io.mud.Database.DatabaseHelper;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;


public class VariableManagementActivity extends AppCompatActivity {

    /**
     * Local Arraylist to access user variables
     * Still not connected to the database...
     * - Robert
     */
    private CollectionReference user_variables;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variable_management);

        user_variables = DatabaseHelper.getVariableCollection();
        setUpAdapter();

        // Make the RecyclerView use the adapter
        RecyclerView recyclerView = findViewById(R.id.variable_table);
        recyclerView.setAdapter(adapter);

    }


    private void setUpAdapter() {
        FirestoreRecyclerOptions<Variable> options = new FirestoreRecyclerOptions.Builder<Variable>()
                .setQuery(
                        user_variables.orderBy("name", Query.Direction.ASCENDING),
                        Variable.class
                ).setLifecycleOwner(this).build();

        adapter = new VariableManagementAdapter(options, (variable) -> deleteVariable(variable));

    }


    /**
     * Method createVariable
     * Creates variables to save to user.
     *
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
        userVarName.getText().clear();
    }

    /**
     * Method deleteVariable
     * Deletes variables
     * Probably should make it more user friendly
     *
     * @param v for our onClick
     */
    public void deleteVariable(View v) {

        EditText userVarName = findViewById(R.id.variable_name);
        String variableName = userVarName.getText().toString();

        user_variables.get().addOnCompleteListener(runnable -> {
            for (DocumentSnapshot document : runnable.getResult().getDocuments()) {
                if (document.toObject(Variable.class).getName().equals(variableName)) {
                    document.getReference().delete();
                }
            }

            userVarName.getText().clear();
        });

    }

    public void deleteVariable(DocumentReference reference) {
        ((EditText)findViewById(R.id.variable_name)).getText().clear();
        reference.delete();
    }

    // This is much slower than deleting by reference
    public void deleteVariable(Variable variable) {
        EditText userVarName = findViewById(R.id.variable_name);

        user_variables.get().addOnCompleteListener(runnable -> {
            for (DocumentSnapshot document : runnable.getResult().getDocuments()) {
                if (document.toObject(Variable.class).getName().equals(variable.getName())) {
                    document.getReference().delete();
                }
            }

            userVarName.getText().clear();
        });
    }
}