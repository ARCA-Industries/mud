package mud.arca.io.mud.DataRecordList.recorddetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import mud.arca.io.mud.DataRecordList.recorddetails.dummy.VariableListContent.VariableListItem;
import mud.arca.io.mud.R;

/**
 * {@link RecyclerView.Adapter} that can display a {@link VariableListItem}
 * TODO: Replace the implementation with code for your data type.
 */
public class DetailsVariableRecyclerViewAdapter extends RecyclerView.Adapter<DetailsVariableRecyclerViewAdapter.ViewHolder> {

    private final List<VariableListItem> mValues;

    private Context context;

    public DetailsVariableRecyclerViewAdapter(List<VariableListItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_detailsrow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mVariableTextView.setText(mValues.get(position).type);
        holder.mValueTextView.setText(mValues.get(position).value);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mVariableTextView;
        public final TextView mValueTextView;
        public VariableListItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mVariableTextView = (TextView) view.findViewById(R.id.variableTypeTextView);
            mValueTextView = (TextView) view.findViewById(R.id.valueTextView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mValueTextView.getText() + "'";
        }
    }
}
