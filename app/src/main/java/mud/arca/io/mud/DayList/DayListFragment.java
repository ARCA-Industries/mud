package mud.arca.io.mud.DayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import mud.arca.io.mud.Analysis.AnalysisFragment;
import mud.arca.io.mud.Analysis.PersistentSpinner;
import mud.arca.io.mud.DayDetails.DayDetailsActivity;
import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.MockUser;
import mud.arca.io.mud.DataStructures.User;
import mud.arca.io.mud.Util.Util;
import mud.arca.io.mud.R;
import mud.arca.io.mud.Database.DatabaseHelper;

/**
 * A fragment containing the Day list (Dashboard).
 */
public class DayListFragment extends Fragment {

    private final static int RC_EDIT_DAY_DETAILS = 1001;

    private CollectionReference mItemsCollection;
    private FirestoreRecyclerAdapter adapter;
    private ArrayAdapter<String> spinnerArrayAdapter;
    PersistentSpinner varPS;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DayListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_datarecord_list, container, false);

        mItemsCollection = DatabaseHelper.getDaysCollection();
        setUpAdapter();

        // Make the RecyclerView use the adapter
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setAdapter(adapter);

        // TODO: Temporarily add/delete days for debug
        User user = new MockUser();
        view.findViewById(R.id.dateTextView).setOnClickListener(v -> debugAddDay(
                user.getDayData().get(new Random().nextInt(user.getDayData().size()))
        ));
        view.findViewById(R.id.moodTextView).setOnClickListener(v -> new AlertDialog.Builder(view.getContext())
                .setTitle("Delete all?")
                .setMessage("Do you really want to delete all day records?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> debugRemoveAllDays())
                .setNegativeButton(android.R.string.no, null).show());

        // Set up the dropdown
//        AppCompatSpinner varSpinner = view.findViewById(R.id.dayListVarDropdown);
//        spinnerArrayAdapter = new ArrayAdapter<>(view.getContext(),
//                android.R.layout.simple_spinner_item,
//                Util.getVariableLabels());
//        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        varSpinner.setAdapter(spinnerArrayAdapter);


        // Can't call AnalysisFragment.setupSpinner because we need to set spinnerArrayAdapter,
        // in order for refreshDropdown to function.
        AppCompatSpinner varSpinner = view.findViewById(R.id.dayListVarDropdown);
        spinnerArrayAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, Util.getVariableLabels());
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        varSpinner.setAdapter(spinnerArrayAdapter);
        varPS = new PersistentSpinner(getContext(), varSpinner, "DayListVarSelectedInt");
        varSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                varPS.savePosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Not implemented
            }
        });

        User.getCurrentUser().updateUserData(u -> {
            User.setCurrentUser(u);
            refreshDropdown();
        });

        return view;
    }

    private void refreshDropdown() {
        Util.debug("refreshDropdown called");
        spinnerArrayAdapter.clear();
        spinnerArrayAdapter.addAll(Util.getVariableLabels());
        varPS.setSelectionToSharedPref();
    }

    private void setUpAdapter() {
        FirestoreRecyclerOptions<Day> options = new FirestoreRecyclerOptions.Builder<Day>()
                .setQuery(
                        mItemsCollection.orderBy("date", Query.Direction.DESCENDING),
                        Day.class
                ).build();


        adapter = new DayListRecyclerViewAdapter(options, (day, reference) -> {
            Intent i = DayDetailsActivity.getLaunchIntentForDate(getContext(), day.getDate());
            startActivityForResult(i, RC_EDIT_DAY_DETAILS);

        });

    }

    // TODO: Use this instead: https://github.com/firebase/FirebaseUI-Android/blob/master/database/README.md#automatic-listening
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    // TODO: Remove debug method
    private void debugAddDay(@NonNull Day chat) {
        mItemsCollection.add(chat);
    }

    // TODO: Remove debug method
    private void debugRemoveAllDays() {
        mItemsCollection.get().addOnCompleteListener(task -> {
            for (DocumentSnapshot document : task.getResult().getDocuments()) {
                document.getReference().delete();
            }
        });
    }

}
