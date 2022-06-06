package com.arup.yahmoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class login2 extends AppCompatActivity {

    String name, phone;
    Button submit;
    EditText nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        this.overridePendingTransition(R.anim.anim_right_to_left, R.anim.anim_left_to_right);

        Intent get = getIntent();
        phone = get.getStringExtra("phone");

        submit = findViewById(R.id.submit_name);
        nameEditText = findViewById(R.id.name_login);

        submit.setOnClickListener(v -> {
            name = nameEditText.getText().toString();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("nameFromLogin", name);
            intent.putExtra("phoneFromLogin", phone);
            startActivity(intent);
        });

    }
}