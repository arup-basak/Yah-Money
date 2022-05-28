package com.arup.yahmoney;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arup.yahmoney.ChatSystem.Chat;
import com.arup.yahmoney.ChatSystem.Message;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {
    private final Context context;
    private final Chat chat;

    public ViewAdapter(Chat chat, Context context){
        this.chat = chat;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.right_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        int index = viewHolder.getAdapterPosition();
        Message message = chat.get(index);
        viewHolder.amountView.setText(message.getMessage());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return chat.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView amountView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amountView = itemView.findViewById(R.id.amount);
        }
    }
}
