package com.medic.jimslms.ViewHolder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.medic.jimslms.R;

public class ScheduleViewHolder extends RecyclerView.ViewHolder {
    public MaterialTextView subjectNameSchedule, subjectTimeSchedule, subjectLinkSchedule, subjectReminderSchedule, shareLinkSchedule;

    public ScheduleViewHolder(@NonNull View itemView) {
        super(itemView);

        subjectNameSchedule = itemView.findViewById(R.id.subjectNameSchedule);
        subjectTimeSchedule = itemView.findViewById(R.id.subjectTimeSchedule);
        subjectLinkSchedule = itemView.findViewById(R.id.subjectLinkSchedule);
        subjectReminderSchedule = itemView.findViewById(R.id.subjectReminderSchedule);
        shareLinkSchedule = itemView.findViewById(R.id.shareLinkSchedule);

    }
}
