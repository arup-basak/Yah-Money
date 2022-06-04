package com.arup.yahmoney;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.arup.yahmoney.Library.ChatSystem.Chat;
import com.arup.yahmoney.Library.User;
import com.google.android.material.appbar.MaterialToolbar;

public class ChatPage extends AppCompatActivity {

    private Chat chat;
    private TextView totalTextView;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        MaterialToolbar toolbar = findViewById(R.id.tempToolBar);


        int index = Integer.parseInt(getIntent().getStringExtra("IndexFromMainPage"));

        this.chat = MainActivity.chats.get(index);
        user = chat.getUser();
        String name = user.getName();
        toolbar.setTitle(name);
        toolbar.findViewById(R.id.call).setOnClickListener(v -> call(user.getPhone()));
        toolbar.setNavigationOnClickListener(v-> finish());


        totalTextView = findViewById(R.id.total);

        refresh();

        EditText editTextInputView = findViewById(R.id.type_add_page);

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

    private void call(String number) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ChatPage.this, new String[]{Manifest.permission.CALL_PHONE}, 100);
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                call(number);
            }
        }
        else {
            number = number.replace(" ", "");
            number = number.replace("-", "");
            number = number.replace("+91", "");
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + "+91" + number));
            startActivity(callIntent);
        }
    }
}