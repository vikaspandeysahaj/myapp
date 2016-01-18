package com.monetease.app.helper.preferences;

/**
 * Created by vikas on 03/12/15.
 */
public enum Preferences {
    LOGIN_VIA,
    FACEBOOK_AUTH_TOKEN,
    FACEBOOK_USER_ID;

    public String toString(){
        switch(this){
            case LOGIN_VIA :
                return "LOGIN_VIA";
            case FACEBOOK_USER_ID :
                return "FACEBOOK_USER_ID";
            case FACEBOOK_AUTH_TOKEN :
                return "FACEBOOK_AUTH_TOKEN";
        }
        return null;
    }

    public static Preferences getType(String value){
        if(value.equalsIgnoreCase(FACEBOOK_AUTH_TOKEN.toString()))
            return Preferences.FACEBOOK_AUTH_TOKEN;
        else if(value.equalsIgnoreCase(FACEBOOK_USER_ID.toString()))
            return Preferences.FACEBOOK_USER_ID;
        else if(value.equalsIgnoreCase(LOGIN_VIA.toString()))
            return Preferences.LOGIN_VIA;
        else
            return null;
    }
}
