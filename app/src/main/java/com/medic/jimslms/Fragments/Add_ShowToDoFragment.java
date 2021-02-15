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
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
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
import com.medic.jimslms.Model.ToDo;
import com.medic.jimslms.R;

public class Add_ShowToDoFragment extends Fragment {
    private ImageView goBackAddToDo, verticalMenuButtonToDo;
    private TextInputEditText titleAddToDo, textAddToDo;
    private MaterialTextView cancelToDo, saveToDo;
    private NumberPicker priorityPicker;
    private DatabaseReference toDoRef;
    private FirebaseAuth auth;

    public Add_ShowToDoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add__show_to_do, container, false);

        goBackAddToDo = view.findViewById(R.id.goBackAddToDo);
        titleAddToDo = view.findViewById(R.id.titleAddToDo);
        verticalMenuButtonToDo = view.findViewById(R.id.verticalMenuButtonToDo);
        textAddToDo = view.findViewById(R.id.textAddToDo);
        cancelToDo = view.findViewById(R.id.cancelToDo);
        saveToDo = view.findViewById(R.id.saveToDo);
        priorityPicker = view.findViewById(R.id.priorityPicker);
        priorityPicker.setMinValue(1);
        priorityPicker.setMaxValue(5);

        Bundle toDoBundle = this.getArguments();
        String task = toDoBundle.getString("Task");

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uid = user.getUid();

        if (task.equals("Add")) {
            addToDo(uid);
        } else if (task.equals("Show")) {
            String toDoId = toDoBundle.getString("toDoId");
            verticalMenu(uid, toDoId);
            showToDo(uid, toDoId);
        }

        goBack();
        cancelToDo();

        return view;
    }

    private void goBack() {
        goBackAddToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void cancelToDo() {
        cancelToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void addToDo(String uid) {
        if (Connectivity.isConnectedToInternet(getActivity())) {
            saveToDo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toDoRef = FirebaseDatabase.getInstance().getReference("Students").child(uid).child("To Do");
                    String title = titleAddToDo.getText().toString();
                    String text = textAddToDo.getText().toString();
                    int priority = priorityPicker.getValue();

                    if (text.isEmpty()) {
                        textAddToDo.setError("Can't be Empty!");
                    } else {
                        String toDoId = String.valueOf(System.currentTimeMillis());
                        if (title.isEmpty()) {
                            title = "ToDo" + toDoId.substring(toDoId.length() - 4);
                        }
                        ToDo newToDo = new ToDo(toDoId, title, text, priority);

                        toDoRef.child(toDoId).setValue(newToDo);
                        Toast.makeText(getContext(), "Saved Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void showToDo(String uid, String toDoId) {
        if (Connectivity.isConnectedToInternet(getActivity())) {
            toDoRef = FirebaseDatabase.getInstance().getReference("Students").child(uid).child("To Do").child(toDoId);
            toDoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    titleAddToDo.setText(snapshot.child("toDoTitle").getValue(String.class));
                    textAddToDo.setText(snapshot.child("toDoText").getValue(String.class));
                    if (snapshot.child("toDoPriority").exists()) {
                        priorityPicker.setValue(snapshot.child("toDoPriority").getValue(int.class));
                    }
                    saveToDo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateToDo(uid, snapshot.child("toDoId").getValue(String.class));
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

    private void updateToDo(String uid, String toDoId) {
        if (Connectivity.isConnectedToInternet(getActivity())) {
            toDoRef = FirebaseDatabase.getInstance().getReference("Students").child(uid).child("To Do").child(toDoId);
            ToDo newToDo = new ToDo(toDoId, titleAddToDo.getText().toString(), textAddToDo.getText().toString(), priorityPicker.getValue());
            toDoRef.setValue(newToDo);
            Toast.makeText(getContext(), "Saved Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void verticalMenu(String uid, String toDoId) {
        verticalMenuButtonToDo.setOnClickListener(new View.OnClickListener() {
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
                                deleteToDo(uid, toDoId);
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

    private void deleteToDo(String uid, String toDoId) {
        if (Connectivity.isConnectedToInternet(getActivity())) {
            toDoRef = FirebaseDatabase.getInstance().getReference("Students").child(uid).child("To Do").child(toDoId);
            AlertDialog.Builder deleteDialog = new AlertDialog.Builder(getContext());
            deleteDialog.setMessage("Delete To Do");
            deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    toDoRef.removeValue();
                    getFragmentManager().beginTransaction().replace(R.id.mainActivity, new ToDoFragment()).addToBackStack(" ").commit();
                    Toast.makeText(getContext(), "Note Deleted", Toast.LENGTH_SHORT).show();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            deleteDialog.show();
        } else {
            Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }
}