package com.medic.jimslms.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.medic.jimslms.R;

public class UploadAssignmentFragment extends Fragment {
    private TextView subjectNameUploadAssignmentFragment,assignNameAssignmentFragment,assignStatusAssignmentFragment,
            assignUploadDateAssignmentFragment,assignDueDateAssignmentFragment;
    private MaterialButton uploadAssignment;
    private ImageView goBackUploadAssignmentFragment;

    public UploadAssignmentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_assignment, container, false);

        Bundle assigBundle = this.getArguments();
        String assigName = assigBundle.getString("assigName");
        String subjName = assigBundle.getString("subjName");

        goBackUploadAssignmentFragment = view.findViewById(R.id.goBackUploadAssignmentFragment);
        subjectNameUploadAssignmentFragment = view.findViewById(R.id.subjectNameUploadAssignmentFragment);
        assignNameAssignmentFragment = view.findViewById(R.id.assignNameAssignmentFragment);

        subjectNameUploadAssignmentFragment.setText(subjName);
        assignNameAssignmentFragment.setText(assigName);

        goBack();

        return view;
    }

    private void goBack(){
        goBackUploadAssignmentFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
}