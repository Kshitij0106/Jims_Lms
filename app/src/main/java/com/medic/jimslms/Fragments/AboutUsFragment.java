package com.medic.jimslms.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.medic.jimslms.R;

public class AboutUsFragment extends Fragment {

    public AboutUsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);

        Bundle cName = this.getArguments();
        String courseName = cName.getString("courseName");
        Toast.makeText(getContext(), courseName, Toast.LENGTH_SHORT).show();

        return view;
    }
}