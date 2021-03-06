package com.arup.yahmoney.Adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.arup.yahmoney.ChatPage;
import com.arup.yahmoney.R;

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ViewHolder> {
    private final String[][] localData;
    private final Context context;

    public MainListAdapter(String[][] localData, Context context) {
        this.localData = localData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_main_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        int index = viewHolder.getAdapterPosition();

        viewHolder.nameView.setText(localData[index][0]);
        viewHolder.numbView.setText(localData[index][1]);

        viewHolder.container.setOnClickListener(v -> {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    (Activity) context,
                    viewHolder.container,
                    ViewCompat.getTransitionName(viewHolder.container)
            );

            Intent intent = new Intent(context, ChatPage.class);
            intent.putExtra("IndexFromMainPage", String.valueOf(index));
            context.startActivity(intent, options.toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return localData.length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout container;
        private final TextView nameView;
        private final TextView numbView;
        public ViewHolder(@NonNull View view) {
            super(view);
            container = view.findViewById(R.id.main_page_container);
            nameView = view.findViewById(R.id.main_page_name);
            numbView = view.findViewById(R.id.main_page_amount);
        }
    }
}
