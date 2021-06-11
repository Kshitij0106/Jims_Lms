package com.medic.jimslms.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
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
import com.medic.jimslms.Model.Subject;
import com.medic.jimslms.R;
import com.medic.jimslms.ViewHolder.SubjectViewHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class HomePageFragment extends Fragment {
    private SwipeRefreshLayout swipeLayoutHomePage;
    private MaterialTextView userNameHomePage;
    private TextView noConnectionText, offlineMode, userCourseText, userSemText, userShiftText;
    private ImageView searchSubjectButton, aboutButton;
    private EditText searchSubject;
    private View v1,v2;
    private GridLayout gridLayoutHomePage;
    private MaterialButton clsScheduleButton;
    private CardView cardViewProfile, cardViewMyNotes, cardViewAttendance, cardViewToDo, cardViewAssignment, cardViewFavSubjects;
    private RecyclerView homePageRecyclerView;
    private DatabaseReference userRef, subjectRef;
    private FirebaseRecyclerOptions<Subject> subjectOptions;
    private FirebaseRecyclerAdapter<Subject, SubjectViewHolder> subjectAdapter;
    private FirebaseAuth auth;

    public HomePageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        swipeLayoutHomePage = view.findViewById(R.id.swipeLayoutHomePage);
        userNameHomePage = view.findViewById(R.id.userNameHomePage);
        searchSubject = view.findViewById(R.id.searchSubject);
        searchSubjectButton = view.findViewById(R.id.searchSubjectButton);
        clsScheduleButton = view.findViewById(R.id.clsScheduleButton);
        homePageRecyclerView = view.findViewById(R.id.homePageRecyclerView);

        cardViewProfile = view.findViewById(R.id.cardViewProfile);
        aboutButton = view.findViewById(R.id.aboutButton);
        cardViewMyNotes = view.findViewById(R.id.cardViewMyNotes);
        cardViewAttendance = view.findViewById(R.id.cardViewAttendance);
        cardViewToDo = view.findViewById(R.id.cardViewToDo);
        cardViewAssignment = view.findViewById(R.id.cardViewAssignment);
        cardViewFavSubjects = view.findViewById(R.id.cardViewFavSubjects);

        v1 = view.findViewById(R.id.v1);
        v2 = view.findViewById(R.id.v2);
        gridLayoutHomePage = view.findViewById(R.id.gridLayoutHomePage);

        noConnectionText = view.findViewById(R.id.noConnectionText);
        offlineMode = view.findViewById(R.id.offlineMode);

        userCourseText = view.findViewById(R.id.userCourseText);
        userSemText = view.findViewById(R.id.userSemText);
        userShiftText = view.findViewById(R.id.userShiftText);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uid = user.getUid();

        swipeLayoutHomePage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUserInfo(uid);
                searchSubject();
                goTo();
            }
        });
        if(Connectivity.isConnectedToInternet(getContext())) {
            getUserInfo(uid);
            searchSubject();
            goTo();
        }else{
            noConnectionText.setVisibility(View.VISIBLE);
            offlineMode.setVisibility(View.VISIBLE);
            homePageRecyclerView.setVisibility(View.GONE);
            clsScheduleButton.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            gridLayoutHomePage.setVisibility(View.GONE);
        }

        return view;
    }

    private void getUserInfo(String uid) {
        if (Connectivity.isConnectedToInternet(getActivity())) {
            swipeLayoutHomePage.setRefreshing(false);
            userRef = FirebaseDatabase.getInstance().getReference("Students").child(uid);
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child("userFullName").exists()) {
                        String name = snapshot.child("userFullName").getValue().toString();

                        char ch = ' ';
                        String userName = " ";
                        for (int i = 0; i < name.length(); i++) {
                            ch = name.charAt(i);
                            if (Character.isWhitespace(ch)) {
                                break;
                            } else {
                                userName += ch;
                            }
                        }
                        userNameHomePage.setText("Hello " + userName);
                    }

                    userCourseText.setText(snapshot.child("userCourse").getValue().toString());
                    userShiftText.setText(snapshot.child("userShift").getValue().toString());
                    userSemText.setText(snapshot.child("userSem").getValue().toString());
                    loadSubjects("");
                    goToCollegeInfo();
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

    private void loadSubjects(String s) {
        if (Connectivity.isConnectedToInternet(getActivity())) {
            swipeLayoutHomePage.setRefreshing(false);
            String course = userCourseText.getText().toString();
            String sem = userSemText.getText().toString();
            String shift = userShiftText.getText().toString();

            Query query = FirebaseDatabase.getInstance().getReference("Courses").child(course).child(sem).child("Subjects").orderByChild("subjectName").startAt(s).endAt(s + "\uf8ff");
            subjectOptions = new FirebaseRecyclerOptions.Builder<Subject>().setQuery(query, Subject.class).build();
            subjectAdapter = new FirebaseRecyclerAdapter<Subject, SubjectViewHolder>(subjectOptions) {
                @Override
                protected void onBindViewHolder(@NonNull SubjectViewHolder holder, int position, @NonNull Subject model) {
                    holder.courseNameCardView.setText(model.getSubjectName());
                    holder.courseDescCardView.setText(model.getSubjectDescription());

                    holder.cardViewCourses.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle subjectNameBundle = new Bundle();
                            subjectNameBundle.putString("subjectName", holder.courseNameCardView.getText().toString());
                            subjectNameBundle.putString("userCourse", course);
                            subjectNameBundle.putString("userShift", shift);
                            SubjectFragment subFrag = new SubjectFragment();
                            subFrag.setArguments(subjectNameBundle);
                            getFragmentManager().beginTransaction().replace(R.id.mainActivity, subFrag).addToBackStack(" ").commit();
                        }
                    });
                }

                @NonNull
                @Override
                public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_layout, parent, false);
                    SubjectViewHolder svh = new SubjectViewHolder(view);
                    return svh;
                }
            };
            homePageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            homePageRecyclerView.setNestedScrollingEnabled(false);
            homePageRecyclerView.setAdapter(subjectAdapter);
            subjectAdapter.startListening();
        } else {
            Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void goTo() {
        clsScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.mainActivity, new TodayScheduleFragment()).addToBackStack(" ").commit();
            }
        });

        cardViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.mainActivity, new ProfileFragment()).addToBackStack(" ").commit();
            }
        });

        cardViewMyNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle noteBundle = new Bundle();
                noteBundle.putString("showType", "All");
                MyNotesFragment notesFragment = new MyNotesFragment();
                notesFragment.setArguments(noteBundle);
                getFragmentManager().beginTransaction().replace(R.id.mainActivity, notesFragment).addToBackStack(" ").commit();
            }
        });

        cardViewToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.mainActivity, new ToDoFragment()).addToBackStack(" ").commit();
            }
        });
    }

    private void searchSubject() {
        if (Connectivity.isConnectedToInternet(getContext())) {
            searchSubjectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String subjName = searchSubject.getText().toString();
                    if (subjName.length() > 0 && subjName != null) {
                        String first = String.valueOf(subjName.charAt(0)).toUpperCase();
                        String rem = subjName.substring(1);
                        String searchSubject = first + rem;
                        loadSubjects(searchSubject);
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToCollegeInfo() {
        String course = userCourseText.getText().toString();
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle cName = new Bundle();
                cName.putString("courseName", course);
                AboutUsFragment aboutUsFragment = new AboutUsFragment();
                aboutUsFragment.setArguments(cName);
                getFragmentManager().beginTransaction().replace(R.id.mainActivity, aboutUsFragment).addToBackStack(" ").commit();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (subjectAdapter != null)
            subjectAdapter.startListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (subjectAdapter != null)
            subjectAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (subjectAdapter != null)
            subjectAdapter.stopListening();
    }
}