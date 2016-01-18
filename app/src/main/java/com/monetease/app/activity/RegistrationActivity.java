package com.monetease.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.monetease.app.MoneteaseApplication;
import com.monetease.app.R;
import com.monetease.app.helper.Common;
import com.monetease.app.helper.IntentExtraHelper;
import com.monetease.app.helper.preferences.Preferences;

import java.util.Arrays;

/**
 * Created by vikas on 27/12/15.
 */
public class RegistrationActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private MoneteaseApplication application;
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    private static final int RC_SIGN_IN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
        initFaceBook();
        initGoogle();
    }

    private void initGoogle() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        SignInButton signInButton = (SignInButton)findViewById(R.id.btnRegisterGamil);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mGoogleApiClient.isConnecting()) {
                    mSignInClicked = true;
                    resolveSignInError();
                }
            }
        });
    }

    private void init() {
        this.application = (MoneteaseApplication)getApplicationContext();
    }

    private void initFaceBook() {

        LoginButton loginButton = (LoginButton)findViewById(R.id.btnRegisterFacebook);
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                application.getPrivatePreferences().setStringPreferences
                        (Preferences.FACEBOOK_AUTH_TOKEN, loginResult.getAccessToken().getToken());
                application.getPrivatePreferences().setStringPreferences
                        (Preferences.FACEBOOK_USER_ID, loginResult.getAccessToken().getUserId());
                openRegistrationActivityPage2(Common.LoginVia.FACEBOOK);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {
                throw e;
            }
        });
    }

    private void openRegistrationActivityPage2(Common.LoginVia loginVia) {
        application.getPrivatePreferences().setStringPreferences(Preferences.LOGIN_VIA, loginVia.toString());
        Intent intent = new Intent(this, RegistrationActivityPage2.class);
        intent.putExtra(IntentExtraHelper.STRING_LOGIN_VIA,loginVia.toString());
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
        application.setGoogleApiClient(mGoogleApiClient);
        openRegistrationActivityPage2(Common.LoginVia.GOOGLE);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

}
