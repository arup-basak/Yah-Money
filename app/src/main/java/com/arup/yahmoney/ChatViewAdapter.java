package com.arup.yahmoney;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arup.yahmoney.Library.ChatSystem.Chat;
import com.arup.yahmoney.Library.ChatSystem.Message;

public class ChatViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    Chat chat;
    private static final int RIGHT_SIDE = 1;
    private static final int LEFT_SIDE = 2;

    public ChatViewAdapter(Context context, Chat chat) {
        this.context = context;
        this.chat = chat;
    }

    private class MessageInViewHolder extends RecyclerView.ViewHolder {
        final TextView timeView;
        final TextView messageTextView;

        MessageInViewHolder(final View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.amount);
            timeView = itemView.findViewById(R.id.time);
        }
        void bind(int position) {
            Message message = chat.get(position);
            messageTextView.setText(message.getMessage());
            timeView.setText(message.getTime());
        }
    }

    private class MessageOutViewHolder extends RecyclerView.ViewHolder {
        final TextView messageTextView;
        final TextView timeView;

        MessageOutViewHolder(final View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.amount);
            timeView = itemView.findViewById(R.id.time);
        }
        void bind(int position) {
            Message message = chat.get(position);
            messageTextView.setText(message.getMessage());
            timeView.setText(message.getTime());
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LEFT_SIDE) {
            return new MessageInViewHolder(LayoutInflater.from(context).inflate(R.layout.side_left_chat, parent, false));
        }
        else {
            return new MessageOutViewHolder(LayoutInflater.from(context).inflate(R.layout.side_right_chat, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (!chat.side(position)) {
            ((MessageOutViewHolder) holder).bind(position);
        } else {
            ((MessageInViewHolder) holder).bind(position);
        }
    }

    @Override
    public int getItemCount() {
        try {
            return chat.size();
        }
        catch (Exception e){
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(!chat.side(position)) {
            return RIGHT_SIDE;
        }
        else {
            return LEFT_SIDE;
        }
    }
}
