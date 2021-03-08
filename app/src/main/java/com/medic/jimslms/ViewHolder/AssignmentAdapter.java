package com.medic.jimslms.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.medic.jimslms.Model.Assignment;
import com.medic.jimslms.R;

import java.util.List;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.AssignmentViewHolder> {

    private Context ctx;
    private List<Assignment> assignmentList;
    private OnCardClickListener adapterListener;

    public interface OnCardClickListener{
        void onAssignmentClicked(int pos,String name);
    }

    public void setOnCardClickedListener(OnCardClickListener onCardClickListener){
        adapterListener = onCardClickListener;
    }

    public AssignmentAdapter(Context ctx, List<Assignment> assignmentList) {
        this.ctx = ctx;
        this.assignmentList = assignmentList;
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentViewHolder holder, int position) {
        holder.unitNotesName.setText(assignmentList.get(position).getAssignmentName());
    }

    @NonNull
    @Override
    public AssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_name_layout, parent, false);
        return new AssignmentViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return assignmentList.size();
    }

    public class AssignmentViewHolder extends RecyclerView.ViewHolder {
        public MaterialTextView unitNotesName;
        public AssignmentViewHolder(@NonNull View itemView) {
            super(itemView);
            unitNotesName = itemView.findViewById(R.id.unitNotesName);

            unitNotesName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(adapterListener!=null) {
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            adapterListener.onAssignmentClicked(pos,unitNotesName.getText().toString());
                        }
                    }
                }
            });
        }
    }
}
