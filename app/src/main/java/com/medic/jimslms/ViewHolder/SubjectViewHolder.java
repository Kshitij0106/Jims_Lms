package com.medic.jimslms.ViewHolder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.medic.jimslms.R;

public class SubjectViewHolder extends RecyclerView.ViewHolder {
    public CardView cardViewCourses;
    public ImageView coursePicCardView;
    public MaterialTextView courseNameCardView,courseDescCardView;

    public SubjectViewHolder(@NonNull View itemView) {
        super(itemView);
        cardViewCourses = itemView.findViewById(R.id.cardViewCourses);
        coursePicCardView = itemView.findViewById(R.id.coursePicCardView);
        courseNameCardView = itemView.findViewById(R.id.courseNameCardView);
        courseDescCardView = itemView.findViewById(R.id.courseDescCardView);
    }
}
