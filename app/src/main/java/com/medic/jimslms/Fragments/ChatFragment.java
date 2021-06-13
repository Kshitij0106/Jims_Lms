package com.medic.jimslms.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medic.jimslms.Common.Connectivity;
import com.medic.jimslms.Model.Message;
import com.medic.jimslms.R;
import com.medic.jimslms.ViewHolder.MessageAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatFragment extends Fragment {
    private ImageView goBackMyTeacherFragment, attachFileChatFragment, sendMessageButton;
    private EditText textMessageChatFragment;
    private TextView teacherNameChatFragment;
    private RecyclerView recyclerViewChatFragment;
    private DatabaseReference messageRef;
    private List<Message> messageList;
    private FirebaseAuth auth;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        Bundle teacherIdBundle = this.getArguments();
        String teacherId = teacherIdBundle.getString("tid");
        String teacherName = teacherIdBundle.getString("tName");

        goBackMyTeacherFragment = view.findViewById(R.id.goBackMyTeacherFragment);
        teacherNameChatFragment = view.findViewById(R.id.teacherNameChatFragment);
        teacherNameChatFragment.setText(teacherName);
        textMessageChatFragment = view.findViewById(R.id.textMessageChatFragment);
        attachFileChatFragment = view.findViewById(R.id.attachFileChatFragment);
        sendMessageButton = view.findViewById(R.id.sendMessageChatFragment);
        sendMessageButton.setEnabled(false);
        recyclerViewChatFragment = view.findViewById(R.id.recyclerViewChatFragment);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uid = user.getUid();

        sendMessage(uid, teacherId);
        getMessage(uid, teacherId);

        goBack();

        return view;
    }

    private void goBack() {
        goBackMyTeacherFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void sendMessage(String senderId, String receiverId) {
        textMessageChatFragment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                sendMessageButton.setEnabled(true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sendMessageButton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                sendMessageButton.setEnabled(true);
            }
        });
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((Connectivity.isConnectedToInternet(getActivity()))) {

                    messageRef = FirebaseDatabase.getInstance().getReference("Messages");
                    String txt = textMessageChatFragment.getText().toString();
                    if (!txt.isEmpty()) {
                        HashMap<String, Object> messageMap = new HashMap<>();
                        messageMap.put("senderId", senderId);
                        messageMap.put("receiverId", receiverId);
                        messageMap.put("textMessage", txt);
                        messageMap.put("messageType", "text");
                        messageMap.put("messageStatus", "unseen");

                        messageRef.push().setValue(messageMap);
                        textMessageChatFragment.setText("");
                    }
                } else {
                    Toast.makeText(getContext(), "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getMessage(String uid, String teacherId) {
        if (Connectivity.isConnectedToInternet(getActivity())) {
            messageList = new ArrayList<>();
            messageRef = FirebaseDatabase.getInstance().getReference("Messages");
            messageRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    messageList.clear();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Message msg = data.getValue(Message.class);
                        if (msg.getSenderId().equals(uid) && msg.getReceiverId().equals(teacherId) ||
                                msg.getSenderId().equals(teacherId) && msg.getReceiverId().equals(uid)) {
                            messageList.add(msg);
                        }
                        MessageAdapter adapter = new MessageAdapter(messageList, getContext());
                        recyclerViewChatFragment.setHasFixedSize(true);
                        LinearLayoutManager llm = new LinearLayoutManager(getContext());
                        llm.scrollToPosition(messageList.size() - 1);
                        recyclerViewChatFragment.setLayoutManager(llm);
                        recyclerViewChatFragment.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}