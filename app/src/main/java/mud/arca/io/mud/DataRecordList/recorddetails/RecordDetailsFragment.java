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

import java.sql.Timestamp;
import java.util.NoSuchElementException;

import mud.arca.io.mud.DataRecordList.MyDataRecordRecyclerViewAdapter;
import mud.arca.io.mud.DataRecordList.recorddetails.dummy.VariableListContent2;
import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.MoodRecording;
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
     * Update the seekbar progress to the mood value.
     */
    public void updateSeekBar(float moodVal) {
        int progressVal = moodToSlider(moodVal);
        seekbar.setProgress(progressVal);
    }

    /**
     * Update the mood text to the mood value.
     */
    public void updateMoodText(float moodVal) {
        String val = String.format("Mood (%.1f)", moodVal);
        moodTextView.setText(val);
    }

    /**
     * Update the mood recording in data structure.
     */
    public void updateUserMood(Day d, float moodVal) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        d.setMoodRecording(new MoodRecording(timestamp, moodVal));
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

        try {
            float moodVal = MyDataRecordRecyclerViewAdapter.daySelected.getAverageMood();
            updateSeekBar(moodVal);
            updateMoodText(moodVal);
        } catch (NoSuchElementException e) {
            // If there is no mood recording for that day.
            updateSeekBar(5f);
            moodTextView.setText("Mood (no value)");
        }

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                float moodVal = sliderToMood(progressValue);
                updateMoodText(moodVal);
                updateUserMood(MyDataRecordRecyclerViewAdapter.daySelected, moodVal);
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
