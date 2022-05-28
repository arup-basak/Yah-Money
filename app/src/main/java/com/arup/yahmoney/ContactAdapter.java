package com.arup.yahmoney;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;


public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private final LinkedList<User> list;
    final Cursor cursor;
    final Context context;
    final ContentResolver cr;
    //LayoutInflater inflater;

    public ContactAdapter(LinkedList<User> list, Cursor cursor, Context context) {
        this.list = list;
        this.cursor = cursor;
        this.context = context;
        this.cr = context.getContentResolver();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_contacts, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        HashSet<String> numberSet = new HashSet<>();
        int index = viewHolder.getAdapterPosition();
        viewHolder.nameView.setText(list.get(index).getName());
        viewHolder.imageView.setImageResource(imageView());
        viewHolder.container.setOnClickListener(v -> {
            Cursor cur = cr.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                    new String[]{list.get(index).getPhone()}, null); //GetPhone is Currently ID

            while (cur.moveToNext()) {
                String number = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if(number.contains("+91")){
                    number = number.replace("+91", "");
                }
                if(number.contains("-")) {
                    number = number.replace("-", "");
                }
                number = number.replace(" ", "");
                numberSet.add(number);
            }
            cur.close();

            /*String[] arr = new String[] {"HEllo", "Arup Basak"};
            showDialog(arr, index);*/

            String[] numbers = Arrays.copyOf(numberSet.toArray(), numberSet.size(), String[].class);

            if(numbers.length != 1) {
                showDialog(numbers, index);
                Toast.makeText(context, numberSet.toString(), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, numbers[0], Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout container;
        private final TextView nameView;
        private final ImageView imageView;
        public ViewHolder(@NonNull View view) {
            super(view);
            container = view.findViewById(R.id.contact_container);
            nameView = view.findViewById(R.id.contact_name);
            imageView = view.findViewById(R.id.contact_image);
        }
    }

    private int imageView() {
        switch ((int) (Math.random() * 6)){
            case 0:
                return R.drawable.mesh0;
            case 1:
                return R.drawable.mesh1;
            case 2:
                return R.drawable.mesh2;
            case 3:
                return R.drawable.mesh3;
            case 4:
                return R.drawable.mesh4;
            case 5:
                return R.drawable.mesh5;
            default:
                return R.drawable.user;
        }
    }

    public void showDialog(String[] numbers, int index) {
        AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.alert_numbers, null);

        final RecyclerView recyclerView = view.findViewById(R.id.number_rv);
        NumberListContactAdapter adapter = new NumberListContactAdapter(list.get(index), numbers);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        alert.setView(view);
        alert.setCancelable(false);

        AlertDialog alertDialog = alert.create();
        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();

        final Button close = view.findViewById(R.id.alert_close_contact);
        close.setOnClickListener(v-> alertDialog.cancel());
    }

    private void sort(String[] names) {
        int n = names.length;
        String temp;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (names[i].compareTo(names[j]) > 0) {
                    temp = names[i];
                    names[i] = names[j];
                    names[j] = temp;
                }
            }
        }
    }

    // If the Clicked Contact has many numbers then Show A Dialog
    private class NumberListContactAdapter extends RecyclerView.Adapter<NumberListContactAdapter.ViewHolder>{
        User user;
        final String[] numbers;
        public NumberListContactAdapter(User user, String[] numbers) {
            this.user = user;
            this.numbers = numbers;
        }

        @NonNull
        @Override
        public NumberListContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recylerview_numbers_contact, parent, false);
            return new NumberListContactAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NumberListContactAdapter.ViewHolder holder, int position) {
            int index = holder.getAdapterPosition();
            Toast.makeText(context, String.valueOf(index), Toast.LENGTH_SHORT).show();
            holder.button.setText(numbers[index]);
            holder.button.setOnClickListener(v -> {
                user.changePhone(numbers[index]);
                startActivity();
            });
        }

        @Override
        public int getItemCount() {
            return numbers.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
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
}


