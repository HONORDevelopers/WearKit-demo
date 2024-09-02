/*
 * Copyright (c) Honor Device Co., Ltd. 2022-2022. All rights reserved.
 */

package com.hihonor.wearkitextdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * MainActivity
 *
 * @since 2022-07-04
 */
public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * startAutoActivity
     *
     * @param view view
     */
    public void startAutoActivity(View view) {
        startActivity(PermissionActivity.class);
    }

    /**
     * startClientActivity
     *
     * @param view view
     */
    public void startClientActivity(View view) {
        startActivity(ClientActivity.class);
    }

    /**
     * startMonitorActivity
     *
     * @param view view
     */
    public void startMonitorActivity(View view) {
        startActivity(MonitorActivity.class);
    }

    /**
     * startNotifyActivity
     *
     * @param view view
     */
    public void startNotifyActivity(View view) {
        startActivity(NotifyActivity.class);
    }

    /**
     * startP2pActivity
     *
     * @param view view
     */
    public void startP2pActivity(View view) {
        startActivity(P2pActivity.class);
    }

    /**
     * startDevicesActivity
     *
     * @param view view
     */
    public void startDevicesActivity(View view) {
        startActivity(DevicesActivity.class);
    }

    private void startActivity(Class cls) {
        Intent intent = new Intent(MainActivity.this, cls);
        startActivity(intent);
    }
}