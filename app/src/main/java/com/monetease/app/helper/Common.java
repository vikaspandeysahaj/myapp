package com.monetease.app.helper;

/**
 * Created by vikas on 07/01/16.
 */
public class Common {

    public static enum LoginVia{
        FACEBOOK,
        GOOGLE,
        LINKEDIN,
        NONE;

        public String toString(){
            switch(this){
                case FACEBOOK :
                    return "FACEBOOK";
                case GOOGLE :
                    return "GOOGLE";
                case LINKEDIN :
                    return "LINKEDIN";
                case NONE :
                    return "NONE";
            }
            return null;
        }

        public static LoginVia getType(String value){
            if(value.equalsIgnoreCase(FACEBOOK.toString()))
                return LoginVia.FACEBOOK;
            else if(value.equalsIgnoreCase(GOOGLE.toString()))
                return LoginVia.GOOGLE;
            else if(value.equalsIgnoreCase(LINKEDIN.toString()))
                return LoginVia.LINKEDIN;
            else if(value.equalsIgnoreCase(NONE.toString()))
                return LoginVia.NONE;
            else
                return null;
        }
    }
}
