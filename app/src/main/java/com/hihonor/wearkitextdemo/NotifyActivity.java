/*
 * Copyright (c) Honor Device Co., Ltd. 2022-2022. All rights reserved.
 */

package com.hihonor.wearkitextdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.hihonor.mcs.fitness.wear.WearKit;
import com.hihonor.mcs.fitness.wear.api.notify.Action;
import com.hihonor.mcs.fitness.wear.api.notify.Notification;
import com.hihonor.mcs.fitness.wear.api.notify.NotificationTemplate;
import com.hihonor.mcs.fitness.wear.api.notify.NotifyClient;
import com.hihonor.mcs.fitness.wear.task.OnFailureListener;
import com.hihonor.mcs.fitness.wear.task.OnSuccessListener;
import com.hihonor.wearkitextdemo.utils.DeviceDataManager;

import java.util.HashMap;

/**
 * NotifyActivity
 *
 * @since 2022-07-04
 */
public class NotifyActivity extends BaseActivity {
    private static final int NOTIFY_TEMPLATE_NO_BUTTON = 50;

    private static final int NOTIFY_TEMPLATE_ONE_BUTTON = 51;

    private static final int NOTIFY_TEMPLATE_TWO_BUTTON = 52;

    private static final int NOTIFY_TEMPLATE_THREE_BUTTON = 53;

    private NotifyClient notifyClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_notify);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init() {
        TAG = NotifyActivity.class.getSimpleName();
        Log.i(TAG, "init start");
        result = findViewById(R.id.result);
        notifyClient = WearKit.getNotifyClient(this);
        super.init();
        Log.i(TAG, "init end");
    }

    /**
     * notify Template No Button
     *
     * @param view view
     */
    public void notifyTemplateNoButton(View view) {
        Log.i(TAG, "notifyTemplateNoButton");
        createNotify(NOTIFY_TEMPLATE_NO_BUTTON);
    }

    /**
     * notify Template One Button
     *
     * @param view view
     */
    public void notifyTemplateOneButton(View view) {
        Log.i(TAG, "notifyTemplateOneButton");
        createNotify(NOTIFY_TEMPLATE_ONE_BUTTON);
    }

    /**
     * notify Template Two Button
     *
     * @param view view
     */
    public void notifyTemplateTwoButton(View view) {
        Log.i(TAG, "notifyTemplateTwoButton");
        createNotify(NOTIFY_TEMPLATE_TWO_BUTTON);
    }

    /**
     * notify Template Three Button
     *
     * @param view view
     */
    public void notifyTemplateThreeButton(View view) {
        Log.i(TAG, "notifyTemplateThreeButton");
        createNotify(NOTIFY_TEMPLATE_THREE_BUTTON);
    }

    private void createNotify(int templateId) {
        Log.i(TAG, "createNotify, templateId: " + templateId);
        Notification.Builder builder = new Notification.Builder();
        builder.setPackageName("com.test.notify");
        builder.setTitle("title notify");
        builder.setText("text notify");
        notifyType(templateId, builder);
        builder.setAction(new Action() {
            @Override
            public void onError(Notification notification, int errCode, String s) {
                setTextView("notifyTem onError: " + notification.toString() + ", errCode: " + errCode);
            }

            @Override
            public void onResult(Notification notification, int feedBack) {
                // 处理notify返回发送成功的结果
                // feedback == 0: 物理HOME键退出或者灭屏
                // feedback == 1: 删除消息
                // feedback == 2: 用户点击第一个按钮
                // feedback == 3: 用户点击第二个按钮
                // feedback == 4：用户点击第三个按钮
                setTextView("notifyTem onResult: " + ", feedBack: " + feedBack);
            }
        });
        Notification notification = builder.build();

        notifyClient.notify(DeviceDataManager.getInstance(this).getDevice(), notification)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void object) {
                    setTextView("notifyTem onSuccess");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    setTextView("notifyTem onFailure: " + e.getMessage());
                }
            });
    }

    private void notifyType(int templateId, Notification.Builder builder) {
        Log.i(TAG, "notifyType, templateId: " + templateId);
        HashMap<Integer, String> buttonContents = new HashMap<>();
        switch (templateId) {
            case NOTIFY_TEMPLATE_NO_BUTTON:
                builder.setTemplateId(NotificationTemplate.NOTIFICATION_TEMPLATE_NO_BUTTON);
                break;
            case NOTIFY_TEMPLATE_ONE_BUTTON:
                builder.setTemplateId(NotificationTemplate.NOTIFICATION_TEMPLATE_ONE_BUTTON);
                buttonContents.put(NotificationTemplate.NOTIFICATION_TEMPLATE_ONE_BUTTON.value(), "notify");
                builder.setButtonContents(buttonContents);
                break;
            case NOTIFY_TEMPLATE_TWO_BUTTON:
                builder.setTemplateId(NotificationTemplate.NOTIFICATION_TEMPLATE_TWO_BUTTONS);
                buttonContents.put(NotificationTemplate.NOTIFICATION_TEMPLATE_ONE_BUTTON.value(), "notify");
                buttonContents.put(NotificationTemplate.NOTIFICATION_TEMPLATE_TWO_BUTTONS.value(), "notify2");
                builder.setButtonContents(buttonContents);
                break;
            case NOTIFY_TEMPLATE_THREE_BUTTON:
                builder.setTemplateId(NotificationTemplate.NOTIFICATION_TEMPLATE_THREE_BUTTONS);
                buttonContents.put(NotificationTemplate.NOTIFICATION_TEMPLATE_ONE_BUTTON.value(), "notify");
                buttonContents.put(NotificationTemplate.NOTIFICATION_TEMPLATE_TWO_BUTTONS.value(), "notify2");
                buttonContents.put(NotificationTemplate.NOTIFICATION_TEMPLATE_THREE_BUTTONS.value(), "notify3");
                builder.setButtonContents(buttonContents);
                break;
            default:
                break;
        }
    }
}