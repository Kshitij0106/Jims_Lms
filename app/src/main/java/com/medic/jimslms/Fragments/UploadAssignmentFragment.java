package com.medic.jimslms.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.medic.jimslms.Common.Connectivity;
import com.medic.jimslms.Model.Assignment;
import com.medic.jimslms.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class UploadAssignmentFragment extends Fragment {
    private TextView subjectNameUploadAssignmentFragment, assignLinkAssignmentFragment, assignNameAssignmentFragment, assignStatusAssignmentFragment,
            assignUploadDateAssignmentFragment, assignDueDateAssignmentFragment;
    private MaterialButton uploadAssignmentButton;
    private ImageView goBackUploadAssignmentFragment;
    private FirebaseAuth auth;
    private DatabaseReference assignRef;
    private StorageReference assignStorage;
    private static final int EXTERNAL_STORAGE_PERMISSION = 1;
    private static final int REQUEST_PDF = 101;

    public UploadAssignmentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_assignment, container, false);

        Bundle assigBundle = this.getArguments();
        String assigName = assigBundle.getString("assigName");
        String assigLink = assigBundle.getString("assigLink");
        String assigUpload = assigBundle.getString("assigUpload");
        String assigDue = assigBundle.getString("assigDue");
        String subjName = assigBundle.getString("subjName");

        goBackUploadAssignmentFragment = view.findViewById(R.id.goBackUploadAssignmentFragment);
        subjectNameUploadAssignmentFragment = view.findViewById(R.id.subjectNameUploadAssignmentFragment);
        assignLinkAssignmentFragment = view.findViewById(R.id.assignLinkAssignmentFragment);
        assignNameAssignmentFragment = view.findViewById(R.id.assignNameAssignmentFragment);
        assignStatusAssignmentFragment = view.findViewById(R.id.assignStatusAssignmentFragment);
        assignUploadDateAssignmentFragment = view.findViewById(R.id.assignUploadDateAssignmentFragment);
        assignDueDateAssignmentFragment = view.findViewById(R.id.assignDueDateAssignmentFragment);
        uploadAssignmentButton = view.findViewById(R.id.uploadAssignment);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uid = user.getUid();
        assignRef = FirebaseDatabase.getInstance().getReference("Students").child(uid).child("Assignments");
        assignStorage = FirebaseStorage.getInstance().getReference("MCA").child(subjName).child("Assignments").child(uid);

        subjectNameUploadAssignmentFragment.setText(subjName);
        assignNameAssignmentFragment.setText(assigName);
        assignUploadDateAssignmentFragment.setText(assigUpload);
        assignDueDateAssignmentFragment.setText(assigDue);

        viewAssignment(assigLink);
        checkUploadDate(assigDue);
        checkAssignmentStatus();
//        checkPermission();
        goBack();

        return view;
    }

    private void goBack() {
        goBackUploadAssignmentFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void viewAssignment(String url) {
        assignLinkAssignmentFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(url), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, "Open"));
            }
        });
    }

    private void checkAssignmentStatus() {
        assignRef.child(subjectNameUploadAssignmentFragment.getText().toString()).child(assignNameAssignmentFragment.getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    assignStatusAssignmentFragment.setText("Uploaded");
                } else {
                    assignStatusAssignmentFragment.setText("Not Uploaded");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUploadDate(String due) {
        uploadAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                Date today = cal.getTime();
                try {
                    Date dueDate = new SimpleDateFormat("dd-MMM-yyyy").parse(due);
                    if (today.before(dueDate)) {
                        checkPermission();
                    } else {
                        Toast.makeText(getContext(), "Time is up", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION);
        } else {
            selectPDF();
        }
    }

    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_PDF);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_PDF && data != null && data.getData() != null) {
            AlertDialog.Builder selectDialog = new AlertDialog.Builder(getContext());
            selectDialog.setMessage("Upload this File");
            selectDialog.setCancelable(false);
            selectDialog.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    uploadAssign(data.getData());
                }
            }).setNegativeButton("Select Another", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectPDF();
                    dialog.dismiss();
                }
            });
            selectDialog.show();
        } else {
            Toast.makeText(getContext(), "Try Again !", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadAssign(Uri data) {
        if (Connectivity.isConnectedToInternet(getActivity())) {
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Uploading Assignment");
            progressDialog.show();

            StorageReference ref = assignStorage.child(System.currentTimeMillis() + ".pdf");
            ref.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();

                    Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                    while (!task.isSuccessful()) ;
                    Uri uri = task.getResult();
                    String url = uri.toString();

                    Assignment assignment = new Assignment();
                    assignment.setAssignmentName(assignNameAssignmentFragment.getText().toString());
                    assignment.setAssignmentLink(url);

                    assignRef.child(subjectNameUploadAssignmentFragment.getText().toString()).child(assignNameAssignmentFragment.getText().toString()).setValue(assignment);

                    Toast.makeText(getContext(), "Assignment Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double prog = ((snapshot.getBytesTransferred() / snapshot.getTotalByteCount()) * 100);
                    progressDialog.setMessage((int) prog + "% Uploaded");
                    progressDialog.show();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkPermission();
            } else {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}