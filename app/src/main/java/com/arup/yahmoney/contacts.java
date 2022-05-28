package com.arup.yahmoney;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.LinkedList;

public class contacts extends AppCompatActivity {
    private Cursor cursor;

    private void loadRecyclerView(LinkedList<User> list) {
        RecyclerView view = findViewById(R.id.contacts_rv);
        ContactAdapter adapter = new ContactAdapter(list, cursor, this);
        view.setAdapter(adapter);
        view.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        loadRecyclerView(get());
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();

    }

    private LinkedList<User> get() {
        LinkedList<User> list = new LinkedList<>();
        final String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        };
        String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        ContentResolver cr = getContentResolver();
        cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, projection, selection, null, null);
        if (cursor.getCount() > 0)
        {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                list.add(new User(name, id));
            }
        }
        sort(list);
        return list;
    }

    private void sort(LinkedList<User> users) {
        int n = users.size();
        User user = new User("", "");
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (users.get(i).getName().compareTo(users.get(j).getName()) > 0) {
                    user.set(users.get(i));
                    users.get(i).set(users.get(j));
                    users.get(j).set(user);
                }
            }
        }
    }
}