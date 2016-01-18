package com.monetease.app;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.google.android.gms.common.api.GoogleApiClient;
import com.monetease.app.controller.FaceBookController;
import com.monetease.app.controller.GoogleController;
import com.monetease.app.controller.SmsController;
import com.monetease.app.helper.UiHelper;
import com.monetease.app.helper.preferences.PrivatePreferences;
import com.monetease.app.model.User;

/**
 * Created by vikas on 01/12/15.
 */
public class MoneteaseApplication extends Application {

    private SmsController smsController;
    private PrivatePreferences privatePreferences;
    private FaceBookController faceBookController;
    private User authenticatedUser;
    private GoogleApiClient googleApiClient;
    private GoogleController googleController;
    private UiHelper uiHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        initComponents();
    }

    private void initComponents() {
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    public SmsController getSmsController(){
        if(smsController==null){
            smsController = new SmsController(getApplicationContext());
        }
        return smsController;
    }

    public PrivatePreferences getPrivatePreferences(){
        if(privatePreferences==null){
            privatePreferences = new PrivatePreferences(getApplicationContext());
        }
        return privatePreferences;
    }

    public FaceBookController getFaceBookController(){
        if(faceBookController == null){
            faceBookController = new FaceBookController(getApplicationContext());
        }
        return faceBookController;
    }

    public User getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void setAuthenticatedUser(User authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    public GoogleController getGoogleController(){
        if(googleController==null){
            googleController = new GoogleController();
        }
        return googleController;
    }

    public UiHelper getUiHelper(){
        if(uiHelper==null){
            uiHelper = new UiHelper(getApplicationContext());
        }
        return uiHelper;
    }
}
