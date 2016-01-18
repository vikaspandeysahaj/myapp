package com.monetease.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.monetease.app.R;
import com.monetease.app.model.SmsPayload;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vikas on 22/07/15.
 */
public class SmsAdapter extends BaseAdapter {

    private static LayoutInflater inflater=null;
    List<SmsPayload> smses = new ArrayList<SmsPayload>();

    public SmsAdapter(Context context){
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return smses.size();
    }

    @Override
    public SmsPayload getItem(int position) {
        return smses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final SmsPayload smsPayload = smses.get(position);
        if(view == null){
            view = inflater.inflate(R.layout.view_sms_list_item, null);
        }

        TextView txtSenderName = (TextView)view.findViewById(R.id.txtSenderName);
        TextView txtMessage = (TextView)view.findViewById(R.id.txtMessage);
        ImageView itemIndicator = (ImageView)view.findViewById(R.id.itemSelectionIndicator);

        if(smsPayload.isSelected()){
            itemIndicator.setImageResource(R.drawable.ic_select);
        }else {
            itemIndicator.setImageResource(R.drawable.ic_unselect);
        }
        txtSenderName.setText(smsPayload.getSenderName());
        txtMessage.setText(smsPayload.getMessage());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageView itemIndicator = (ImageView) v.findViewById(R.id.itemSelectionIndicator);
                if (smsPayload.isSelected()) {
                    smsPayload.setIsSelected(false);
                    itemIndicator.setImageResource(R.drawable.ic_unselect);
                } else {
                    smsPayload.setIsSelected(true);
                    itemIndicator.setImageResource(R.drawable.ic_select);
                }
            }
        });

        return view;
    }

    public void addSmses(List<SmsPayload> smses){
        this.smses = smses;
        notifyDataSetChanged();
    }

}
