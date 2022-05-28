package com.arup.yahmoney;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class NumberListContactAdapter extends RecyclerView.Adapter<NumberListContactAdapter.ViewHolder>{
    User user;
    final LinkedList<String> list;
    final Context context;
    public NumberListContactAdapter(User user, LinkedList<String> list, Context context) {
        this.user = user;
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recylerview_numbers_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int index = holder.getAdapterPosition();
        holder.button.setText(list.get(index));
        holder.button.setOnClickListener(v -> {
            user.changePhone(list.get(index));
            startActivity();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final Button button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.number_button);
        }
    }

    private void startActivity() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("NewUserNameFromContact", user.getName());
        intent.putExtra("NewUserContactFromContact", user.getPhone());
        context.startActivity(intent);
    }
}
