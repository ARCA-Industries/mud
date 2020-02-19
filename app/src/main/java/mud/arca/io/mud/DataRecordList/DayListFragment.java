package mud.arca.io.mud.DataRecordList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import mud.arca.io.mud.DataRecordList.recorddetails.RecordDetailsActivity;
import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.MockUser;
import mud.arca.io.mud.DataStructures.User;
import mud.arca.io.mud.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DayListFragment extends Fragment {

    // This variable keeps track of which day the user has clicked on. // TODO: Don't do this.
    public static Day daySelected;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private CollectionReference mItemsCollection;
    private FirestoreRecyclerAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DayListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DayListFragment newInstance(int columnCount) {
        DayListFragment fragment = new DayListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
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

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnDayItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Day item);
    }





    private void setUpAdapter() {
        FirestoreRecyclerOptions<Day> options = new FirestoreRecyclerOptions.Builder<Day>()
                .setQuery(
                        mItemsCollection.orderBy("date", Query.Direction.DESCENDING),
                        Day.class
                ).build();


        adapter = new DayListRecyclerViewAdapter(options, day -> {
            Intent intent = new Intent(getContext(), RecordDetailsActivity.class);

            daySelected =day; // TODO: Pass the selected day (model) to RecordDetailsActivity
            getContext().startActivity(intent);
        }) ;

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
