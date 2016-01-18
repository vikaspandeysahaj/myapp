package com.monetease.app.activity;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.monetease.app.R;
import com.monetease.app.adapter.EmailAdapter;
import com.monetease.app.model.EmailPayload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vikas on 10/01/16.
 */
public class GmailActivity extends Activity {
    GoogleAccountCredential mCredential;
    EmailAdapter emailAdapter;
    ListView emailListView;
    TextView toolbar_title;
    String nextPageToken = "";
    public final static Integer LOAD_THRESHOLD = 3;
    private boolean mLoading = false;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final String PREF_ACCOUNT_NAME = "accountName";

    /**
     * Create the main activity.
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_gmail);
        waitProgressVisibility(true);
        emailAdapter = new EmailAdapter(this);
        emailListView = (ListView)findViewById(R.id.emailListView);
        emailListView.setAdapter(emailAdapter);
        emailListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(totalItemCount==0) return;
                int lastVisibleItems =0;
                lastVisibleItems = emailListView.getLastVisiblePosition();
                if (!mLoading && lastVisibleItems > totalItemCount - LOAD_THRESHOLD) {
                    new MakeRequestTask(mCredential).execute();
                }
            }
        });

        toolbar_title = (TextView)findViewById(R.id.toolbar_title);

        ImageView btnBack = (ImageView)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Initialize credentials and service object.
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), GmailScopes.all())
                .setBackOff(new ExponentialBackOff())
                .setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isGooglePlayServicesAvailable()) {
            refreshResults();
        } else {
            Toast.makeText(getApplicationContext(),"Google Play Services required: " +
                    "after installing, close and relaunch this app.",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    isGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        mCredential.setSelectedAccountName(accountName);
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getApplicationContext(),"Account unspecified.",Toast.LENGTH_LONG).show();
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode != RESULT_OK) {
                    chooseAccount();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void refreshResults() {
        if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else {
            toolbar_title.setText(mCredential.getSelectedAccountName());
            if (isDeviceOnline()) {
                new MakeRequestTask(mCredential).execute();
            } else {
                Toast.makeText(getApplicationContext(),"No network connection available.",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void chooseAccount() {
        startActivityForResult(
                mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        final int connectionStatusCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        } else if (connectionStatusCode != ConnectionResult.SUCCESS ) {
            return false;
        }
        return true;
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                connectionStatusCode,
                GmailActivity.this,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private class MakeRequestTask extends AsyncTask<Void, List<EmailPayload>, List<EmailPayload>> {
        private com.google.api.services.gmail.Gmail mService = null;
        private Exception mLastError = null;

        public MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.gmail.Gmail.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Gmail API Android Quickstart")
                    .build();
        }

        @Override
        protected List<EmailPayload> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        private List<EmailPayload> getDataFromApi() throws IOException {
            // Get the labels in the user's account.
            List<EmailPayload> emailPayloads = new ArrayList<EmailPayload>();
            String user = "me";
            ListMessagesResponse listResponse =
                    mService.users().messages()
                            .list(user)
                            .setMaxResults(10L)
                            .setPageToken(nextPageToken)
                            .execute();
            if (listResponse != null || listResponse.size() > 0) {

                nextPageToken = listResponse.getNextPageToken();

                for (Message message : listResponse.getMessages()) {

                    Message gmailMessage = mService
                            .users()
                            .messages()
                            .get("me", message.getId())
                            .setFormat("metadata").execute();

                    EmailPayload emailPayload = new EmailPayload();
                    emailPayload.setId(message.getId());
                    emailPayload.setSnippet(gmailMessage.getSnippet());

                    for (int i = 0; i < gmailMessage.getPayload().getHeaders().size(); ++i) {
                        String name = gmailMessage.getPayload().getHeaders().get(i).getName();
                        String value = gmailMessage.getPayload().getHeaders().get(i).getValue();
                        if (name.equals("From")) {

                            String[] strings = value.split("<");
                            if (strings.length > 1) {
                                emailPayload.setSenderName(strings[0]);
                                emailPayload.setSenderId(strings[1].replace(">", ""));
                            } else {
                                emailPayload.setSenderName(value);
                                emailPayload.setSenderId(value);
                            }

                        } else if (name.equals("Subject")) {
                            emailPayload.setSubject(value);
                        } else if (name.equals("Date")) {
                            emailPayload.setReceivedTime(value);
                        }
                        if (emailPayload.getSenderName() != null
                                && emailPayload.getSenderId() != null
                                && emailPayload.getSubject() != null
                                && emailPayload.getReceivedTime() != null) {
                            break;
                        }
                    }
                    emailPayloads.add(emailPayload);
                }
            }
            return emailPayloads;
        }


        @Override
        protected void onPreExecute() {
            mLoading = true;
        }

        @Override
        protected void onPostExecute(List<EmailPayload> output) {
            mLoading = false;
            emailAdapter.setEmails(output);
            waitProgressVisibility(false);
        }

        @Override
        protected void onCancelled() {
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            GmailActivity.REQUEST_AUTHORIZATION);
                } else {
                    Toast.makeText(getApplicationContext(),mLastError.getMessage(),Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(),"Request cancelled try again",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        this.finish();
    }

    private void waitProgressVisibility(final boolean visibility) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final RelativeLayout layoutWaitError = (RelativeLayout) findViewById(R.id.llWaitProgress);
                if (visibility)
                    layoutWaitError.setVisibility(View.VISIBLE);
                else
                    layoutWaitError.setVisibility(View.GONE);
            }
        });
    }
}