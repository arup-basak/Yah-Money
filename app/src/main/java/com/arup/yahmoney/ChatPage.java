package com.arup.yahmoney;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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

        TextView nameView = findViewById(R.id.chats_name);
        TextView numberView = findViewById(R.id.chats_number);

        int index = Integer.parseInt(getIntent().getStringExtra("IndexFromMainPage"));

        this.chat = MainActivity.chats.get(index);
        User user = chat.getUser();
        nameView.setText(user.getName());
        numberView.setText(user.getPhone());


        totalTextView = findViewById(R.id.total);

        refresh();



        EditText editTextInputView = findViewById(R.id.type_addpage);

        Button receiveView = findViewById(R.id.receive);
        Button sendView = findViewById(R.id.send);

        receiveView.setOnClickListener(v -> {
            String str = editTextInputView.getText().toString();
            if(str.length() != 0) {
                chat.subTransaction(Long.parseLong(str));
                editTextInputView.setText(null);
                chat.addMessage(str, true);
                refresh();
            }
        });

        sendView.setOnClickListener(v -> {
            String str = editTextInputView.getText().toString();
            if(str.length() != 0) {
                chat.addTransaction(Long.parseLong(str));
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