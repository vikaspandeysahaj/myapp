package com.monetease.app.helper.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vikas on 03/12/15.
 */
public class PrivatePreferences {

    private Context context;
    private SharedPreferences sharedPreferences;

    public PrivatePreferences(Context context){
        this.context = context;
        sharedPreferences = this.context.getSharedPreferences("com.monetease", Context.MODE_PRIVATE);
    }

    public void setBooleanPreferences(Preferences preferences , Boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(preferences.toString(), value);
        editor.commit();
    }

    public Boolean getBooleanPreferences(Preferences preferences){
        return sharedPreferences.getBoolean(preferences.toString(), true);
    }

    public void setStringPreferences(Preferences preferences , String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferences.toString(), value);
        editor.commit();
    }

    public String getStringPreferences(Preferences preferences){
        return sharedPreferences.getString(preferences.toString(), "");
    }

    public void setDatePreferences(Preferences preferences, Date value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        editor.putString(preferences.toString(), format.format(value));
        editor.commit();
    }

    public Date getDatePreferences(Preferences preferences) throws ParseException {
        String date = sharedPreferences.getString(preferences.toString(), "");
        if(date.isEmpty()){
            return new Date();
        }else {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            return format.parse(date);
        }
    }
}
