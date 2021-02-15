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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.medic.jimslms.Model.Assignment;
import com.medic.jimslms.Model.AssignmentUnit;
import com.medic.jimslms.Model.SubjectNotes;
import com.medic.jimslms.Model.SubjectNotesUnit;
import com.medic.jimslms.R;
import com.medic.jimslms.ViewHolder.AssignmentAdapter;
import com.medic.jimslms.ViewHolder.SubjectNotesAdapter;
import com.medic.jimslms.ViewHolder.UnitNotesViewHolder;

import java.util.ArrayList;
import java.util.List;

public class SubjectNotesFragment extends Fragment {
    private ImageView goBackSubjectNotes;
    private TextView subjectNameSubjectNotes;
    private RecyclerView recyclerViewSubjectNotes;
    private DatabaseReference subjectNotesRef, assingNotesRef;
    private FirebaseRecyclerOptions<SubjectNotesUnit> notesOptions;
    private FirebaseRecyclerAdapter<SubjectNotesUnit, UnitNotesViewHolder> notesAdapter;
    private List<SubjectNotes> notesList;
    private FirebaseRecyclerOptions<AssignmentUnit> assignmentOptions;
    private FirebaseRecyclerAdapter<AssignmentUnit, UnitNotesViewHolder> assignmentAdapter;
    private List<Assignment> assignmentList;

    public SubjectNotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subject_notes, container, false);

        goBackSubjectNotes = view.findViewById(R.id.goBackSubjectNotes);
        subjectNameSubjectNotes = view.findViewById(R.id.subjectNameSubjectNotes);
        recyclerViewSubjectNotes = view.findViewById(R.id.recyclerViewSubjectNotes);

        Bundle subjectDetailsBundle = this.getArguments();
        String course = subjectDetailsBundle.getString("userCourse");
        String sem = subjectDetailsBundle.getString("userSem");
        String subjectName = subjectDetailsBundle.getString("subjectName");
        String type = subjectDetailsBundle.getString("type");

        if (type.equals("Notes")) {
            subjectNameSubjectNotes.setText("Notes");
            loadNotesUnits(course, sem, subjectName);
        } else if (type.equals("Assignment")) {
            subjectNameSubjectNotes.setText("Assignment");
            loadAssignmentUnit(course, sem, subjectName);
        }

        goBack();

        return view;
    }

    private void goBack() {
        goBackSubjectNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void loadNotesUnits(String course, String sem, String subjName) {
        notesList = new ArrayList<>();
        subjectNotesRef = FirebaseDatabase.getInstance().getReference("Courses").child(course).
                child(sem).child("Subjects").child(subjName).child("Notes");
        notesOptions = new FirebaseRecyclerOptions.Builder<SubjectNotesUnit>().setQuery(subjectNotesRef, SubjectNotesUnit.class).build();
        notesAdapter = new FirebaseRecyclerAdapter<SubjectNotesUnit, UnitNotesViewHolder>(notesOptions) {
            @Override
            protected void onBindViewHolder(@NonNull UnitNotesViewHolder holder, int position, @NonNull SubjectNotesUnit model) {
                holder.unitNoSubjectNotes.setText(model.getUnitNo());
                notesList = model.getNotesList();

                SubjectNotesAdapter notesAdapter = new SubjectNotesAdapter(getActivity(), notesList);
                holder.recyclerViewUnitNotes.setLayoutManager(new LinearLayoutManager(getContext()));
                holder.recyclerViewUnitNotes.setAdapter(notesAdapter);
            }

            @NonNull
            @Override
            public UnitNotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_notes_layout, parent, false);
                return new UnitNotesViewHolder(view);
            }
        };
        recyclerViewSubjectNotes.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSubjectNotes.setAdapter(notesAdapter);
        notesAdapter.startListening();
    }

    private void loadAssignmentUnit(String course, String sem, String subjName) {
        assignmentList = new ArrayList<>();
        assingNotesRef = FirebaseDatabase.getInstance().getReference("Courses").child(course).
                child(sem).child("Subjects").child(subjName).child("Assignment");
        assignmentOptions = new FirebaseRecyclerOptions.Builder<AssignmentUnit>().setQuery(assingNotesRef, AssignmentUnit.class).build();
        assignmentAdapter = new FirebaseRecyclerAdapter<AssignmentUnit, UnitNotesViewHolder>(assignmentOptions) {
            @Override
            protected void onBindViewHolder(@NonNull UnitNotesViewHolder holder, int position, @NonNull AssignmentUnit model) {
                holder.unitNoSubjectNotes.setText(model.getUnitNo());
                assignmentList = model.getAssignmentList();

                AssignmentAdapter adapter = new AssignmentAdapter(getActivity(),assignmentList);
                holder.recyclerViewUnitNotes.setLayoutManager(new LinearLayoutManager(getContext()));
                holder.recyclerViewUnitNotes.setAdapter(adapter);
            }

            @NonNull
            @Override
            public UnitNotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_notes_layout, parent, false);
                return new UnitNotesViewHolder(view);
            }
        };
        recyclerViewSubjectNotes.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSubjectNotes.setAdapter(assignmentAdapter);
        assignmentAdapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (notesAdapter != null && assignmentAdapter != null) {
            notesAdapter.startListening();
            assignmentAdapter.startListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (notesAdapter != null && assignmentAdapter != null) {
            notesAdapter.startListening();
            assignmentAdapter.startListening();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (notesAdapter != null && assignmentAdapter != null) {
            notesAdapter.stopListening();
            assignmentAdapter.stopListening();
        }
    }
}