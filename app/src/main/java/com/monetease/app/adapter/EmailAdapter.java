package com.monetease.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.monetease.app.MoneteaseApplication;
import com.monetease.app.R;
import com.monetease.app.helper.UiHelper;
import com.monetease.app.model.EmailPayload;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vikas on 13/01/16.
 */
public class EmailAdapter extends BaseAdapter {

    private static LayoutInflater inflater=null;
    private UiHelper uiHelper;
    List<EmailPayload> emails = new ArrayList<EmailPayload>();
    private int fontSize;

    public EmailAdapter(Context context){
        inflater = LayoutInflater.from(context);
        uiHelper = ((MoneteaseApplication)context.getApplicationContext()).getUiHelper();
        fontSize = context.getResources().getDimensionPixelSize(R.dimen.app_title_small);
    }

    @Override
    public int getCount() {
        return emails.size();
    }

    @Override
    public EmailPayload getItem(int position) {
        return emails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final EmailPayload email = emails.get(position);
        if(view == null){
            view = inflater.inflate(R.layout.view_email_list_item, null);
        }

        TextView txtSenderName = (TextView)view.findViewById(R.id.txtSenderName);
        TextView txtMessage = (TextView)view.findViewById(R.id.txtMessage);
        TextView txtSubject = (TextView)view.findViewById(R.id.txtSubject);
        ImageView imageView = (ImageView)view.findViewById(R.id.imgSender);
        ImageView itemIndicator = (ImageView)view.findViewById(R.id.itemSelectionIndicator);

        if(email.isSelected()){
            itemIndicator.setImageResource(R.drawable.ic_select);
        }else {
            itemIndicator.setImageResource(R.drawable.ic_unselect);
        }

        txtSenderName.setText(email.getSenderName());
        txtMessage.setText(email.getSnippet());
        txtSubject.setText(email.getSubject());
        imageView.setImageDrawable(uiHelper.getTextDrawableWithRadius(email.getSenderName(), 50, fontSize));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageView itemIndicator = (ImageView) v.findViewById(R.id.itemSelectionIndicator);
                if (email.isSelected()) {
                    email.setIsSelected(false);
                    itemIndicator.setImageResource(R.drawable.ic_unselect);
                } else {
                    email.setIsSelected(true);
                    itemIndicator.setImageResource(R.drawable.ic_select);
                }
            }
        });

        return view;
    }

    public void setEmails(List<EmailPayload> emails){
        this.emails.addAll(emails);
        notifyDataSetChanged();
    }
}
