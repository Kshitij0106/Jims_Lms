package com.medic.jimslms.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.medic.jimslms.Model.Message;
import com.medic.jimslms.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private FirebaseAuth auth;
    private List<Message> messageList;
    private Context context;

    private static final int MSG_SEND = 1;
    private static final int MSG_RECD = 0;

    public MessageAdapter(List<Message> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(MessageAdapter.MessageViewHolder holder, int position) {
        holder.showMessage.setText(messageList.get(position).getTextMessage());
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_SEND) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sender_message_layout, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receiver_message_layout, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView showMessage;

        public MessageViewHolder(View itemView) {
            super(itemView);
            showMessage = itemView.findViewById(R.id.showMessage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (messageList.get(position).getSenderId().equals(user.getUid())) {
            return MSG_SEND;
        } else {
            return MSG_RECD;
        }
    }
}
