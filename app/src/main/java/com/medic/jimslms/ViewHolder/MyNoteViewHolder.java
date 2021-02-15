package com.medic.jimslms.ViewHolder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.medic.jimslms.R;

public class MyNoteViewHolder extends RecyclerView.ViewHolder {
    public MaterialTextView titleMyNotes, subjectMyNotes, textMyNotes;
    public CardView notesCardViewLayout;

    public MyNoteViewHolder(@NonNull View itemView) {
        super(itemView);

        notesCardViewLayout = itemView.findViewById(R.id.notesCardViewLayout);
        titleMyNotes = itemView.findViewById(R.id.titleMyNotes);
        subjectMyNotes = itemView.findViewById(R.id.subjectMyNotes);
        textMyNotes = itemView.findViewById(R.id.textMyNotes);
    }
}
