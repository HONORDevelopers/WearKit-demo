/*
 * Copyright (c) Honor Device Co., Ltd. 2022-2022. All rights reserved.
 */

package com.hihonor.wearkitextdemo.utils;

import android.content.Context;

import com.hihonor.mcs.fitness.wear.BaseClient;
import com.hihonor.mcs.fitness.wear.api.device.Device;

import java.util.ArrayList;
import java.util.List;

/**
 * DeviceDataManager
 *
 * @since 2022-08-03
 */
public class DeviceDataManager extends BaseClient {
    private static volatile DeviceDataManager deviceData;

    private Device mDevice;

    private List<Device> mDevices = new ArrayList<>();

    private DeviceDataManager(Context context) {
        super(context);
    }

    /**
     * get DeviceDataManager instance.
     *
     * @param context context
     * @return DeviceDataManager instance
     */
    public static DeviceDataManager getInstance(Context context) {
        if (deviceData == null) {
            synchronized (DeviceDataManager.class) {
                if (deviceData == null) {
                    deviceData = new DeviceDataManager(context);
                }
            }
        }
        return deviceData;
    }

    public void setDevice(Device device) {
        mDevice = device;
    }

    public Device getDevice() {
        return mDevice;
    }

    public List<Device> getDevices() {
        return mDevices;
    }

    public void setDevices(List<Device> devices) {
        this.mDevices = devices;
    }
}