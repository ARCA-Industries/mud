package mud.arca.io.mud.DataRecordList.recorddetails;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mud.arca.io.mud.DataRecordList.DataRecordListFragment.OnListFragmentInteractionListener;
import mud.arca.io.mud.DataRecordList.dummy.DummyContent.DummyItem;
import mud.arca.io.mud.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyDataRecordRecyclerViewAdapter extends RecyclerView.Adapter<MyDataRecordRecyclerViewAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    private Context context;

    public MyDataRecordRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener) {
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
        holder.mItem = mValues.get(position);
        holder.mDateView.setText(mValues.get(position).id);
        holder.mMoodView.setText(mValues.get(position).content);
        holder.mVariableView.setText(mValues.get(position).details);

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
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDateView = (TextView) view.findViewById(R.id.dateTextView);
            mMoodView = (TextView) view.findViewById(R.id.moodTextView);
            mVariableView = (TextView) view.findViewById(R.id.attributeTextView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mMoodView.getText() + "'";
        }
    }
}
