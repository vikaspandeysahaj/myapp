package com.monetease.app.controller;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.monetease.app.model.SmsPayload;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vikas on 10/01/16.
 */
public class SmsController {

    private Context context;
    public static final String INBOX = "content://sms/inbox";
    public static final String SENT = "content://sms/sent";
    public static final String DRAFT = "content://sms/draft";

    public SmsController(Context context){
        this.context = context;
    }

    public List<SmsPayload> getSms(){
        List<SmsPayload> messages = new ArrayList<SmsPayload>();
        Cursor cursor = context.getContentResolver().query(Uri.parse(INBOX), null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                SmsPayload smsPayload = new SmsPayload();
                smsPayload.setSenderName(cursor.getString(2));
                smsPayload.setMessage(cursor.getString(12));
                smsPayload.setSenderNumber(cursor.getString(13));
                messages.add(smsPayload);
            } while (cursor.moveToNext());
        } else {

        }
        return messages;
    }
}
