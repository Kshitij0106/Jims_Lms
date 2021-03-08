package com.medic.jimslms.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medic.jimslms.Common.Connectivity;
import com.medic.jimslms.R;

public class StudentCollegeDetailsFragment extends Fragment {
    private CardView cardViewNotesCollege, cardViewToDoCollege;
    private TextView noOfMyNotes, noOfToDo;
    private DatabaseReference notesRef, todoRef;
    private FirebaseAuth auth;

    public StudentCollegeDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_college_details, container, false);

        cardViewNotesCollege = view.findViewById(R.id.cardViewNotesCollege);
        cardViewToDoCollege = view.findViewById(R.id.cardViewToDoCollege);
        noOfMyNotes = view.findViewById(R.id.noOfMyNotes);
        noOfToDo = view.findViewById(R.id.noOfToDo);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uid = user.getUid();

        getNoOfMyNotes(uid);
        getNoOfToDo(uid);

        cardViewNotesCollege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle noteBundle = new Bundle();
                noteBundle.putString("showType", "All");
                MyNotesFragment notesFragment = new MyNotesFragment();
                notesFragment.setArguments(noteBundle);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.mainActivity, notesFragment).addToBackStack(" ").commit();
            }
        });

        cardViewToDoCollege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.mainActivity, new ToDoFragment()).addToBackStack(" ").commit();
            }
        });

        return view;
    }

    private void getNoOfMyNotes(String uid) {
        if (Connectivity.isConnectedToInternet(getActivity())) {
            notesRef = FirebaseDatabase.getInstance().getReference("Students").child(uid).child("My Notes");
            notesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    noOfMyNotes.setText("My Notes: " + snapshot.getChildrenCount());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void getNoOfToDo(String uid) {
        if (Connectivity.isConnectedToInternet(getActivity())) {
            todoRef = FirebaseDatabase.getInstance().getReference("Students").child(uid).child("To Do");
            todoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    noOfToDo.setText("To Do Tasks: " + snapshot.getChildrenCount());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
        }

    }
}