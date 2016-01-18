package com.monetease.app.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.monetease.app.MoneteaseApplication;
import com.monetease.app.R;
import com.monetease.app.custom.DatePickerFragment;
import com.monetease.app.helper.Common;
import com.monetease.app.helper.IntentExtraHelper;
import com.monetease.app.helper.preferences.Preferences;
import com.monetease.app.listener.DatePickerListener;
import com.monetease.app.listener.UserProfileListener;
import com.monetease.app.model.User;
import com.squareup.picasso.Picasso;

import java.util.Date;

/**
 * Created by vikas on 17/01/16.
 */
public class RegistrationActivityPage2 extends AppCompatActivity
        implements DatePickerListener, UserProfileListener {

    private MoneteaseApplication application;
    private UserProfileListener userProfileListener;
    private Common.LoginVia loginVia;
    private int EDIT_TEXT_INTENT_ID = 1000;
    private int PLACE_PICKER_REQUEST = 1;
    private TextView editableTextView;
    private TextView txtUserName;
    private TextView txtEmail;
    private TextView txtMobile;
    private TextView txtDOB;
    private Spinner txtGender;
    private TextView txtAddress;
    private TextView txtProvider;
    private Button btnUpdate;
    private ImageView imageUserProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page2);
        init();
        loadUserData();
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

    private void loadProfile(User user) {
        application.setAuthenticatedUser(user);

        Picasso.with(getApplicationContext())
                .load(user.getProfile_url())
                .centerCrop()
                .fit()
                .into(imageUserProfile);

        txtUserName.setText(user.getName());
        txtEmail.setText(user.getEmail());
        txtMobile.setText(user.getMobile());
        if(user.getBirthday()!=null) {
            txtDOB.setText(application.getUiHelper().formatDateTime(new Date(user.getBirthday())));
        }
        if(user.getGender()!=null) {
            if (user.getGender().toUpperCase().equals("MALE")
                    ||user.getGender().toUpperCase().equals("0")) {
                txtGender.setSelection(0);
            } else {
                txtGender.setSelection(1);
            }
        }
        txtAddress.setText(user.getAddress());
        txtProvider.setText(user.getProvider());
    }

    private void init() {
        this.application = (MoneteaseApplication)getApplicationContext();
        this.txtDOB = (TextView)findViewById(R.id.txtDOB);
        this.txtUserName = (TextView) findViewById(R.id.txtUserName);
        this.txtEmail = (TextView) findViewById(R.id.txtEmail);
        this.txtMobile = (TextView) findViewById(R.id.txtMobile);
        this.txtDOB = (TextView) findViewById(R.id.txtDOB);
        this.txtGender = (Spinner) findViewById(R.id.txtGender);
        this.txtAddress = (TextView) findViewById(R.id.txtAddress);
        this.txtProvider = (TextView) findViewById(R.id.txtProvider);
        this.btnUpdate = (Button)findViewById(R.id.btnUpdate);
        this.loginVia = Common.LoginVia.getType(getIntent().getStringExtra(IntentExtraHelper.STRING_LOGIN_VIA));
        this.userProfileListener = this;
        this.imageUserProfile = (ImageView) findViewById(R.id.imageUserProfile);

        RelativeLayout btnMobile = (RelativeLayout)findViewById(R.id.btnMobile);
        btnMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editableTextView = (TextView) findViewById(R.id.txtMobile);
                openEditTextIntent(String.valueOf(editableTextView.getText()));
            }
        });

        RelativeLayout btnDOB = (RelativeLayout)findViewById(R.id.btnDOB);
        btnDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        RelativeLayout btnProvider = (RelativeLayout)findViewById(R.id.btnProvider);
        btnProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editableTextView = (TextView) findViewById(R.id.txtProvider);
                openEditTextIntent(String.valueOf(editableTextView.getText()));
            }
        });

        RelativeLayout btnAddress = (RelativeLayout)findViewById(R.id.btnAddress);
        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlacePicker();
            }
        });

        RelativeLayout btnUserName = (RelativeLayout)findViewById(R.id.btnUserName);
        btnUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editableTextView = (TextView) findViewById(R.id.txtUserName);
                openEditTextIntent(String.valueOf(editableTextView.getText()));
            }
        });

        RelativeLayout btnEmail = (RelativeLayout)findViewById(R.id.btnEmail);
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editableTextView = (TextView) findViewById(R.id.txtEmail);
                openEditTextIntent(String.valueOf(editableTextView.getText()));
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtGender.setAdapter(adapter);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    updateProfile();
                }
            }
        });
    }

    private void updateProfile() {
        //Update logic here
        openMainActivity();
    }

    private void openMainActivity() {
        application.getPrivatePreferences().setStringPreferences(Preferences.LOGIN_VIA, loginVia.toString());
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(IntentExtraHelper.STRING_LOGIN_VIA,loginVia.toString());
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private void openEditTextIntent(String data){
        Intent intent = new Intent(this, EditTextActivity.class);
        intent.putExtra(IntentExtraHelper.INTENT_EXTRA_STRING, data);
        startActivityForResult(intent, EDIT_TEXT_INTENT_ID);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    private void openDatePicker(){
        DialogFragment newFragment = new DatePickerFragment(this);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == EDIT_TEXT_INTENT_ID) {
            String newTitle = intent.getStringExtra(IntentExtraHelper.INTENT_EXTRA_STRING);
            if (newTitle != null) {
                editableTextView.setText(newTitle);
            }
        }else if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(intent, this);
                txtAddress.setText(place.getAddress());
            }
        }
    }

    @Override
    public void onDateSelected(Date date) {
        String dateString = application.getUiHelper().formatDateTime(date);
        txtDOB.setText(dateString);
    }

    public void openPlacePicker() {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean validateFields() {
        if (txtEmail.getText().toString().trim().isEmpty()
                || txtUserName.getText().toString().trim().isEmpty()
                || txtAddress.getText().toString().trim().isEmpty()
                || txtDOB.getText().toString().trim().isEmpty()
                || txtMobile.getText().toString().trim().isEmpty()
                || txtProvider.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void setUserProfile(final User user) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadProfile(user);
            }
        });
    }
}
