package mud.arca.io.mud.DataRecordList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import mud.arca.io.mud.DataRecordList.recorddetails.RecordDetailsActivity;
import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.MockUser;
import mud.arca.io.mud.DataStructures.User;
import mud.arca.io.mud.DataStructures.Util;
import mud.arca.io.mud.DataStructures.Variable;
import mud.arca.io.mud.R;

/**
 * A fragment containing the Day list (Dashboard).
 */
public class DayListFragment extends Fragment {

    private final static int RC_EDIT_DAY_DETAILS = 1001;

    private final static String EXTRA_REFERENCE_ID = "reference_id";

    private CollectionReference mItemsCollection;
    private FirestoreRecyclerAdapter adapter;

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

        mItemsCollection = FirebaseFirestore.getInstance().collection("users/" + FirebaseAuth.getInstance().getUid() + "/days");
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
        AppCompatSpinner spinner = view.findViewById(R.id.dayListVarDropdown);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_spinner_item,
                Util.getVariableLabels());
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        return view;
    }

    private void setUpAdapter() {
        FirestoreRecyclerOptions<Day> options = new FirestoreRecyclerOptions.Builder<Day>()
                .setQuery(
                        mItemsCollection.orderBy("date", Query.Direction.DESCENDING),
                        Day.class
                ).build();



        adapter = new DayListRecyclerViewAdapter(options, (day, reference) -> {
            Intent i = RecordDetailsActivity.getLaunchIntentForDay(getContext(), day);
            // Pass the reference so that I know which document to update in the db.
            // This relies on the fragment passing back the same intent...
            i.putExtra(EXTRA_REFERENCE_ID, reference.getId());
            startActivityForResult(i, RC_EDIT_DAY_DETAILS);

        }) ;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_EDIT_DAY_DETAILS:
                if (resultCode == Activity.RESULT_OK) {
                    // Save this day to the database
                    mItemsCollection.document(data.getStringExtra(EXTRA_REFERENCE_ID)).set(
                            data.getSerializableExtra(RecordDetailsActivity.EXTRA_DAY)
                    );
                }
                break;
        }

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
