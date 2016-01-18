package com.monetease.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.monetease.app.MoneteaseApplication;
import com.monetease.app.R;
import com.monetease.app.adapter.SmsAdapter;
import com.monetease.app.controller.SmsController;
import com.monetease.app.model.SmsPayload;

import java.util.List;

/**
 * Created by vikas on 10/01/16.
 */
public class UploadSMSFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_upload_sms, container, false);
        MoneteaseApplication application = (MoneteaseApplication)inflater.getContext().getApplicationContext();
        SmsController smsController = application.getSmsController();
        final SmsAdapter smsAdapter = new SmsAdapter(inflater.getContext().getApplicationContext());

        List<SmsPayload> smses = smsController.getSms();
        ListView smsListView = (ListView)rootView.findViewById(R.id.smsListView);
        smsListView.setAdapter(smsAdapter);
        smsAdapter.addSmses(smses);
        return rootView;
    }
}
