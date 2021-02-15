package com.medic.jimslms.ViewHolder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.medic.jimslms.R;

public class UnitNotesViewHolder extends RecyclerView.ViewHolder {
    public MaterialTextView unitNoSubjectNotes;
    public RecyclerView recyclerViewUnitNotes;

    public UnitNotesViewHolder(@NonNull View itemView) {
        super(itemView);

        unitNoSubjectNotes = itemView.findViewById(R.id.unitNoSubjectNotes);
        recyclerViewUnitNotes = itemView.findViewById(R.id.recyclerViewUnitNotes);
    }
}
