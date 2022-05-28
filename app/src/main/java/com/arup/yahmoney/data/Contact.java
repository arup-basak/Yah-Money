package com.arup.yahmoney.data;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Arrays;

public class Contact extends AppCompatActivity {
    private final Context context;

    private String[][] ContactData;

    private static final int REQUEST_READ_CONTACTS = 79;
    public Contact(Context context) {
        this.context = context;

        int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS);
        if(permission != PackageManager.PERMISSION_GRANTED) {
            RequestPermission();
        }

    }


    public String[][] getContact() {
        return ContactData;
    }

    private void RequestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.READ_CONTACTS)) {
            Toast.makeText(context, "Accessed", Toast.LENGTH_SHORT).show();//Problem
            
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
            readContact();
        }
    }

    @SuppressLint("Range")
    private void readContact() {
        ContentResolver contentResolver = context.getContentResolver();
        //@SuppressLint("Recycle")
        Cursor cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        int CursorLength = cursor.getCount();
        if(CursorLength > 0) {
            ContactData = new String[CursorLength][3]; //id, Name, ContactNo
            Log.d("CursorLength", String.valueOf(CursorLength));
            while(cursor.moveToNext()) {
                int pos = cursor.getPosition();
                ContactData[pos][0] = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                ContactData[pos][1] = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if(cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    //@SuppressLint("Recycle")
                    Cursor pCur = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{ContactData[pos][0]}, null
                    );
                    while (pCur.moveToNext()) {
                        ContactData[pos][2] = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    pCur.close();
                }
                Log.d("OneLineData", Arrays.toString(ContactData[pos]));
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /*if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "Permission Denied2", Toast.LENGTH_SHORT).show();
            }
        }*/
    }
}
