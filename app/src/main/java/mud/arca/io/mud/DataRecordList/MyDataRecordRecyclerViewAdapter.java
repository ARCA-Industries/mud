package mud.arca.io.mud.DataRecordList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.Util;
import mud.arca.io.mud.R;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Day} and makes a call to the
 * specified {@link OnDayItemClickListener} when an item is clicked.
 */
public class MyDataRecordRecyclerViewAdapter extends FirestoreRecyclerAdapter<Day, MyDataRecordRecyclerViewAdapter.ViewHolder> {

    private final OnDayItemClickListener mListener;

    private Context context;

    public MyDataRecordRecyclerViewAdapter(FirestoreRecyclerOptions<Day> options, OnDayItemClickListener listener) {
        super(options);

        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        // Create a new instance of the ViewHolder, in this case we are using a custom layout called R.layout.fragment_datarecord for each item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_datarecord, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Day model) {
        // Inflate the view from data inside of model
        holder.day = model;

        // Set UI from model data
        holder.mDateView.setText(Util.formatDate(model.getDate()));
        holder.mMoodView.setText(model.getMoodString());
        holder.mVariableView.setText(model.getVarString("Sleep"));


        // Set the RecordDetailsActivity to launch on click
        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onDayItemClick(holder.day);
            }
        });

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mDateView;
        final TextView mMoodView;
        final TextView mVariableView;
        public Day day;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDateView = view.findViewById(R.id.dateTextView);
            mMoodView = view.findViewById(R.id.moodTextView);
            mVariableView = view.findViewById(R.id.attributeTextView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mMoodView.getText() + "'";
        }
    }


    public interface OnDayItemClickListener {
        void onDayItemClick(Day day);
    }
}
