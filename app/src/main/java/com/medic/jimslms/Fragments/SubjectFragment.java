package com.medic.jimslms.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medic.jimslms.R;
import com.squareup.picasso.Picasso;

public class SubjectFragment extends Fragment {
    private ImageView goBackSubjectFragment, imageSubjectTeacher;
    private TextView subjectNameSubjectFragment, nameSubjectTeacher, mailSubjectTeacher, contactTeacherButton;
    private MaterialTextView syllabusSubjectFragment, subjectNotesSubjectFragment,
            myNotesSubjectFragment, assignmentSubjectFragment, attendanceSubjectFragment;
    private DatabaseReference subjectRef, teacherRef;

    public SubjectFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subject, container, false);

        goBackSubjectFragment = view.findViewById(R.id.goBackSubjectFragment);
        subjectNameSubjectFragment = view.findViewById(R.id.subjectNameSubjectFragment);
        syllabusSubjectFragment = view.findViewById(R.id.syllabusSubjectFragment);
        subjectNotesSubjectFragment = view.findViewById(R.id.subjectNotesSubjectFragment);
        myNotesSubjectFragment = view.findViewById(R.id.myNotesSubjectFragment);
        assignmentSubjectFragment = view.findViewById(R.id.assignmentSubjectFragment);
        attendanceSubjectFragment = view.findViewById(R.id.attendanceSubjectFragment);

        imageSubjectTeacher = view.findViewById(R.id.imageSubjectTeacher);
        nameSubjectTeacher = view.findViewById(R.id.nameSubjectTeacher);
        mailSubjectTeacher = view.findViewById(R.id.mailSubjectTeacher);
        contactTeacherButton = view.findViewById(R.id.contactTeacherButton);

        Bundle subjectBundle = this.getArguments();
        String subjectName = subjectBundle.getString("subjectName");
        String course = subjectBundle.getString("userCourse");
        String sem = subjectBundle.getString("userShift");

        subjectNameSubjectFragment.setText(subjectName);

        goBack();
        getSubjectInfo(course,sem,subjectName);
        subjectNotes(course,sem,subjectName);
        mySubjectNotes();
        subjectAssignment(course,sem,subjectName);
        teacherInfo(course,sem,subjectName);

        return view;
    }

    private void goBack() {
        goBackSubjectFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void getSubjectInfo(String course,String sem,String sName) {
        syllabusSubjectFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subjectRef = FirebaseDatabase.getInstance().getReference("Courses").child(course).child(sem).child("Subjects").child(sName);
                subjectRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String url = snapshot.child("subjectSyllabus").getValue().toString();
                        subjectSyllabus(url);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void subjectSyllabus(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(url),"application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent,"open"));
    }

    private void subjectNotes(String course,String sem,String subjectName){
        subjectNotesSubjectFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSubjectData(course,sem,subjectName,"Notes");
            }
        });
    }

    private void mySubjectNotes() {
        myNotesSubjectFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle subjNameBundle = new Bundle();
                String subName = subjectNameSubjectFragment.getText().toString();
                subjNameBundle.putString("SubjectName",subName);
                subjNameBundle.putString("showType","showSubject");
                MyNotesFragment notesFragment = new MyNotesFragment();
                notesFragment.setArguments(subjNameBundle);
                getFragmentManager().beginTransaction().replace(R.id.mainActivity,notesFragment).addToBackStack(" ").commit();
            }
        });
    }

    private void subjectAssignment(String course,String sem,String subjectName){
        assignmentSubjectFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSubjectData(course,sem,subjectName,"Assignment");
            }
        });
    }

    private void sendSubjectData(String course,String sem,String subjectName,String type){
        Bundle subjectDataBundle = new Bundle();
        subjectDataBundle.putString("userCourse",course);
        subjectDataBundle.putString("userSem",sem);
        subjectDataBundle.putString("subjectName",subjectName);
        subjectDataBundle.putString("type",type);

        SubjectNotesFragment notesFragment = new SubjectNotesFragment();
        notesFragment.setArguments(subjectDataBundle);
        getFragmentManager().beginTransaction().replace(R.id.mainActivity,notesFragment).addToBackStack(" ").commit();
    }

    private void teacherInfo(String course,String sem,String sName) {
        teacherRef = FirebaseDatabase.getInstance().getReference("Courses").child(course).child(sem).child("Subjects").child(sName).child("Teacher");
        teacherRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String tName = snapshot.child("teacherName").getValue().toString();
                String tMail = snapshot.child("teacherMail").getValue().toString();
                String tPic = snapshot.child("teacherPic").getValue().toString();

                nameSubjectTeacher.setText(tName);
                mailSubjectTeacher.setText(tMail);
                Picasso.get().load(tPic).into(imageSubjectTeacher);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}