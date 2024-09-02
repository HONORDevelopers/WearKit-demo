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
import com.hihonor.mcs.fitness.wear.api.monitor.MonitorClient;
import com.hihonor.mcs.fitness.wear.api.monitor.MonitorData;
import com.hihonor.mcs.fitness.wear.api.monitor.MonitorItem;
import com.hihonor.mcs.fitness.wear.api.monitor.MonitorListener;
import com.hihonor.mcs.fitness.wear.task.OnFailureListener;
import com.hihonor.mcs.fitness.wear.task.OnSuccessListener;
import com.hihonor.wearkitextdemo.utils.DeviceDataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * MonitorActivity
 *
 * @since 2022-07-04
 */
public class MonitorActivity extends BaseActivity {
    private MonitorClient mMonitorClient;

    private Device mDevice;

    private MonitorListener mMonitorListener;

    private MonitorItem mCurrentMonitorItem;

    private RadioGroup mSelectMonitor;

    private Map<String, MonitorItem> mMonitorMap = new HashMap<String, MonitorItem>() {
        {
            put("connectionStatus", MonitorItem.MONITOR_ITEM_CONNECTION);
            put("wearStatus", MonitorItem.MONITOR_ITEM_WEAR);
            put("sleepStatus", MonitorItem.MONITOR_ITEM_SLEEP);
            put("lowPower", MonitorItem.MONITOR_ITEM_LOW_POWER);
            put("sportStatus", MonitorItem.MONITOR_ITEM_SPORT);
            put("chargeStatus", MonitorItem.MONITOR_CHARGE_STATUS);
            put("heartRateAlarm", MonitorItem.MONITOR_ITEM_HEART_RATE_ALARM);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_monitor);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init() {
        TAG = MonitorActivity.class.getSimpleName();
        Log.i(TAG, "init start");
        result = findViewById(R.id.result);
        mSelectMonitor = findViewById(R.id.select_monitor);
        mMonitorClient = WearKit.getMonitorClient(this);
        mDevice = DeviceDataManager.getInstance(getApplicationContext()).getDevice();
        Iterator<Map.Entry<String, MonitorItem>> it = mMonitorMap.entrySet().iterator();
        int id = 0;
        while (it.hasNext()) {
            Map.Entry<String, MonitorItem> entry = it.next();
            RadioButton deviceRadioButton = new RadioButton(MonitorActivity.this);
            setRadioButton(deviceRadioButton, entry.getKey(), id);
            mSelectMonitor.addView(deviceRadioButton);
            id++;
        }
        mSelectMonitor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) group.getChildAt(checkedId);
                mCurrentMonitorItem = mMonitorMap.get(button.getText());
            }
        });
        super.init();
        Log.i(TAG, "init end");
    }

    private void setRadioButton(RadioButton radioButton, String text, int id) {
        if (id == 0) {
            radioButton.setChecked(true);
        } else {
            radioButton.setChecked(false);
        }
        radioButton.setId(id);
        radioButton.setText(text);
    }

    /**
     * query Monitor
     *
     * @param view view
     */
    public void queryMonitor(View view) {
        Log.i(TAG, "queryMonitor");
        if (!checkDeviceIsConnected()) {
            return;
        }
        mMonitorClient.query(mDevice, mCurrentMonitorItem).addOnSuccessListener(new OnSuccessListener<MonitorData>() {
            @Override
            public void onSuccess(MonitorData monitorData) {
                setTextView("queryMonitor onSuccess, status: " + monitorData.asInt());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                setTextView("queryMonitor onFailure: " + e.getMessage());
            }
        });
    }

    /**
     * register One Monitor
     *
     * @param view view
     */
    public void registerOneMonitor(View view) {
        Log.i(TAG, "registerOneMonitor");
        if (!checkDeviceIsConnected()) {
            return;
        }

        if (mMonitorListener == null) {
            createMonitorListener();
        }

        mMonitorClient.register(mDevice, mCurrentMonitorItem, mMonitorListener)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void object) {
                    setTextView("registerOneMonitor onSuccess");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    setTextView("registerOneMonitor onFailure: " + e.getMessage());
                }
            });
    }

    private void createMonitorListener() {
        Log.i(TAG, "createMonitorListener");
        mMonitorListener = new MonitorListener() {
            @Override
            public void onChanged(int i, MonitorItem monitorItem, MonitorData monitorData) {
                setTextView("onChanged: " + monitorItem.getName() + ", status changes: " + monitorData.asInt());
            }
        };
    }

    /**
     * register Many Monitor
     *
     * @param view view
     */
    public void registerManyMonitor(View view) {
        Log.i(TAG, "registerManyMonitor");
        if (!checkDeviceIsConnected()) {
            return;
        }

        if (mMonitorListener == null) {
            createMonitorListener();
        }

        List<MonitorItem> monitorItems = new ArrayList<>();
        Iterator<Map.Entry<String, MonitorItem>> it = mMonitorMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, MonitorItem> entry = it.next();
            monitorItems.add(entry.getValue());
        }
        mMonitorClient.register(mDevice, monitorItems, mMonitorListener)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void object) {
                    setTextView("registerManyMonitor onSuccess");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    setTextView("registerManyMonitor onFailure: " + e.getMessage());
                }
            });
    }

    /**
     * unRegister Monitor
     *
     * @param view view
     */
    public void unRegisterMonitor(View view) {
        Log.i(TAG, "unRegisterMonitor");
        if (mMonitorListener == null) {
            setTextView("not register monitor, please register monitor");
            return;
        }
        mMonitorClient.unregister(mMonitorListener).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void object) {
                setTextView("unRegisterMonitor onSuccess");
                mMonitorListener = null;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                setTextView("unRegisterMonitor onFailure: " + e.getMessage());
            }
        });
    }
}