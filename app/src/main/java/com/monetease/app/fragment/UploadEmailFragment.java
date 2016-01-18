package com.monetease.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.monetease.app.R;
import com.monetease.app.activity.GmailActivity;

/**
 * Created by vikas on 10/01/16.
 */
public class UploadEmailFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_upload_email, container, false);
        RelativeLayout btnGmail = (RelativeLayout)rootView.findViewById(R.id.btnGmail);
        btnGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGmailActivity();
            }
        });
        return rootView;

    }

    private void openGmailActivity() {
        if(getActivity()==null) return;
        Intent intent = new Intent(getActivity(), GmailActivity.class);
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }
}
