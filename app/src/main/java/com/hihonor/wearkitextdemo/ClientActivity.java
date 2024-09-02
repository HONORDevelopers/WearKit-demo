/*
 * Copyright (c) Honor Device Co., Ltd. 2022-2022. All rights reserved.
 */

package com.hihonor.wearkitextdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.hihonor.mcs.fitness.wear.WearKit;
import com.hihonor.mcs.fitness.wear.api.connect.ServiceConnectionListener;
import com.hihonor.mcs.fitness.wear.api.connect.WearKitClient;
import com.hihonor.mcs.fitness.wear.task.OnFailureListener;
import com.hihonor.mcs.fitness.wear.task.OnSuccessListener;

/**
 * ClientActivity
 *
 * @since 2022-07-04
 */
public class ClientActivity extends BaseActivity {
    private WearKitClient wearKitClient;

    private ServiceConnectionListener mServiceConnectionListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_client);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init() {
        TAG = ClientActivity.class.getSimpleName();
        Log.i(TAG, "init start");
        result = findViewById(R.id.result);
        mServiceConnectionListener = new ServiceConnectionListener() {
            @Override
            public void onServiceConnect() {
                setTextView("onServiceConnect");
            }

            @Override
            public void onServiceDisconnect() {
                setTextView("onServiceDisconnect");
            }
        };
        wearKitClient = WearKit.getWearKitClient(this, mServiceConnectionListener);
        super.init();
        Log.i(TAG, "init end");
    }

    /**
     * get Client Api Level
     *
     * @param view view
     */
    public void getClientApiLevel(View view) {
        Log.i(TAG, "getClientApiLevel");
        wearKitClient.getClientApiLevel().addOnSuccessListener(new OnSuccessListener<Integer>() {
            @Override
            public void onSuccess(Integer apiLever) {
                setTextView("getClientApiLevel onSuccess: " + apiLever);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                setTextView("getClientApiLevel onFailure: " + e.getMessage());
            }
        });
    }

    /**
     * get Service Api Level
     *
     * @param view view
     */
    public void getServiceApiLevel(View view) {
        Log.i(TAG, "getServiceApiLevel");
        wearKitClient.getServiceApiLevel().addOnSuccessListener(new OnSuccessListener<Integer>() {
            @Override
            public void onSuccess(Integer apiLever) {
                setTextView("getServiceApiLevel onSuccess: " + apiLever);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                setTextView("getServiceApiLevel onFailure: " + e.getMessage());
            }
        });
    }

    /**
     * register Service Connection Listener
     *
     * @param view view
     */
    public void registerServiceConnectionListener(View view) {
        Log.i(TAG, "registerServiceConnectionListener");
        wearKitClient.registerServiceConnectionListener().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void object) {
                setTextView("registerServiceConnectionListener onSuccess");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                setTextView("registerServiceConnectionListener onFailure: " + e.getMessage());
            }
        });
    }

    /**
     * release Connection
     *
     * @param view view
     */
    public void releaseConnection(View view) {
        Log.i(TAG, "releaseConnection");
        wearKitClient.releaseConnection().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void object) {
                setTextView("releaseConnection onSuccess");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                setTextView("releaseConnection onFailure: " + e.getMessage());
            }
        });
    }

    /**
     * unregister Service Connection Listener
     *
     * @param view view
     */
    public void unregisterServiceConnectionListener(View view) {
        Log.i(TAG, "unregisterServiceConnectionListener");
        wearKitClient.unregisterServiceConnectionListener().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void object) {
                setTextView("unregisterServiceConnectionListener onSuccess");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                setTextView("unregisterServiceConnectionListener onFailure: " + e.getMessage());
            }
        });
    }
}