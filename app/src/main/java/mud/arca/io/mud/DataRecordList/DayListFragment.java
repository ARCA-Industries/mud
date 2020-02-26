package mud.arca.io.mud.DataRecordList;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import mud.arca.io.mud.DataRecordList.dummy.DayListContent;
import mud.arca.io.mud.DataRecordList.dummy.DayListContent.DayListItem;
import mud.arca.io.mud.DataStructures.User;
import mud.arca.io.mud.DataStructures.Variable;
import mud.arca.io.mud.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DayListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

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

        // Set the adapter
        RecyclerView recyclerView = view.findViewById(R.id.list);
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        // Add the list items
        DayListContent dlc = new DayListContent(User.getCurrentUser());
        recyclerView.setAdapter(new MyDataRecordRecyclerViewAdapter(dlc.ITEMS, mListener));

        // Set up the dropdown
        AppCompatSpinner spinner = view.findViewById(R.id.dayListVarDropdown);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_spinner_item,
                getVariableLabels());
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        return view;
    }

    public static List<String> getVariableLabels() {
        List<String> ret = new ArrayList<>();
        for (Variable v : User.getCurrentUser().getVarData()) {
            ret.add(v.getName());
        }
        return ret;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
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
        void onListFragmentInteraction(DayListItem item);
    }
}
