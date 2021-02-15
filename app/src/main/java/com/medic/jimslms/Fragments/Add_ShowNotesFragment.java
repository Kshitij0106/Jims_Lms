package com.medic.jimslms.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medic.jimslms.Common.Connectivity;
import com.medic.jimslms.Model.Notes;
import com.medic.jimslms.Model.Subject;
import com.medic.jimslms.R;

import java.util.ArrayList;
import java.util.List;

public class Add_ShowNotesFragment extends Fragment {
    private ImageView goBackAddNotes, verticalMenuButtonNotes;
    private TextInputEditText titleAddNote, textAddNote;
    private MaterialTextView showSubjectNoteFragment;
    private Spinner subjectAddNote;
    private MaterialTextView cancelAddNote, saveAddNote;
    private DatabaseReference notesRef, userRef, subjectRef;
    private FirebaseAuth auth;
    private List<String> subjectsList;

    public Add_ShowNotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add__show_notes, container, false);

        goBackAddNotes = view.findViewById(R.id.goBackAddNotes);
        titleAddNote = view.findViewById(R.id.titleAddNote);
        verticalMenuButtonNotes = view.findViewById(R.id.verticalMenuButtonNotes);
        subjectAddNote = view.findViewById(R.id.subjectAddNote);
        showSubjectNoteFragment = view.findViewById(R.id.showSubjectNoteFragment);
        textAddNote = view.findViewById(R.id.textAddNote);
        cancelAddNote = view.findViewById(R.id.cancelAddNote);
        saveAddNote = view.findViewById(R.id.saveAddNote);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uid = user.getUid();

        Bundle noteBundle = this.getArguments();
        String task = noteBundle.getString("task");

        if (task.equals("Add")) {
            subjectAddNote.setVisibility(View.VISIBLE);
            showSubjectNoteFragment.setVisibility(View.GONE);
            getUserInfo(uid);
            addNote(uid);
        } else if (task.equals("Show")) {
            String notedId = noteBundle.getString("id");
            showNote(uid, notedId);
            verticalMenu(uid,notedId);
        }

        goBack();
        cancelNote();

        return view;
    }

    private void goBack() {
        goBackAddNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void cancelNote() {
        cancelAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void getUserInfo(String uid) {
        if (Connectivity.isConnectedToInternet(getActivity())) {
            userRef = FirebaseDatabase.getInstance().getReference("Students").child(uid);
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String course = snapshot.child("userCourse").getValue(String.class);
                    String sem = snapshot.child("userSem").getValue(String.class);
                    getSubjectsList(course, sem);
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

    private void getSubjectsList(String course, String sem) {
        subjectsList = new ArrayList<>();
        subjectsList.add("Select Subject");
        subjectRef = FirebaseDatabase.getInstance().getReference("Courses").child(course).child(sem).child("Subjects");
        subjectRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    subjectsList.add(data.child("subjectName").getValue(String.class));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, subjectsList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subjectAddNote.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNote(String uid) {
        if (Connectivity.isConnectedToInternet(getContext())) {
            saveAddNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notesRef = FirebaseDatabase.getInstance().getReference("Students").child(uid).child("My Notes");

                    String title = titleAddNote.getText().toString();
                    String subject = subjectAddNote.getSelectedItem().toString();
                    String text = textAddNote.getText().toString();

                    if (subjectAddNote.getSelectedItemPosition() == 0 && text.isEmpty()) {
                        Toast.makeText(getContext(), "Select Course", Toast.LENGTH_SHORT).show();
                        textAddNote.setError("Can't be Empty!");
                    } else if (subjectAddNote.getSelectedItemPosition() == 0) {
                        Toast.makeText(getContext(), "Select Course", Toast.LENGTH_SHORT).show();
                    } else if (text.isEmpty()) {
                        textAddNote.setError("Can't be Empty!");
                    } else {
                        String id = String.valueOf(System.currentTimeMillis());
                        if (title.isEmpty()) {
                            String half = id.substring(id.length() - 4);
                            title = "Note" + half;
                        }
                        Notes newNote = new Notes(id, title, subject, text);
                        notesRef.child(id).setValue(newNote);
                        Toast.makeText(getContext(), "Note Saved Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void showNote(String uid, String noteId) {
        if (Connectivity.isConnectedToInternet(getActivity())) {
            notesRef = FirebaseDatabase.getInstance().getReference("Students").child(uid).child("My Notes").child(noteId);
            notesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    titleAddNote.setText(snapshot.child("noteTitle").getValue(String.class));
                    showSubjectNoteFragment.setText(snapshot.child("noteSubject").getValue(String.class));
                    textAddNote.setText(snapshot.child("noteText").getValue(String.class));
                    saveAddNote.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateNote(uid, snapshot.child("noteId").getValue(String.class));
                        }
                    });
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

    private void updateNote(String uid, String noteId) {
        if (Connectivity.isConnectedToInternet(getActivity())) {
            notesRef = FirebaseDatabase.getInstance().getReference("Students").child(uid).child("My Notes").child(noteId);
            Notes updateNote = new Notes(noteId, titleAddNote.getText().toString(), showSubjectNoteFragment.getText().toString(), textAddNote.getText().toString());

            notesRef.setValue(updateNote);
            Toast.makeText(getContext(), "Note Saved Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void verticalMenu(String uid,String noteId) {
        verticalMenuButtonNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(getContext(), v);
                MenuInflater inflater = menu.getMenuInflater();
                inflater.inflate(R.menu.notes_menu, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.deleteMenuButton:
                                deleteNote(uid,noteId);
                                return true;
                            case R.id.saveOfflineMenuButton:
                                return true;
                        }
                        return true;
                    }
                });
                menu.show();
            }
        });
    }

    private void deleteNote(String uid,String noteId) {
        if (Connectivity.isConnectedToInternet(getActivity())) {
            notesRef = FirebaseDatabase.getInstance().getReference("Students").child(uid).child("My Notes").child(noteId);
            AlertDialog.Builder deleteDialog = new AlertDialog.Builder(getContext());
            deleteDialog.setMessage("Delete Note");
            deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    notesRef.removeValue();
                    Bundle bundle = new Bundle();
                    bundle.putString("showType", "All");
                    MyNotesFragment notesFragment = new MyNotesFragment();
                    notesFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.mainActivity,notesFragment).addToBackStack(" ").commit();
                    Toast.makeText(getContext(), "Note Deleted", Toast.LENGTH_SHORT).show();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            deleteDialog.show();
        }else{
            Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

}