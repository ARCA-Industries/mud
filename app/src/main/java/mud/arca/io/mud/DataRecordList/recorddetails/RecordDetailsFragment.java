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
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.slider.Slider;

import mud.arca.io.mud.DataRecordList.MyDataRecordRecyclerViewAdapter;
import mud.arca.io.mud.DataRecordList.recorddetails.dummy.VariableListContent2;
import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.Util;
import mud.arca.io.mud.R;

public class RecordDetailsFragment extends Fragment {

    private RecordDetailsFragmentViewModel mViewModel;
    private TextView moodTextView;
    private SeekBar seekbar;

    public static RecordDetailsFragment newInstance() {
        return new RecordDetailsFragment();
    }

    /**
     * Convert slider value to mood value.
     * Slider value: int in range [0, 100]
     * Mood value: float in range [0, 10.0]
     * @param val
     * @return
     */
    public static float sliderToMood(int val) {
        return ((float) val) / 10f;
    }

    public static int moodToSlider(float val) {
        return Math.round(val * 10);
    }

    /**
     * Update the mood text to the slider value.
     */
    public void updateMoodText(int progress) {
        float moodVal = sliderToMood(progress);
        String val = String.format("Mood (%.1f)", moodVal);
        moodTextView.setText(val);
    }

    /**
     * Update the seekbar progress based on the avg mood of the day selected.
     */
    public void updateSeekBar() {
        Day d = MyDataRecordRecyclerViewAdapter.daySelected;
        int progressVal = moodToSlider((float) d.getAverageMood());
        seekbar.setProgress(progressVal);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.record_details_fragment, container, false);

        // Initialize fields
        moodTextView = view.findViewById(R.id.moodTextView);
        seekbar = view.findViewById(R.id.seekBar);

        // Set the adapter
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        updateSeekBar();
        updateMoodText(seekbar.getProgress());

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                updateMoodText(progressValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO: implement
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO: implement
            }
        });

        //recyclerView.setAdapter(new DetailsVariableRecyclerViewAdapter(VariableListContent.ITEMS));
        recyclerView.setAdapter(new DetailsVariableRecyclerViewAdapter(VariableListContent2.getItems()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RecordDetailsFragmentViewModel.class);
        // TODO: Use the ViewModel
    }

}
