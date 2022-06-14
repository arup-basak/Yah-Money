package com.arup.yahmoney;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arup.yahmoney.Adapters.MainListAdapter;
import com.arup.yahmoney.Library.ChatSystem.Chat;
import com.arup.yahmoney.Library.ChatSystem.Chats;
import com.arup.yahmoney.Library.User;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    public static Chats chats;

    static User user;

    public static User tempUser;

    FirebaseDatabase database;
    DatabaseReference myRef;

    Gson GSON;
    private final String KEY = "ChatData";

    FloatingActionButton addCustomFab, addContactFab;
    ExtendedFloatingActionButton mAddFab;
    TextView addCustomActionText, addContactActionText;
    Boolean isAllFabVisible;

    LinearLayout console;


    @Override
    public void onResume() {
        super.onResume();
        if(tempUser != null) {
            chats.add(new Chat(user, tempUser, ""));
            tempUser = null;
        }
        refresh();
        SaveDetails();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        GSON = new Gson();

        Intent get_intent = getIntent();
        String json = get_intent.getStringExtra("userJSONFromLogin");

        user = GSON.fromJson(json, User.class);

        Log.d("gheg", json);

        database = FirebaseDatabase.getInstance("https://yah--money-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef = database.getReference().child(user.getUid());

        SaveOnline();

        getDetails();

        console = findViewById(R.id.console_layout);

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
                    OpenContacts();
                });

        addCustomFab.setOnClickListener(
                view -> showDialog());

        console.setOnClickListener(v -> {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    console,
                    ViewCompat.getTransitionName(console)
            );
            startActivity(new Intent(this, searchActivity.class), options.toBundle());
        });


        context = this;

//        consoleEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                //String command = consoleEditText.getText().toString();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });



        refresh();
    }

    private void getDetails() {
        try {
            SharedPreferences sp = getSharedPreferences(KEY, MODE_PRIVATE);
            String json = sp.getString(user.getUid(), "");
            chats = GSON.fromJson(json, Chats.class);
            refresh();

        }
        catch (Exception e) {
            chats = new Chats(user);
            Log.d("Error", e.getMessage());

        }
    }

    private void SaveDetails() {
        SharedPreferences preferences = getSharedPreferences(KEY, MODE_PRIVATE);
        SharedPreferences.Editor myEdit = preferences.edit();

        String json = GSON.toJson(chats);
        myEdit.putString(user.getUid(), json);
        myEdit.apply();
    }

    public void showDialog() {
        AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_custom_add_page, null);

        final EditText nameEV = view.findViewById(R.id.alert_name);
        final EditText phoneEV = view.findViewById(R.id.alert_phone_no);

        final Button save = view.findViewById(R.id.alert_save);

        alert.setView(view);
        alert.setCancelable(false);

        AlertDialog alertDialog = alert.create();
        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();

        save.setOnClickListener(v -> {
            String name = nameEV.getText().toString();
            String phone = phoneEV.getText().toString();

            User user2 = new User(name, phone, null);

            chats.add(new Chat(user, user2, null));

            refresh();
            SaveDetails();
            alertDialog.cancel();

            Intent intent = new Intent(this, ChatPage.class);
            intent.putExtra("IndexFromMainPage", String.valueOf(chats.size() - 1));
            startActivity(intent);
        });
        view.findViewById(R.id.alert_close).setOnClickListener(v -> alertDialog.cancel());
    }

    private void refresh() {
        int size = chats.size();
        String[][] data = new String[size][2];
        for(int i = 0 ; i < size ; i++) {
            data[i][0] = chats.get(i).getUser().getName();
            data[i][1] = String.valueOf(chats.get(i).getAmount());
        }

        MainListAdapter adapter = new MainListAdapter(data, this);
        RecyclerView rv = findViewById(R.id.mainpage_recycler);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void OpenContacts() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 100);
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                OpenContacts();
            }
        }
        else {
            Intent intent = new Intent(getApplicationContext(), contacts.class);
            startActivity(intent);
            finish();
        }
    }

    private void SaveOnline() {
        myRef.setValue("This");
    }
}