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

    private String KEY = "ChatData";

    boolean bool = false;

    FloatingActionButton addCustomFab, addContactFab;
    ExtendedFloatingActionButton mAddFab;
    TextView addCustomActionText, addContactActionText;
    Boolean isAllFabVisible;

    EditText consoleEditText;


    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        if(bool) {
            refresh();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent get_intent = getIntent();
        String name = get_intent.getStringExtra("nameFromLogin");
        String phoneNo = get_intent.getStringExtra("phoneFromLogin");

        user = new User(name, phoneNo);


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



        refresh();
        bool = true;

        try {
            Toast.makeText(context, String.valueOf(chats.size()), Toast.LENGTH_SHORT).show();

        }
        catch (Exception e) {}
    }

    private void SearchName(String command) {

    }

    private void getDetails() {

    }

    private void SaveDetails(User user) {
        ObjectPreference objectPreference = (ObjectPreference) this.getApplication();

        chats.add(new Chat(MainActivity.user, user, null));

        ComplexPreferences complexPreferences = objectPreference.getComplexPreference();
        if(complexPreferences != null) {
            complexPreferences.putObject(KEY, chats);
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



        final Button save = view.findViewById(R.id.alert_save);


        alert.setView(view);
        alert.setCancelable(false);
        
        AlertDialog alertDialog = alert.create();
        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();


        save.setOnClickListener(v -> {
            String name = nameEV.getText().toString();
            String phone = phoneEV.getText().toString();

            User user2 = new User(name, phone);

            chats.add(new Chat(user, user2, null));

            refresh();
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
}