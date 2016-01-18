package com.monetease.app.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.monetease.app.MoneteaseApplication;
import com.monetease.app.R;
import com.monetease.app.helper.Common;
import com.monetease.app.helper.IntentExtraHelper;
import com.monetease.app.helper.preferences.Preferences;
import com.monetease.app.listener.UserProfileListener;
import com.monetease.app.model.User;
import com.squareup.picasso.Picasso;

/**
 * Created by vikas on 27/12/15.
 */
public class MainActivity extends AppCompatActivity
                implements UserProfileListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private MoneteaseApplication application;
    private Common.LoginVia loginVia;
    private UserProfileListener userProfileListener;
    private GoogleApiClient mGoogleApiClient;
    private ConnectionResult mConnectionResult;
    private static final int RC_SIGN_IN = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        if(loginVia.equals(Common.LoginVia.GOOGLE)&&application.getGoogleApiClient()==null){
            initGoogle();
        }else {
            loadUserData();
        }
        setDrawer();
    }

    private void init() {
        this.application = (MoneteaseApplication)this.getApplicationContext();
        this.loginVia = Common.LoginVia.getType(getIntent().getStringExtra(IntentExtraHelper.STRING_LOGIN_VIA));
        this.userProfileListener = this;
        RelativeLayout btnProfile = (RelativeLayout)findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMyProfileActivity();
            }
        });
        RelativeLayout btnUpload = (RelativeLayout)findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUploadActivity();
            }
        });
        RelativeLayout btnSendMoney = (RelativeLayout)findViewById(R.id.btnSendMoney);
        btnSendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        RelativeLayout btnRequestMoney = (RelativeLayout)findViewById(R.id.btnRequestMoney);
        btnRequestMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        RelativeLayout btnLogout = (RelativeLayout)findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void openUploadActivity() {
        Intent intent = new Intent(this,UploadActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    private void logout() {
        Common.LoginVia loginVia = Common.LoginVia.getType(application.getPrivatePreferences().getStringPreferences(Preferences.LOGIN_VIA));
        if(loginVia.equals(Common.LoginVia.FACEBOOK)){
            LoginManager.getInstance().logOut();
        }else if(loginVia.equals(Common.LoginVia.GOOGLE)){
            if (application.getGoogleApiClient().isConnected()) {
                Plus.AccountApi.clearDefaultAccount(application.getGoogleApiClient());
                application.getGoogleApiClient().disconnect();
                application.getGoogleApiClient().connect();
            }
        }
        application.getPrivatePreferences().setStringPreferences(Preferences.LOGIN_VIA, Common.LoginVia.NONE.toString());
        this.finish();
    }

    private void setDrawer() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(myToolbar);
        ActionBarDrawerToggle actionBarDrawerToggle;
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, myToolbar, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {

            }

            public void onDrawerOpened(View drawerView) {

            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView btnDrawer = (ImageView)findViewById(R.id.btnDrawer);
        btnDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);
                LinearLayout drawerView = (LinearLayout) findViewById(R.id.drawerView);
                if (drawerLayout.isDrawerOpen(drawerView)) {
                    drawerLayout.closeDrawer(drawerView);
                } else {
                    drawerLayout.openDrawer(drawerView);
                }
            }
        });
    }

    private void loadUserData() {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (loginVia.equals(Common.LoginVia.FACEBOOK)) {
                    application.getFaceBookController().fetchFaceBookUserProfile(userProfileListener);
                }else if(loginVia.equals(Common.LoginVia.GOOGLE)){
                    application.getGoogleController().fetchGoogleUserProfile(application.getGoogleApiClient(), userProfileListener);
                }
                return null;
            }
        }.execute();
    }

    private void openMyProfileActivity() {
        Intent intent = new Intent(this,ProfileActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    private void loadView(User user) {
        try{
            application.setAuthenticatedUser(user);
            ImageView imgUserMain = (ImageView)findViewById(R.id.imgUserMain);
            Picasso.with(getApplicationContext())
                    .load(user.getProfile_url())
                    .centerCrop()
                    .fit()
                    .into(imgUserMain);

            ImageView imgDrawerUser = (ImageView)findViewById(R.id.imgDrawerUser);
            Picasso.with(getApplicationContext())
                    .load(application.getAuthenticatedUser().getProfile_url())
                    .centerCrop()
                    .fit()
                    .into(imgDrawerUser);

            TextView txtDrawerUserName = (TextView)findViewById(R.id.txtDrawerUserName);
            txtDrawerUserName.setText(application.getAuthenticatedUser().getName());

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void setUserProfile(final User user) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadView(user);
            }
        });
    }

    private void initGoogle() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        application.setGoogleApiClient(mGoogleApiClient);
        loadUserData();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

    }
}
