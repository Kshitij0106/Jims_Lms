package com.medic.jimslms.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.medic.jimslms.R;

public class FilterSubjectViewHolder extends RecyclerView.ViewHolder {
    public TextView filterSubjectName;
    public CardView filterSubjectCardView;

    public FilterSubjectViewHolder(@NonNull View itemView) {
        super(itemView);

        filterSubjectName = itemView.findViewById(R.id.filterSubjectName);
        filterSubjectCardView = itemView.findViewById(R.id.filterSubjectCardView);
    }
}
