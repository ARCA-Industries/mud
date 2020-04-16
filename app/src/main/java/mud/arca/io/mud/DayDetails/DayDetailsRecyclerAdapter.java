package mud.arca.io.mud.DayDetails;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import androidx.recyclerview.widget.RecyclerView;


import mud.arca.io.mud.DataStructures.Variable;
import mud.arca.io.mud.DayDetails.DayDetailsContent.VariableListItem;
import mud.arca.io.mud.DataStructures.Measurement;
import mud.arca.io.mud.Util.Util;
import mud.arca.io.mud.R;

/**
 * {@link RecyclerView.Adapter} that can display a {@link VariableListItem}
 * TODO: Replace the implementation with code for your data type.
 */
//public class DayDetailsRecyclerAdapter extends RecyclerView.Adapter<DayDetailsRecyclerAdapter.ViewHolder> {
public class DayDetailsRecyclerAdapter extends RecyclerView.Adapter {

    private final List<VariableListItem> mValues;

    private Context context;

    public DayDetailsRecyclerAdapter(List<VariableListItem> items) {
        mValues = items;
    }

    @Override
    public int getItemViewType(int position) {
        if (mValues.get(position).variable.getVartype() == Variable.VarType.BOOL) {
            return RowType.BOOL;
        } else {
            return RowType.NOT_BOOL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        if (viewType == RowType.BOOL) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_detailsrow_bool, parent, false);
            return new BoolViewHolder(view);
        } else { // NOT_BOOL
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_detailsrow, parent, false);
            return new NonBoolViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
//        holder.mItem = mValues.get(position);
//        holder.mVariableTextView.setText(mValues.get(position).varName);
//        holder.mValueTextView.setText(mValues.get(position).valueStr);

//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        if (holder instanceof NonBoolViewHolder) {
            NonBoolViewHolder nbvh = (NonBoolViewHolder) holder;
            nbvh.mItem = mValues.get(position);
            nbvh.mVariableTextView.setText(mValues.get(position).varName);
            nbvh.mValueTextView.setText(mValues.get(position).valueStr);
        } else if (holder instanceof BoolViewHolder) {
            BoolViewHolder bvh = (BoolViewHolder) holder;
            bvh.mVariableTextView.setText("test123");
            bvh.mBoolButton.setText("Thing");
        }
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class BoolViewHolder extends RecyclerView.ViewHolder {
        public final TextView mVariableTextView;
        public final Button mBoolButton;

        public BoolViewHolder(View view) {
            super(view);
            mVariableTextView = view.findViewById(R.id.variableTypeTextView);
            mBoolButton = view.findViewById(R.id.boolButton);
        }
    }

    public class NonBoolViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mVariableTextView;
        public final EditText mValueTextView;
        public final ImageView mDeleteButton;
        public VariableListItem mItem;

        public NonBoolViewHolder(View view) {
            super(view);
            mView = view;
            mVariableTextView = view.findViewById(R.id.variableTypeTextView);
            mValueTextView = view.findViewById(R.id.valueTextView);
            mDeleteButton = view.findViewById(R.id.variableListDeleteButton);

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mValueTextView.setText("");
                }
            });

            mValueTextView.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //
                }

                public void afterTextChanged(Editable s) {
                    // Util.debug("afterTextChanged");
                }

                // TODO: Currently, this function is called any time the user types a single character. Ideally, it should only be called once when the user leaves the details view.
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // if (mValueTextView.getTag() != null) {

                    String input = s.toString();
                    Util.debug("onTextChanged: " + input);

                    if (input.equals("")) {
                        if (mItem.measurement != null) {
                            // 1. Delete measurement
                            mItem.day.removeMeasurement(mItem.measurement);
                            mItem.measurement = null;
                        }
                    } else {
                        float newValue = Float.parseFloat(input);

                        if (mItem.measurement != null) {
                            // 2. Update measurement
                            mItem.measurement.setValue(newValue);
                        } else {
                            // 3. Create measurement
                            Measurement m = new Measurement(newValue, mItem.variable);
                            mItem.day.getMeasurements().add(m);
                            mItem.measurement = m;
                        }
                    }

                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mValueTextView.getText() + "'";
        }
    }
}
