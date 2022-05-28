package com.arup.yahmoney;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arup.yahmoney.ChatSystem.Chat;

import java.util.LinkedList;

public class DateChatsAdapter extends RecyclerView.Adapter<DateChatsAdapter.ViewHolder> {
    final Context context;
    LinkedList<Chat> chats;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_chat_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int index = holder.getAdapterPosition();
        holder.dateTextView.setText(DateToString(chats.get(index).get(0).getDate()));
        ChatViewAdapter adapter = new ChatViewAdapter(context, chats.get(index));
        holder.recyclerView.setAdapter(adapter);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        try {
            holder.recyclerView.scrollToPosition(chats.get(index).size() - 1);
        }
        catch (Exception ignored) {}
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public DateChatsAdapter(Context context, LinkedList<Chat> chats) {
        this.context = context;
        this.chats = chats;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerView recyclerView;
        private final TextView dateTextView;
        public ViewHolder(@NonNull View view) {
            super(view);
            recyclerView = view.findViewById(R.id.chat);
            dateTextView = view.findViewById(R.id.date);
        }
    }

    private static String Month(int time) {
        switch (time) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return null;
        }
    }
    private static String DateToString(String date) {
        String[] arr = date.split("/");
        return arr[0] + " " + Month(Integer.parseInt(arr[1])) + " " + arr[2];
    }
}
