package com.arup.yahmoney;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.arup.yahmoney.ChatSystem.Chat;

public class ChatPage extends AppCompatActivity {

    private Chat chat;
    private TextView totalTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        Intent intent = getIntent();
        String posStr = String.valueOf(intent.getStringExtra("PositionIndex"));
        TextView nameView = findViewById(R.id.chats_name);
        TextView numberView = findViewById(R.id.chats_number);
        int index = 0;

        if (MainActivity.chats.size() == 0) {
            String name = intent.getStringExtra("NameFromNew");
            String phone = intent.getStringExtra("PhoneFromNew");
            User user = new User(name, phone);
            chat = new Chat(MainActivity.user, user, "");
            nameView.setText(name);
            numberView.setText(phone);
        }
        else {
            this.chat = MainActivity.chats.get(index);
        }
        totalTextView = findViewById(R.id.total);

        refresh();



        EditText editTextInputView = findViewById(R.id.type_addpage);

        Button receiveView = findViewById(R.id.receive);
        Button sendView = findViewById(R.id.send);

        receiveView.setOnClickListener(v -> {
            String str = editTextInputView.getText().toString();
            chat.subTransaction(Long.parseLong(str));
            if(str.length() != 0) {
                editTextInputView.setText(null);
                chat.addMessage(str, true);
                refresh();
            }
        });

        sendView.setOnClickListener(v -> {
            String str = editTextInputView.getText().toString();
            chat.addTransaction(Long.parseLong(str));
            if(str.length() != 0) {
                editTextInputView.setText(null);
                chat.addMessage(str, false);
                refresh();
            }
        });


    }
    private void refresh() {
        RecyclerView recyclerView = findViewById(R.id.chats);
        DateChatsAdapter adapter = new DateChatsAdapter(this, chat.getMessageWithDates());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            Log.d("Frebfgregj", chat.get(0).toString());
            Log.d("Frebfgregj", chat.get(1).toString());
            Log.d("Frebfgregj", chat.get(2).toString());
            Log.d("Frebfgregj", chat.get(3).toString());
        }
        catch (Exception e) {

        }


        totalTextView.setText(String.valueOf(chat.getAmount()));
        if(chat.getAmount() < 0) {
            totalTextView.setTextColor(totalTextView.getContext().getColor(R.color.green));
        }
        else if(chat.getAmount() > 0) {
            totalTextView.setTextColor(totalTextView.getContext().getColor(R.color.red));
        }
        else {
            totalTextView.setTextColor(totalTextView.getContext().getColor(R.color.color5));
        }
    }
}