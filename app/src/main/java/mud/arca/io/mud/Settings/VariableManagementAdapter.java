package mud.arca.io.mud.Settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import mud.arca.io.mud.DataStructures.Variable;
import mud.arca.io.mud.R;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Variable} and makes a call to the
 * specified {@link OnDeleteClickListener} when an item is clicked.
 */
public class VariableManagementAdapter extends FirestoreRecyclerAdapter<Variable, VariableManagementAdapter.ViewHolder> {

    private final OnDeleteClickListener mListener;

    public VariableManagementAdapter(FirestoreRecyclerOptions<Variable> options, OnDeleteClickListener listener) {
        super(options);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new instance of the ViewHolder, in this case we are using a custom layout called R.layout.fragment_datarecord for each item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.variable_management_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Variable model) {
        // Inflate the view from data inside of model
        holder.variable = model;

        // Set UI from model data
        holder.mVariableHeader.setText(model.getName());
        holder.mTypeHeader.setText(model.varTypeToString());

        // Notify any listeners of delete button click
        holder.mDeleteButton.setOnClickListener(v -> {
            if (null != mListener) {
                // Note: Position is assigned when view is bound. Need to get position at time of delete clicked if I want to delete by reference:
                // mListener.onDeleteClick(holder.variable, getSnapshots().getSnapshot(position).getReference());
                mListener.onDeleteClick(holder.variable);
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mVariableHeader;
        final TextView mTypeHeader;
        final View mDeleteButton;
        public Variable variable;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mVariableHeader = view.findViewById(R.id.variable_header);
            mTypeHeader = view.findViewById(R.id.type_header);
            mDeleteButton = view.findViewById(R.id.delete_button);
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Variable variable);
    }
}
