package com.arup.yahmoney;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

interface SmsListener {
    void messageReceived(String messageText);
}

class SmsReceiver extends BroadcastReceiver {
    private Bundle lastdata = null;
    private static SmsListener mListener;
    Boolean b;
    String abcd, xyz;
    /*
     * EcoAndroid: CACHE ENERGY PATTERN APPLIED
     * Whenever a request is received, checks if anything changes before using the data
     * Application changed java file "SmsReceiver.java"
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (lastdata.equals(intent.getExtras())) {
            // EcoAndroid: nothing has changed; we can safely return
            return;
        }
        updateValues(intent);
        Object[] objArr = (Object[]) lastdata.get("pdus");
        for (Object o : objArr) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) o);
            String sender = smsMessage.getDisplayOriginatingAddress();

            String messageBody = smsMessage.getMessageBody();
            abcd = messageBody.replaceAll("[^0-9]", "");
            //Pass on the text to our listener.
            if (b) {
                mListener.messageReceived(abcd); // attach value to interface
            }
        }
    }
    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }

    private void updateValues(Intent intent) {
        lastdata = intent.getExtras();
    }
}