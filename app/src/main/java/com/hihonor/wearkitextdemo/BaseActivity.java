/*
 * Copyright (c) Honor Device Co., Ltd. 2022-2022. All rights reserved.
 */

package com.hihonor.wearkitextdemo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.hihonor.wearkitextdemo.utils.DeviceDataManager;

/**
 * BaseActivity
 *
 * @since 2022-07-04
 */
public class BaseActivity extends Activity {
    protected static String TAG = BaseActivity.class.getSimpleName();

    private static final String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE};

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static final int MAX_LINE_COUNT = 5;

    protected TextView result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        verifyStoragePermissions();
        addViewListener();
    }

    private void verifyStoragePermissions() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    /**
     * Initialize the view.
     */
    public void init() {
    }

    /**
     * Add view listener
     */
    protected void addViewListener() {
        if (result != null) {
            result.setMovementMethod(ScrollingMovementMethod.getInstance());
        }
    }

    /**
     * set TextView content
     *
     * @param content content
     */
    public void setTextView(String content) {
        Log.i(TAG, content);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.append(System.lineSeparator() + content);
                if (result.getLineCount() > MAX_LINE_COUNT) {
                    int offset = (result.getLineCount() - MAX_LINE_COUNT) * result.getLineHeight();
                    result.scrollTo(0, offset);
                }
            }
        });
    }

    /**
     * check Device Is Connected
     *
     * @return true: is Connected; false: not Connected
     */
    public boolean checkDeviceIsConnected() {
        if (DeviceDataManager.getInstance(getApplicationContext()).getDevice() == null) {
            setTextView("please select the target device!");
            return false;
        }
        if (!DeviceDataManager.getInstance(getApplicationContext()).getDevice().isConnected()) {
            setTextView("please connected the target device!");
            return false;
        }
        return true;
    }
}