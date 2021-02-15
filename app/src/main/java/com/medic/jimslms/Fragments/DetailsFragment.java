package com.medic.jimslms.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medic.jimslms.Common.Session;
import com.medic.jimslms.MainActivity;
import com.medic.jimslms.R;

public class DetailsFragment extends Fragment {
    private TextView namePersonalDetails, emailPersonalDetails, phnPersonalDetails,
            rollNoPersonalDetails, coursePersonalDetails, semPersonalDetails, shiftPersonalDetails;
    private ImageView editPersonalDetails;
    private MaterialButton logOutButton;
    private DatabaseReference userRef;
    private FirebaseAuth auth;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details_fragment, container, false);

        editPersonalDetails = view.findViewById(R.id.editPersonalDetails);
        namePersonalDetails = view.findViewById(R.id.namePersonalDetails);
        emailPersonalDetails = view.findViewById(R.id.emailPersonalDetails);
        phnPersonalDetails = view.findViewById(R.id.phnPersonalDetails);
        rollNoPersonalDetails = view.findViewById(R.id.rollNoPersonalDetails);
        coursePersonalDetails = view.findViewById(R.id.coursePersonalDetails);
        semPersonalDetails = view.findViewById(R.id.semPersonalDetails);
        shiftPersonalDetails = view.findViewById(R.id.shiftPersonalDetails);
        logOutButton = view.findViewById(R.id.logOutButton);

        getUserData();
        editPersonalDetails();
        logOut();

        return view;
    }

    private void getUserData() {
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uid = user.getUid();

        userRef = FirebaseDatabase.getInstance().getReference("Students").child(uid);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                namePersonalDetails.setText(snapshot.child("userFullName").getValue(String.class));
                emailPersonalDetails.setText(snapshot.child("userEmail").getValue(String.class));
                phnPersonalDetails.setText(snapshot.child("userPhone").getValue(String.class));
                coursePersonalDetails.setText(snapshot.child("userCourse").getValue(String.class));
                semPersonalDetails.setText(snapshot.child("userSem").getValue(String.class));
                shiftPersonalDetails.setText(snapshot.child("userShift").getValue(String.class));
                if (snapshot.child("userEnrollmentNo").exists()) {
                    rollNoPersonalDetails.setText(snapshot.child("userEnrollmentNo").getValue(String.class));
                } else {
                    rollNoPersonalDetails.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editPersonalDetails() {
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uid = user.getUid();
        userRef = FirebaseDatabase.getInstance().getReference("Students").child(uid);

        Dialog editDialog = new Dialog(getContext());
        editDialog.setContentView(R.layout.edit_details_layout);

        TextInputEditText editNamePersonalDetails, editPhonePersonalDetails;
        MaterialButton saveEditedDetails;

        editPhonePersonalDetails = editDialog.findViewById(R.id.editPhonePersonalDetails);
        editNamePersonalDetails = editDialog.findViewById(R.id.editNamePersonalDetails);
        saveEditedDetails = editDialog.findViewById(R.id.saveEditedDetails);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                editNamePersonalDetails.setText(snapshot.child("userFullName").getValue(String.class));
                editPhonePersonalDetails.setText(snapshot.child("userPhone").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        saveEditedDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editName = editNamePersonalDetails.getText().toString();
                String editPhn = editPhonePersonalDetails.getText().toString();

                if (editName.isEmpty() && editPhn.isEmpty()) {
                    editNamePersonalDetails.setError("Can't be Empty");
                    editPhonePersonalDetails.setError("Can't be Empty");
                } else if (editName.isEmpty()) {
                    editNamePersonalDetails.setError("Can't be Empty");
                } else if (editPhn.isEmpty()) {
                    editPhonePersonalDetails.setError("Can't be Empty");
                } else {
                    userRef.child("userFullName").setValue(editName);
                    userRef.child("userPhone").setValue(editPhn);

                    editDialog.dismiss();
                    Toast.makeText(getContext(), "Details Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
        editPersonalDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.show();
            }
        });
    }

    private void logOut() {
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder logOutDialog = new AlertDialog.Builder(getContext());
                logOutDialog.setMessage("Log Out !");
                logOutDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Session session = new Session(getContext());
                        session.setLoggedIn(false);

                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Toast.makeText(getActivity(), "Log Out Successful", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                logOutDialog.show();
            }
        });
    }

}