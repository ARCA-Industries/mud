package mud.arca.io.mud.DataRecordList.recorddetails;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import mud.arca.io.mud.DataRecordList.recorddetails.dummy.DummyContent;
import mud.arca.io.mud.R;

public class RecordDetailsFragment extends Fragment {

    private RecordDetailsFragmentViewModel mViewModel;

    public static RecordDetailsFragment newInstance() {
        return new RecordDetailsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.record_details_fragment, container, false);

        // Set the adapter
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        Context context = view.getContext();

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        recyclerView.setAdapter(new DetailsVariableRecyclerViewAdapter(DummyContent.ITEMS));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RecordDetailsFragmentViewModel.class);
        // TODO: Use the ViewModel
    }

}
