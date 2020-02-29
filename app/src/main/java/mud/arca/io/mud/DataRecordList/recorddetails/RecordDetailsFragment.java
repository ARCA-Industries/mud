package mud.arca.io.mud.DataRecordList.recorddetails;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.content.Intent;
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

import java.sql.Timestamp;
import java.util.NoSuchElementException;

import mud.arca.io.mud.App;
import mud.arca.io.mud.DataRecordList.recorddetails.dummy.VariableListContent2;
import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.MoodRecording;
import mud.arca.io.mud.DataStructures.Util;
import mud.arca.io.mud.R;

public class RecordDetailsFragment extends Fragment {

    private TextView moodTextView;
    private SeekBar seekbar;

    public static RecordDetailsFragment newInstance(Day day) {
        RecordDetailsFragment fragment = new RecordDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(RecordDetailsActivity.EXTRA_DAY, day);
        fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * Convert slider value to mood value.
     * Slider value: int in range [0, 100]
     * Mood value: float in range [0, 10.0]
     *
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

    public void setSeekbarColor(int color) {
        seekbar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        seekbar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    private boolean moodRecExists = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Load Day from arguments
        Day day;
        try {
            day = (Day) getArguments().getSerializable(RecordDetailsActivity.EXTRA_DAY);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Must start this fragment with a Day! Use newInstance.");
        }

        String title = Util.formatDateWithYear(day.getDate());
        getActivity().setTitle(title);

        View view = inflater.inflate(R.layout.record_details_fragment, container, false);

        // Initialize fields
        moodTextView = view.findViewById(R.id.moodTextView);
        seekbar = view.findViewById(R.id.seekBar);

        // Set the adapter
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        try {
            float moodVal = day.getAverageMood();
            updateSeekBar(moodVal);
            updateMoodText(moodVal);
        } catch (NoSuchElementException e) {
            // If there is no mood recording for that day.
            updateSeekBar(5f);
            moodTextView.setText("Mood (no value)");
            setSeekbarColor(App.getContext().getColor(R.color.gray));
        }

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                float moodVal = sliderToMood(progressValue);
                updateMoodText(moodVal);
                updateUserMood(day, moodVal);

                if (!moodRecExists) {
                    setSeekbarColor(App.getContext().getColor(R.color.green));
                }
                moodRecExists = true;
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
        recyclerView.setAdapter(new DetailsVariableRecyclerViewAdapter(VariableListContent2.getItems(day)));

        // Exit activity with result when user saves
        view.findViewById(R.id.recordDetailsSaveButton).setOnClickListener(button -> {
            Intent data = getActivity().getIntent();
            data.putExtra(RecordDetailsActivity.EXTRA_DAY, day);
            getActivity().setResult(Activity.RESULT_OK, data);
            getActivity().finish();
        });

        return view;
    }

}
