package mud.arca.io.mud.DataRecordList.recorddetails;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        return inflater.inflate(R.layout.record_details_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RecordDetailsFragmentViewModel.class);
        // TODO: Use the ViewModel
    }

}
