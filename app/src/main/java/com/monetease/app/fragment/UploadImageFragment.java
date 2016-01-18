package com.monetease.app.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;

import com.monetease.app.R;
import com.monetease.app.adapter.ImageAdapter;
import com.monetease.app.model.ImagePayload;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vikas on 10/01/16.
 */
public class UploadImageFragment extends Fragment {

    private ImageAdapter imageAdapter;
    private GridView imageListView;
    private ProgressDialog myProgressDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_upload_image, container, false);
        imageAdapter = new ImageAdapter(inflater.getContext());
        imageListView = (GridView)rootView.findViewById(R.id.imageListView);
        imageListView.setAdapter(imageAdapter);
        myProgressDialog = new ProgressDialog(this.getActivity());
        loadImages(inflater.getContext());
        return rootView;
    }

    private void loadImages(final Context context) {
        new AsyncTask<Void, List<ImagePayload>, List<ImagePayload>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                myProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                myProgressDialog.setMessage("Please wait...");
                myProgressDialog.show();

            }

            @Override
            protected List<ImagePayload> doInBackground(Void... params) {
                List<ImagePayload> imagePayloads = new ArrayList<ImagePayload>();
                Cursor cursor = context.getApplicationContext().getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI    , null, null, null,
                        MediaStore.Images.Media.DEFAULT_SORT_ORDER);
                if (cursor != null) {
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        cursor.moveToPosition(i);
                        ImagePayload imagePayload = new ImagePayload();
                        imagePayload.setId(1);
                        imagePayload.setUri(cursor.getString(1));
                        imagePayload.setTitle(cursor.getString(3));
                        imagePayloads.add(imagePayload);
                    }
                }
                cursor.close();
                return imagePayloads;
            }

            @Override
            protected void onPostExecute(List<ImagePayload> imagePayloads) {
                super.onPostExecute(imagePayloads);
                myProgressDialog.dismiss();
                imageAdapter.setImages(imagePayloads);

            }
        }.execute();
    }
}
