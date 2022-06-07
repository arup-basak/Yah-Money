package com.arup.yahmoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.arup.yahmoney.Library.User;
import com.google.gson.Gson;

public class login2 extends AppCompatActivity {

    String name, phone, uid;
    Button submit;
    EditText nameEditText;

    Gson GSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        GSON = new Gson();
        this.overridePendingTransition(R.anim.anim_right_to_left, R.anim.anim_left_to_right);

        Intent get = getIntent();
        phone = get.getStringExtra("phone");
        uid = get.getStringExtra("uid");

        submit = findViewById(R.id.submit_name);
        nameEditText = findViewById(R.id.name_login);

        submit.setOnClickListener(v -> {
            name = nameEditText.getText().toString();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("nameFromLogin", name);
            intent.putExtra("phoneFromLogin", phone);
            SaveUserData();
            startActivity(intent);
            finish();
        });

    }

    private void SaveUserData() {
        String userString = GSON.toJson(new User(name, phone));
        SharedPreferences.Editor editor = getSharedPreferences(loginActivity.MY_PREF, MODE_PRIVATE).edit();
        editor.putString("user", userString);
        editor.putString("uid", uid);
        editor.apply();
    }
}