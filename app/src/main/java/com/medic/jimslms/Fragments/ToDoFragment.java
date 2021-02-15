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
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.medic.jimslms.Common.Connectivity;
import com.medic.jimslms.Model.ToDo;
import com.medic.jimslms.R;
import com.medic.jimslms.ViewHolder.ToDoViewHolder;

public class ToDoFragment extends Fragment {
    private ImageView goBackToDoFragment;
    private MaterialTextView addNewToDoButton;
    private DatabaseReference toDoRef;
    private RecyclerView recyclerViewToDo;
    private FirebaseRecyclerOptions<ToDo> toDoOptions;
    private FirebaseRecyclerAdapter<ToDo, ToDoViewHolder> toDoAdapter;
    private FirebaseAuth auth;

    public ToDoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do, container, false);

        goBackToDoFragment = view.findViewById(R.id.goBackToDoFragment);
        recyclerViewToDo = view.findViewById(R.id.recyclerViewToDo);
        addNewToDoButton = view.findViewById(R.id.addNewToDoButton);

        goBack();
        showToDo();
        addNewToDo();

        return view;
    }

    private void goBack() {
        goBackToDoFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void addNewToDo() {
        addNewToDoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle toDoBundle = new Bundle();
                toDoBundle.putString("Task", "Add");
                Add_ShowToDoFragment addToDo = new Add_ShowToDoFragment();
                addToDo.setArguments(toDoBundle);
                getFragmentManager().beginTransaction().replace(R.id.mainActivity, addToDo).addToBackStack(" ").commit();
            }
        });
    }

    private void showToDo() {
        if (Connectivity.isConnectedToInternet(getActivity())) {
            auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();
            String uid = user.getUid();

//            toDoRef = FirebaseDatabase.getInstance().getReference("Students").child(uid).child("To Do").orderByChild("toDoPriority");
            Query todoQuery = FirebaseDatabase.getInstance().getReference("Students").child(uid).child("To Do").orderByChild("toDoPriority");
            toDoOptions = new FirebaseRecyclerOptions.Builder<ToDo>().setQuery(todoQuery, ToDo.class).build();
            toDoAdapter = new FirebaseRecyclerAdapter<ToDo, ToDoViewHolder>(toDoOptions) {
                @Override
                protected void onBindViewHolder(@NonNull ToDoViewHolder holder, int position, @NonNull ToDo model) {
                    holder.titleToDo.setText(model.getToDoTitle());
                    holder.priorityToDo.setText(String.valueOf(model.getToDoPriority()));
                    holder.textToDo.setText(model.getToDoText());
                    holder.toDoCardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle showToDoBundle = new Bundle();
                            showToDoBundle.putString("Task", "Show");
                            showToDoBundle.putString("toDoId", model.getToDoId());
                            Add_ShowToDoFragment toDoFragment = new Add_ShowToDoFragment();
                            toDoFragment.setArguments(showToDoBundle);
                            getFragmentManager().beginTransaction().replace(R.id.mainActivity, toDoFragment).addToBackStack(" ").commit();
                        }
                    });
                }

                @NonNull
                @Override
                public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_do_layout, parent, false);
                    return new ToDoViewHolder(view);
                }
            };
            recyclerViewToDo.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerViewToDo.setAdapter(toDoAdapter);
            toDoAdapter.startListening();
        } else {
            Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (toDoAdapter != null)
            toDoAdapter.startListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (toDoAdapter != null)
            toDoAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (toDoAdapter != null)
            toDoAdapter.stopListening();
    }
}