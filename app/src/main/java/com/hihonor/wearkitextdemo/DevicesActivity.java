/*
 * Copyright (c) Honor Device Co., Ltd. 2022-2022. All rights reserved.
 */

package com.hihonor.wearkitextdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import com.hihonor.mcs.fitness.wear.WearKit;
import com.hihonor.mcs.fitness.wear.api.device.Device;
import com.hihonor.mcs.fitness.wear.api.device.DeviceClient;
import com.hihonor.mcs.fitness.wear.task.OnFailureListener;
import com.hihonor.mcs.fitness.wear.task.OnSuccessListener;
import com.hihonor.wearkitextdemo.utils.DeviceDataManager;

import java.util.List;

/**
 * DevicesActivity
 *
 * @since 2022-07-04
 */
public class DevicesActivity extends BaseActivity {
    private DeviceClient deviceClient;

    private RadioGroup getDevices;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_devices);
        init();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init() {
        TAG = DevicesActivity.class.getSimpleName();
        Log.i(TAG, "init start");
        getDevices = findViewById(R.id.get_devices);
        result = findViewById(R.id.result);
        deviceClient = WearKit.getDeviceClient(this);
        super.init();
        Log.i(TAG, "init end");
    }

    @Override
    protected void addViewListener() {
        super.addViewListener();
        Log.i(TAG, "addViewListener");
        getDevices.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Device device = DeviceDataManager.getInstance(getApplicationContext()).getDevices().get(i);
                DeviceDataManager.getInstance(getApplicationContext()).setDevice(device);
                setTextView("onCheckedChanged: " + device.getName());
            }
        });
    }

    /**
     * get Bonded Devices
     *
     * @param view view
     */
    public void getBondedDevices(View view) {
        Log.i(TAG, "getBondedDevices");
        deviceClient.getBondedDevices().addOnSuccessListener(new OnSuccessListener<List<Device>>() {
            @Override
            public void onSuccess(List<Device> devices) {
                if (devices == null || devices.size() == 0) {
                    setTextView("getBondedDevices is null or size is 0");
                    return;
                }
                setTextView("getBondedDevices onSuccess");
                DeviceDataManager.getInstance(getApplicationContext()).setDevices(devices);
                getDevices.removeAllViews();
                for (int i = 0; i < devices.size(); i++) {
                    Device device = devices.get(i);
                    Log.i(TAG,
                        "getBondedDevices name = " + device.getName() + " , isConnected = " + device.isConnected());
                    RadioButton deviceRadioButton = new RadioButton(DevicesActivity.this);
                    setRadioButton(deviceRadioButton, device.getName(), i, device.isConnected());
                    getDevices.addView(deviceRadioButton);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                setTextView("getBondedDevices onFailure: " + e.getMessage());
            }
        });
    }

    private void setRadioButton(RadioButton radioButton, String text, int id, boolean isConnect) {
        radioButton.setChecked(isConnect);
        radioButton.setId(id);
        radioButton.setText(text);
    }

    /**
     * get Common Devices
     *
     * @param view view
     */
    public void getCommonDevices(View view) {
        Log.i(TAG, "getCommonDevices");
        deviceClient.getCommonDevice().addOnSuccessListener(new OnSuccessListener<List<Device>>() {
            @Override
            public void onSuccess(List<Device> devices) {
                if (devices == null || devices.size() == 0) {
                    setTextView("getCommonDevices is null or size is 0");
                    return;
                }
                setTextView("getCommonDevices onSuccess");
                DeviceDataManager.getInstance(getApplicationContext()).setDevices(devices);
                getDevices.removeAllViews();
                for (int i = 0; i < devices.size(); i++) {
                    Device device = devices.get(i);
                    Log.i(TAG,
                        "getCommonDevices name = " + device.getName() + " , isConnected = " + device.isConnected());
                    RadioButton deviceRadioButton = new RadioButton(DevicesActivity.this);
                    setRadioButton(deviceRadioButton, device.getName(), i, device.isConnected());
                    getDevices.post(new Runnable() {
                        @Override
                        public void run() {
                            getDevices.addView(deviceRadioButton);
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                setTextView("getCommonDevices onFailure: " + e.getMessage());
            }
        });
    }

    /**
     * has Available Devices
     *
     * @param view view
     */
    public void hasAvailableDevices(View view) {
        Log.i(TAG, "hasAvailableDevices");
        deviceClient.hasAvailableDevices().addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean isAvailable) {
                // true: 当前有可用穿戴设备 false：当前无可用穿戴设备
                setTextView("hasAvailableDevices onSuccess: " + isAvailable);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                setTextView("hasAvailableDevices onFailure: " + e.getMessage());
            }
        });
    }

    /**
     * get Available Kbytes
     *
     * @param view view
     */
    public void getAvailableKbytes(View view) {
        Log.i(TAG, "getAvailableKbytes");
        if (!checkDeviceIsConnected()) {
            return;
        }
        deviceClient.getAvailableKbytes(DeviceDataManager.getInstance(getApplicationContext()).getDevice())
            .addOnSuccessListener(new OnSuccessListener<Integer>() {
                @Override
                public void onSuccess(Integer size) {
                    setTextView("getAvailableKbytes onSuccess: " + size);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    setTextView("getAvailableKbytes onFailure: " + e.getMessage());
                }
            });
    }
}