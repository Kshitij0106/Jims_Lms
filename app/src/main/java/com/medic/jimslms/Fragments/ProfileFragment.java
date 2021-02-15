package com.medic.jimslms.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medic.jimslms.R;
import com.medic.jimslms.ViewHolder.ViewPagerAdapter;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    private ImageView goBackProfile, userPicProfile;
    private MaterialTextView userNameProfile, userCourseSemProfile;
    private TabLayout profileTab;
    private ViewPager viewPagerProfile;
    private DatabaseReference userRef;
    private FirebaseAuth auth;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        goBackProfile = view.findViewById(R.id.goBackProfile);
        userPicProfile = view.findViewById(R.id.userPicProfile);
        userNameProfile = view.findViewById(R.id.userNameProfile);
        userCourseSemProfile = view.findViewById(R.id.userCourseSemProfile);
        profileTab = view.findViewById(R.id.profileTab);
        viewPagerProfile = view.findViewById(R.id.viewPagerProfile);

        goBack();
        getUserData();
        setUpTabs(viewPagerProfile);

        return view;
    }

    private void goBack(){
        goBackProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();;
            }
        });
    }

    private void getUserData() {
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uid = user.getUid();

        userRef = FirebaseDatabase.getInstance().getReference("Students").child(uid);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userNameProfile.setText(snapshot.child("userFullName").getValue(String.class));
                String course = snapshot.child("userCourse").getValue(String.class);
                String sem = snapshot.child("userSem").getValue(String.class);
                userCourseSemProfile.setText(course+ " "+sem);

                if(snapshot.child("userPic").exists()){
                    Picasso.get().load(snapshot.child("userPic").getValue(String.class)).fit().centerCrop().into(userPicProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpTabs(ViewPager vp) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addTabs(new DetailsFragment(), "Personal");
        adapter.addTabs(new StudentCollegeDetailsFragment(), "College");

        vp.setAdapter(adapter);
        profileTab.setupWithViewPager(vp);
    }
}