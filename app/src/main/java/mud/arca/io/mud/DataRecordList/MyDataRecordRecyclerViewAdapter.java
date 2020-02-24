package mud.arca.io.mud.DataRecordList;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import mud.arca.io.mud.DataRecordList.DataRecordListFragment.OnListFragmentInteractionListener;
import mud.arca.io.mud.DataRecordList.dummy.DayListContent;
import mud.arca.io.mud.DataRecordList.dummy.DayListContent.DayListItem;
import mud.arca.io.mud.DataRecordList.recorddetails.RecordDetailsActivity;
import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DayListContent.DayListItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyDataRecordRecyclerViewAdapter extends RecyclerView.Adapter<MyDataRecordRecyclerViewAdapter.ViewHolder> {

    private final List<DayListItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    private Context context;

    // This variable keeps track of which day the user has clicked on.
    public static Day daySelected;

    public MyDataRecordRecyclerViewAdapter(List<DayListContent.DayListItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_datarecord, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // mItem has type DayListItem
        holder.mItem = mValues.get(position);
        holder.mDateView.setText(mValues.get(position).dateStr);
        holder.mMoodView.setText(mValues.get(position).moodStr);
        holder.mVariableView.setText(mValues.get(position).varStr);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }

                // TODO: Start an activity with recorddetailsfragment
                Intent intent = new Intent(context, RecordDetailsActivity.class);
//                intent.putExtra("key1", String.valueOf(position));

                daySelected = holder.mItem.day;
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDateView;
        public final TextView mMoodView;
        public final TextView mVariableView;
        public DayListContent.DayListItem mItem;

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
}
