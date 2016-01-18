package com.monetease.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.monetease.app.MoneteaseApplication;
import com.monetease.app.R;
import com.monetease.app.helper.Common;
import com.monetease.app.helper.IntentExtraHelper;
import com.monetease.app.helper.preferences.Preferences;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SplashActivity extends Activity {

    private MoneteaseApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
        printKeyHash();
    }

    private void init() {
        this.application = (MoneteaseApplication)getApplicationContext();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AuthenticateUser();
    }

    private void AuthenticateUser() {
        Common.LoginVia loginVia = Common.LoginVia.getType(application.getPrivatePreferences().getStringPreferences(Preferences.LOGIN_VIA));
        if(loginVia!=null && !loginVia.equals(Common.LoginVia.NONE)){
            openMainActivity(loginVia);
        }else {
            openRegistrationActivity();
        }
    }

    public void openMainActivity(Common.LoginVia loginVia){
        try {
            Intent mainActivity = new Intent(this, MainActivity.class);
            mainActivity.putExtra(IntentExtraHelper.STRING_LOGIN_VIA,loginVia.toString());
            startActivity(mainActivity);
            this.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openRegistrationActivity(){
        try {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
            this.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printKeyHash() {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
    }

}
