package com.monetease.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.monetease.app.fragment.UploadEmailFragment;
import com.monetease.app.fragment.UploadImageFragment;
import com.monetease.app.fragment.UploadSMSFragment;

import java.util.Locale;

/**
 * Created by vikas on 10/01/16.
 */
public class ViewPagerAdapterUpload extends FragmentPagerAdapter {

    public ViewPagerAdapterUpload(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        // Returns a new instance of the fragment
        switch (position) {
            case 0:
                return new UploadEmailFragment();
            case 1:
                return new UploadSMSFragment();
            case 2:
                return new UploadImageFragment();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return "EMAIL";
            case 1:
                return "SMS";
            case 2:
                return "IMAGE";
        }
        return null;
    }
}
