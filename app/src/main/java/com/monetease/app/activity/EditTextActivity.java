package com.monetease.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.monetease.app.R;
import com.monetease.app.helper.IntentExtraHelper;


/**
 * Created by vikas on 23/08/15.
 */
public class EditTextActivity extends Activity {

    private int EDIT_TEXT_INTENT_ID = 1000;

    @Override
    public void onCreate(Bundle instance){
        super.onCreate(instance);
        setContentView(R.layout.activity_edit_text);

        final EditText txtData = (EditText)findViewById(R.id.txtEditedText);
        String data = getIntent().getStringExtra(IntentExtraHelper.INTENT_EXTRA_STRING);
        txtData.setText(data);

        final Button btnYes = (Button)findViewById(R.id.btnYes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txtData.getText().toString().isEmpty()){
                    sendResult(txtData.getText().toString());
                }
                else {
                    displayToast("Text box can't be empty.");
                }
            }
        });

        ImageView btnNo = (ImageView)findViewById(R.id.btnNo);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });
    }

    private void sendResult(String data){
        Intent intent = new Intent();
        intent.putExtra(IntentExtraHelper.INTENT_EXTRA_STRING, data);
        setResult(EDIT_TEXT_INTENT_ID, intent);
        finish();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    private void displayToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        this.finish();
    }
}
