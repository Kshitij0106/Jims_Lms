package com.medic.jimslms.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.medic.jimslms.Common.Connectivity;
import com.medic.jimslms.Model.Notes;
import com.medic.jimslms.Model.Subject;
import com.medic.jimslms.R;
import com.medic.jimslms.ViewHolder.FilterSubjectViewHolder;
import com.medic.jimslms.ViewHolder.MyNoteViewHolder;

public class MyNotesFragment extends Fragment {
    private ImageView goBackMyNotesFragment, noteSearchButton;
    private TextInputEditText noteSearch;
    private MaterialTextView addNewMyNoteButton;
    private RecyclerView filterSubjectsRecyclerView, recyclerViewMyNotes;
    private FirebaseRecyclerOptions<Notes> notesOptions;
    private FirebaseRecyclerAdapter<Notes, MyNoteViewHolder> notesAdapter;
    private FirebaseAuth auth;
    private DatabaseReference userRef, subjectRef;
    private FirebaseRecyclerOptions<Subject> subjectOptions;
    private FirebaseRecyclerAdapter<Subject, FilterSubjectViewHolder> subjectAdapter;
    private static int SHOW = 0;

    public MyNotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_notes, container, false);

        goBackMyNotesFragment = view.findViewById(R.id.goBackMyNotesFragment);
        filterSubjectsRecyclerView = view.findViewById(R.id.filterSubjectsRecyclerView);
        recyclerViewMyNotes = view.findViewById(R.id.recyclerViewMyNotes);
        addNewMyNoteButton = view.findViewById(R.id.addNewMyNoteButton);
        noteSearch = view.findViewById(R.id.noteSearch);
        noteSearchButton = view.findViewById(R.id.noteSearchButton);

        Bundle subjectBundle = this.getArguments();
        String showType = subjectBundle.getString("showType");

        if (showType.equals("showSubject")) {
            String SubjectName = subjectBundle.getString("SubjectName");
            showNotes(SubjectName,"Subject");
        }else{
            showNotes("", "");
        }

        searchNotes();
        getUserInfo();
        addNewNote();
        goBack();

        return view;
    }

    private void goBack() {
        goBackMyNotesFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void getUserInfo() {
        if (Connectivity.isConnectedToInternet(getActivity())) {
            auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();
            String uid = user.getUid();

            userRef = FirebaseDatabase.getInstance().getReference("Students").child(uid);
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String course = snapshot.child("userCourse").getValue(String.class);
                    String sem = snapshot.child("userSem").getValue(String.class);
                    getSubjectList(course, sem);
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

    private void getSubjectList(String course, String sem) {
        subjectRef = FirebaseDatabase.getInstance().getReference("Courses").child(course).child(sem).child("Subjects");
        subjectOptions = new FirebaseRecyclerOptions.Builder<Subject>().setQuery(subjectRef, Subject.class).build();
        subjectAdapter = new FirebaseRecyclerAdapter<Subject, FilterSubjectViewHolder>(subjectOptions) {
            @Override
            protected void onBindViewHolder(@NonNull FilterSubjectViewHolder holder, int position, @NonNull Subject model) {
                holder.filterSubjectName.setText(model.getSubjectName());

                holder.filterSubjectCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showNotes(holder.filterSubjectName.getText().toString(), "Subject");
                    }
                });
            }

            @NonNull
            @Override
            public FilterSubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_subject_layout, parent, false);
                return new FilterSubjectViewHolder(view);
            }
        };
        filterSubjectsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        filterSubjectsRecyclerView.setAdapter(subjectAdapter);
        subjectAdapter.startListening();
    }

    private void addNewNote() {
        addNewMyNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle noteBundle = new Bundle();
                noteBundle.putString("task", "Add");
                Add_ShowNotesFragment addNoteFragment = new Add_ShowNotesFragment();
                addNoteFragment.setArguments(noteBundle);
                getFragmentManager().beginTransaction().replace(R.id.mainActivity, addNoteFragment).addToBackStack(" ").commit();
            }
        });
    }

    private void searchNotes() {
        if (Connectivity.isConnectedToInternet(getActivity())) {
            noteSearchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String noteSearchText = noteSearch.getText().toString();
                    if(noteSearchText!=null && noteSearchText.length()>0){
                        String first = String.valueOf(noteSearchText.charAt(0)).toUpperCase();
                        String rem = noteSearchText.substring(1);
                        String searchText = first+rem;
                        showNotes(searchText, "Note");
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void showNotes(String noteSearchText, String searchType) {
        if (Connectivity.isConnectedToInternet(getActivity())) {
            auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();
            String uid = user.getUid();
            String searchChild = "noteTitle";

            if (searchType.equals("Subject")) {
                searchChild = "noteSubject";
            }

            Query searchQuery = FirebaseDatabase.getInstance().getReference("Students").child(uid).
                    child("My Notes").orderByChild(searchChild).startAt(noteSearchText).endAt(noteSearchText + "\uf8ff");
            notesOptions = new FirebaseRecyclerOptions.Builder<Notes>().setQuery(searchQuery, Notes.class).build();
            notesAdapter = new FirebaseRecyclerAdapter<Notes, MyNoteViewHolder>(notesOptions) {
                @Override
                protected void onBindViewHolder(@NonNull MyNoteViewHolder holder, int position, @NonNull Notes model) {
                    holder.titleMyNotes.setText(model.getNoteTitle());
                    holder.subjectMyNotes.setText("Subject: " + model.getNoteSubject());
                    holder.textMyNotes.setText(model.getNoteText());
                    holder.notesCardViewLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle notesDataBundle = new Bundle();
                            notesDataBundle.putString("task", "Show");
                            notesDataBundle.putString("id", model.getNoteId());
                            Add_ShowNotesFragment showNotesFragment = new Add_ShowNotesFragment();
                            showNotesFragment.setArguments(notesDataBundle);
                            getFragmentManager().beginTransaction().replace(R.id.mainActivity, showNotesFragment).addToBackStack("").commit();
                        }
                    });
                }

                @NonNull
                @Override
                public MyNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_notes_layout, parent, false);
                    MyNoteViewHolder nvh = new MyNoteViewHolder(view);
                    return nvh;
                }
            };
            recyclerViewMyNotes.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerViewMyNotes.setAdapter(notesAdapter);
            notesAdapter.startListening();
        } else {
            Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (notesAdapter != null && subjectAdapter != null) {
            notesAdapter.startListening();
            subjectAdapter.startListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (notesAdapter != null && subjectAdapter != null) {
            notesAdapter.startListening();
            subjectAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (notesAdapter != null && subjectAdapter != null) {
            notesAdapter.stopListening();
            subjectAdapter.startListening();
        }
    }
}