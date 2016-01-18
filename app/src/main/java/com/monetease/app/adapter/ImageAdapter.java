package com.monetease.app.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.monetease.app.R;
import com.monetease.app.model.ImagePayload;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vikas on 14/01/16.
 */
public class ImageAdapter extends BaseAdapter {
    private List<ImagePayload> images = new ArrayList<ImagePayload>();
    private Context mContext;
    private static LayoutInflater inflater=null;

    public ImageAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return images.size();
    }

    public ImagePayload getItem(int position) {
        return images.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        final ImagePayload imagePayload = images.get(position);

        if(view == null){
            view = inflater.inflate(R.layout.view_image_list_item, null);
        }
        ImageView imageView = (ImageView)view.findViewById(R.id.imageImageAdd);
        ImageView itemIndicator = (ImageView)view.findViewById(R.id.itemSelectionIndicator);

        if(imagePayload.isSelected()){
            itemIndicator.setImageResource(R.drawable.ic_select);
        }else {
            itemIndicator.setImageResource(R.drawable.ic_unselect);
        }

        Picasso.with(mContext)
                .load(new File(Uri.parse(imagePayload.getUri()).getPath()))
                .fit()
                .centerCrop()
                .into(imageView);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageView itemIndicator = (ImageView) v.findViewById(R.id.itemSelectionIndicator);
                if (imagePayload.isSelected()) {
                    imagePayload.setIsSelected(false);
                    itemIndicator.setImageResource(R.drawable.ic_unselect);
                } else {
                    imagePayload.setIsSelected(true);
                    itemIndicator.setImageResource(R.drawable.ic_select);
                }
            }
        });

        return view;
    }

    public void setImages(List<ImagePayload> imagePayloads){
        this.images = imagePayloads;
        this.notifyDataSetChanged();
    }
}
