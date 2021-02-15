package com.medic.jimslms.ViewHolder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.medic.jimslms.R;

public class ToDoViewHolder extends RecyclerView.ViewHolder {
    public CardView toDoCardView;
    public MaterialTextView titleToDo,priorityToDo,textToDo;

    public ToDoViewHolder(@NonNull View itemView) {
        super(itemView);

        toDoCardView = itemView.findViewById(R.id.toDoCardView);
        titleToDo = itemView.findViewById(R.id.titleToDo);
        priorityToDo = itemView.findViewById(R.id.priorityToDo);
        textToDo = itemView.findViewById(R.id.textToDo);
    }
}
