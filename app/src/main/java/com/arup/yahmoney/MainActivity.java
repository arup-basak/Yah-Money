package com.arup.yahmoney;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arup.yahmoney.ChatSystem.Chat;
import com.arup.yahmoney.ChatSystem.Chats;
import com.arup.yahmoney.data.Console;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    public static Context context;
    public static Chats chats;

    static User user;

    FloatingActionButton addCustomFab, addContactFab;
    ExtendedFloatingActionButton mAddFab;
    TextView addCustomActionText, addContactActionText;
    Boolean isAllFabVisible;

    EditText consoleEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        {
            Intent intent = getIntent();
            String name = intent.getStringExtra("nameFromLogin");
            String phoneNo = intent.getStringExtra("phoneFromLogin");

            user = new User(name, phoneNo);
        }


        consoleEditText = findViewById(R.id.console);

        mAddFab = findViewById(R.id.add_fab);
        addCustomFab = findViewById(R.id.add_alarm_fab);
        addContactFab = findViewById(R.id.add_person_fab);
        addCustomActionText = findViewById(R.id.add_alarm_action_text);
        addContactActionText = findViewById(R.id.add_person_action_text);

        addCustomFab.setVisibility(View.GONE);
        addContactFab.setVisibility(View.GONE);
        addCustomActionText.setVisibility(View.GONE);
        addContactActionText.setVisibility(View.GONE);

        isAllFabVisible = false;
        mAddFab.shrink();

        mAddFab.setOnClickListener(
                view -> {
                    if (!isAllFabVisible) {
                        addCustomFab.show();
                        addContactFab.show();
                        addCustomActionText.setVisibility(View.VISIBLE);
                        addContactActionText.setVisibility(View.VISIBLE);
                        mAddFab.extend();
                        isAllFabVisible = true;
                    }
                    else {
                        addCustomFab.hide();
                        addContactFab.hide();
                        addCustomActionText.setVisibility(View.GONE);
                        addContactActionText.setVisibility(View.GONE);
                        mAddFab.shrink();
                        isAllFabVisible = false;
                    }
                });

        addContactFab.setOnClickListener(
                view -> {
                    Intent intent = new Intent(getApplicationContext(), contacts.class);
                    startActivity(intent);
                });

        addCustomFab.setOnClickListener(
                view -> showDialog());


        context = this;
        chats = new Chats(user);

        consoleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String command = consoleEditText.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        String[][] data = {{"Arup", "Basak"}, {"Arup", "Basak"}, {"Arup", "Basak"}};
        MainListAdapter adapter = new MainListAdapter(data, this);
        RecyclerView rv = findViewById(R.id.mainpage_recycler);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void SearchName(String command) {

    }

    private void SaveDetails() {
        ObjectPreference objectPreference = (ObjectPreference) this.getApplication();

        chats.add(new Chat(user, null, null));

        ComplexPreferences complexPreferences = objectPreference.getComplexPreference();
        if(complexPreferences != null) {
//            complexPreferences.putObject("chat", chats);
            complexPreferences.commit();
        }
        else {
            String TAG = "MainActivity";
            android.util.Log.e(TAG, "Preference is null");
        }
    }

    public void showDialog() {
        AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_custom_add_page, null);

        final EditText nameEV = view.findViewById(R.id.alert_name);
        final EditText phoneEV = view.findViewById(R.id.alert_phone_no);

        String name = nameEV.getText().toString();
        String phone = phoneEV.getText().toString();

        User user2 = new User(name, phone);

        final Button save = view.findViewById(R.id.alert_save);


        alert.setView(view);
        alert.setCancelable(false);
        
        AlertDialog alertDialog = alert.create();
        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();


        save.setOnClickListener(v -> {
            chats.add(new Chat(user, user2, null));
            Intent intent = new Intent(this, ChatPage.class);
            intent.putExtra("NameFromNew", name);
            intent.putExtra("phoneFromLogin", phone);
            intent.putExtra("PositionIndex", 0);
            startActivity(intent);
        });
        view.findViewById(R.id.alert_close).setOnClickListener(v -> alertDialog.cancel());
    }
}