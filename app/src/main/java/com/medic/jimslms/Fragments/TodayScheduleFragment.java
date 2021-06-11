package com.medic.jimslms.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.medic.jimslms.Model.Subject;
import com.medic.jimslms.R;
import com.medic.jimslms.ViewHolder.ScheduleViewHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TodayScheduleFragment extends Fragment {
    private ImageView goBackScheduleFragment;
    private RecyclerView scheduleRecyclerView;
    private DatabaseReference scheduleRef;
    private FirebaseRecyclerOptions<Subject> scheduleOptions;
    private FirebaseRecyclerAdapter<Subject, ScheduleViewHolder> scheduleAdapter;

    public TodayScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_schedule, container, false);

        goBackScheduleFragment = view.findViewById(R.id.goBackScheduleFragment);
        scheduleRecyclerView = view.findViewById(R.id.scheduleRecyclerView);

        goBack();
        loadSchedule();

        return view;
    }

    private void goBack(){
        goBackScheduleFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void loadSchedule(){
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMM");
        String today = sdf.format(date);
        scheduleRef = FirebaseDatabase.getInstance().getReference("Courses").child("MCA").child("1").child("Schedule").child("1302");
        scheduleOptions = new FirebaseRecyclerOptions.Builder<Subject>().setQuery(scheduleRef,Subject.class).build();
        scheduleAdapter = new FirebaseRecyclerAdapter<Subject, ScheduleViewHolder>(scheduleOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position, @NonNull Subject model) {
                holder.subjectNameSchedule.setText(model.getSubjectName());
                holder.subjectTimeSchedule.setText(model.getSubjectTime());
                String url = model.getSubjectLink();
                holder.shareLinkSchedule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String shareTextSubject = "Join "+model.getSubjectName()+" Class starting at "
                                +model.getSubjectTime()+" from our JIMS LMS APP through this link:";
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT,shareTextSubject+"\n"+url);
                        startActivity(Intent.createChooser(intent,"Share Link Via."));
                    }
                });
                holder.subjectLinkSchedule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setPackage("us.zoom.videomeetings");
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_schedule_layout,parent,false);
                ScheduleViewHolder svh = new ScheduleViewHolder(view);
                return svh;
            }
        };

        scheduleRecyclerView.setHasFixedSize(true);
        scheduleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        scheduleRecyclerView.setAdapter(scheduleAdapter);
        scheduleAdapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(scheduleAdapter!=null)
            scheduleAdapter.startListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(scheduleAdapter!=null)
            scheduleAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(scheduleAdapter!=null)
            scheduleAdapter.stopListening();
    }
}