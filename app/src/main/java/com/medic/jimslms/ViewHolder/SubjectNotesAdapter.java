package com.medic.jimslms.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.medic.jimslms.Model.Assignment;
import com.medic.jimslms.Model.SubjectNotes;
import com.medic.jimslms.R;

import java.util.List;

public class SubjectNotesAdapter extends RecyclerView.Adapter<SubjectNotesAdapter.SubjectNotesViewHolder> {

    private Context ctx;
    private List<SubjectNotes> notesList;

    public SubjectNotesAdapter(Context ctx, List<SubjectNotes> notesList) {
        this.ctx = ctx;
        this.notesList = notesList;
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectNotesViewHolder holder, int position) {
        holder.unitNotesName.setText(notesList.get(position).getNoteName());
        holder.unitNotesName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(notesList.get(position).getNoteLink()));
                holder.unitNotesName.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public SubjectNotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_name_layout, parent, false);
        return new SubjectNotesViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class SubjectNotesViewHolder extends RecyclerView.ViewHolder {
        public MaterialTextView unitNotesName;

        public SubjectNotesViewHolder(@NonNull View itemView) {
            super(itemView);
            unitNotesName = itemView.findViewById(R.id.unitNotesName);
        }
    }
}
