/*
 * Copyright (c) Honor Device Co., Ltd. 2022-2022. All rights reserved.
 */

package com.hihonor.wearkitextdemo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.hihonor.mcs.fitness.wear.WearKit;
import com.hihonor.mcs.fitness.wear.api.p2p.CancelFileTransferCallBack;
import com.hihonor.mcs.fitness.wear.api.p2p.FileIdentification;
import com.hihonor.mcs.fitness.wear.api.p2p.Message;
import com.hihonor.mcs.fitness.wear.api.p2p.P2pClient;
import com.hihonor.mcs.fitness.wear.api.p2p.PingCallback;
import com.hihonor.mcs.fitness.wear.api.p2p.Receiver;
import com.hihonor.mcs.fitness.wear.api.p2p.SendCallback;
import com.hihonor.mcs.fitness.wear.task.OnFailureListener;
import com.hihonor.mcs.fitness.wear.task.OnSuccessListener;
import com.hihonor.wearkitextdemo.utils.DeviceDataManager;
import com.hihonor.wearkitextdemo.utils.SelectFileManager;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * P2pActivity
 *
 * @since 2022-07-04
 */
public class P2pActivity extends BaseActivity {
    private static final int SELECT_FILE_CODE = 1;

    private P2pClient p2pClient;

    private EditText mTargetPkgName;

    private EditText mTargetFingerPrint;

    private EditText mSendMsg;

    private PingCallback mPingCallback;

    private Receiver mReceiver;

    private SendCallback mSendCallback;

    private CancelFileTransferCallBack mCancelFileTransferCallBack;

    private String mFilePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_p2p);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init() {
        TAG = P2pActivity.class.getSimpleName();
        Log.i(TAG, "init start");
        p2pClient = WearKit.getP2pClient(this);
        mTargetPkgName = findViewById(R.id.target_pkg_name);
        mTargetFingerPrint = findViewById(R.id.target_finger_print);
        mSendMsg = findViewById(R.id.send_message);
        result = findViewById(R.id.result);
        super.init();
        Log.i(TAG, "init end");
    }

    /**
     * get App Version
     *
     * @param view view
     */
    public void getAppVersion(View view) {
        Log.i(TAG, "getAppVersion");
        if (!checkDeviceIsConnected()) {
            return;
        }
        String targetPkgName = mTargetPkgName.getText().toString();
        if (TextUtils.isEmpty(targetPkgName)) {
            setTextView("getAppVersion fail, please set peerPkgName");
            return;
        }
        p2pClient.getAppVersion(DeviceDataManager.getInstance(getApplicationContext()).getDevice(), targetPkgName)
            .addOnSuccessListener(new OnSuccessListener<Integer>() {
                @Override
                public void onSuccess(Integer version) {
                    // -1：该应用未安装
                    setTextView("getAppVersion onSuccess: " + version);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    setTextView("getAppVersion onFailure: " + e.getMessage());
                }
            });
    }

    /**
     * is App Installed
     *
     * @param view view
     */
    public void isAppInstalled(View view) {
        Log.i(TAG, "isAppInstalled");
        if (!checkDeviceIsConnected()) {
            return;
        }
        String targetPkgName = mTargetPkgName.getText().toString();
        if (TextUtils.isEmpty(targetPkgName)) {
            setTextView("isAppInstalled fail, please set peerPkgName");
            return;
        }
        p2pClient.isAppInstalled(DeviceDataManager.getInstance(getApplicationContext()).getDevice(), targetPkgName)
            .addOnSuccessListener(new OnSuccessListener<Boolean>() {
                @Override
                public void onSuccess(Boolean isInstalled) {
                    // true：已安装 false：未安装
                    setTextView("isAppInstalled onSuccess: " + isInstalled);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    setTextView("isAppInstalled onFailure: " + e.getMessage());
                }
            });
    }

    /**
     * ping
     *
     * @param view view
     */
    public void ping(View view) {
        Log.i(TAG, "ping");
        if (!checkDeviceIsConnected()) {
            return;
        }
        String targetPkgName = mTargetPkgName.getText().toString();
        if (TextUtils.isEmpty(targetPkgName)) {
            setTextView("ping fail, please set peerPkgName");
            return;
        }
        if (mPingCallback == null) {
            createPingCallback();
        }

        p2pClient.setPeerPkgName(targetPkgName);
        p2pClient.ping(DeviceDataManager.getInstance(getApplicationContext()).getDevice(), mPingCallback)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void object) {
                    Log.i(TAG, "ping onSuccess");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    setTextView("ping onFailure: " + e.getMessage());
                }
            });
    }

    private void createPingCallback() {
        Log.i(TAG, "createPingCallback");
        mPingCallback = new PingCallback() {
            @Override
            public void onPingResult(int code) {
                // code 为 200 表示穿戴设备侧应用未安装，201 表示穿戴设备侧应用已安装未启动，202 表示穿戴设备侧应用已启动
                setTextView("ping onPingResult: " + code);
            }
        };
    }

    /**
     * register Receiver
     *
     * @param view view
     */
    public void registerReceiver(View view) {
        Log.i(TAG, "registerReceiver");
        if (!checkDeviceIsConnected()) {
            return;
        }
        String targetPkgName = mTargetPkgName.getText().toString();
        if (TextUtils.isEmpty(targetPkgName)) {
            setTextView("registerReceiver fail, please set peerPkgName");
            return;
        }
        String targetFingerPrint = mTargetFingerPrint.getText().toString();
        if (TextUtils.isEmpty(targetFingerPrint)) {
            setTextView("registerReceiver fail, please set peerFingerPrint");
            return;
        }
        if (mReceiver == null) {
            createReceiver();
        }
        p2pClient.setPeerPkgName(targetPkgName);
        p2pClient.setPeerFingerPrint(targetFingerPrint);
        p2pClient.registerReceiver(DeviceDataManager.getInstance(getApplicationContext()).getDevice(), mReceiver)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void object) {
                    setTextView("registerReceiver onSuccess");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    setTextView("registerReceiver onFailure: " + e.getMessage());
                }
            });
    }

    private void createReceiver() {
        Log.i(TAG, "createReceiver");
        mReceiver = new Receiver() {
            @Override
            public void onReceiveMessage(Message message) {
                if (message != null) {
                    if (message.getType() == Message.TYPE_DATA) {
                        String data = new String(message.getData(), StandardCharsets.UTF_8);
                        setTextView("onReceiveMessage data: " + data);
                    } else if (message.getType() == Message.TYPE_FILE) {
                        File file = message.getFile();
                        setTextView(
                            "onReceiveMessage file, file length: " + file.length() + ", file name: " + file.getName());
                    } else {
                        setTextView("Success Receiver Message, type unknown");
                    }
                } else {
                    setTextView("Success Receiver Message is null");
                }
            }
        };
    }

    /**
     * unregister Receiver
     *
     * @param view view
     */
    public void unregisterReceiver(View view) {
        Log.i(TAG, "unregisterReceiver");
        if (!checkDeviceIsConnected()) {
            return;
        }
        if (mReceiver == null) {
            setTextView("not register receiver, please register receiver");
            return;
        }
        p2pClient.unregisterReceiver(mReceiver).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void object) {
                setTextView("unregisterReceiver onSuccess");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                setTextView("unregisterReceiver onFailure: " + e.getMessage());
            }
        });
    }

    /**
     * send Msg
     *
     * @param view view
     */
    public void sendMsg(View view) {
        Log.i(TAG, "sendMsg");
        if (!checkDeviceIsConnected()) {
            return;
        }
        String targetPkgName = mTargetPkgName.getText().toString();
        if (TextUtils.isEmpty(targetPkgName)) {
            setTextView("sendMsg fail, please set peerPkgName");
            return;
        }
        String targetFingerPrint = mTargetFingerPrint.getText().toString();
        if (TextUtils.isEmpty(targetFingerPrint)) {
            setTextView("sendMsg fail, please set peerFingerPrint");
            return;
        }
        if (mSendCallback == null) {
            createSendCallBack();
        }
        p2pClient.setPeerPkgName(targetPkgName);
        p2pClient.setPeerFingerPrint(targetFingerPrint);
        Message.Builder builder = new Message.Builder();
        String msg = mSendMsg.getText().toString();
        if (!TextUtils.isEmpty(msg)) {
            builder.setPayload(msg.getBytes(StandardCharsets.UTF_8));
        }
        builder.setMessageType(Message.TYPE_DATA);
        Message message = builder.build();
        p2pClient.send(DeviceDataManager.getInstance(getApplicationContext()).getDevice(), message, mSendCallback)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void object) {
                    Log.i(TAG, "sendMsg onSuccess");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    setTextView("sendMsg onFailure: " + e.getMessage());
                }
            });
    }

    private void createSendCallBack() {
        Log.i(TAG, "createSendCallBack");
        mSendCallback = new SendCallback() {
            @Override
            public void onSendResult(int code) {
                // 发送消息：code 为 207 表示消息发送成功，其他表示消息发送失败
                // 发送文件：code 为 0 表示文件发送成功，其他表示文件发送失败
                setTextView("onSendResult: " + code);
            }

            @Override
            public void onSendProgress(long pro) {
                setTextView("onSendProgress: " + pro);
            }
        };
    }

    /**
     * select File And Send
     *
     * @param view view
     */
    public void selectFileAndSend(View view) {
        Log.i(TAG, "selectFileAndSend");
        if (!checkDeviceIsConnected()) {
            return;
        }
        String targetPkgName = mTargetPkgName.getText().toString();
        if (TextUtils.isEmpty(targetPkgName)) {
            setTextView("sendMsg fail, please set peerPkgName");
            return;
        }
        String targetFingerPrint = mTargetFingerPrint.getText().toString();
        if (TextUtils.isEmpty(targetFingerPrint)) {
            setTextView("sendMsg fail, please set peerFingerPrint");
            return;
        }
        if (mSendCallback == null) {
            createSendCallBack();
        }
        p2pClient.setPeerPkgName(targetPkgName);
        p2pClient.setPeerFingerPrint(targetFingerPrint);
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, SELECT_FILE_CODE);
        } catch (ActivityNotFoundException e) {
            setTextView("sendFile fail, ActivityNotFoundException");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_FILE_CODE && resultCode == RESULT_OK) {
            if (data == null) {
                setTextView("Invalid file data");
                return;
            }
            Uri selectFileUri = data.getData();
            mFilePath = SelectFileManager.getFilePath(this, selectFileUri);
            sendFile();
        }
    }

    private void sendFile() {
        Log.i(TAG, "sendFile mFilePath = " + mFilePath);
        if (TextUtils.isEmpty(mFilePath)) {
            setTextView("sendFile fail, file path is null");
            return;
        }
        Message.Builder builder = new Message.Builder();
        File sendFile = new File(mFilePath);
        builder.setPayload(sendFile);
        builder.setMessageType(Message.TYPE_FILE);
        Message message = builder.build();
        p2pClient.send(DeviceDataManager.getInstance(getApplicationContext()).getDevice(), message, mSendCallback)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void object) {
                    Log.i(TAG, "sendFile onSuccess");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    setTextView("sendFile onFailure: " + e.getMessage());
                }
            });
    }

    /**
     * cancel File Transfer
     *
     * @param view view
     */
    public void cancelFileTransfer(View view) {
        Log.i(TAG, "cancelFileTransfer");
        if (!checkDeviceIsConnected()) {
            return;
        }
        if (TextUtils.isEmpty(mFilePath)) {
            setTextView("cancelFileTransfer fail, file path is null");
            return;
        }
        String targetPkgName = mTargetPkgName.getText().toString();
        if (TextUtils.isEmpty(targetPkgName)) {
            setTextView("cancelFileTransfer fail, please set peerPkgName");
            return;
        }
        if (mCancelFileTransferCallBack == null) {
            createCancelFileTransferCallBack();
        }
        p2pClient.setPeerPkgName(targetPkgName);
        File cancelFile = new File(mFilePath);
        FileIdentification.Builder builder = new FileIdentification.Builder();
        builder.setFile(cancelFile);
        FileIdentification fileIdentification = builder.build();
        p2pClient
            .cancelFileTransfer(DeviceDataManager.getInstance(getApplicationContext()).getDevice(), fileIdentification,
                mCancelFileTransferCallBack)
            .addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object result) {
                    setTextView("cancelFileTransfer onSuccess");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    setTextView("cancelFileTransfer onFailure" + e.getMessage());
                }
            });
    }

    private void createCancelFileTransferCallBack() {
        Log.i(TAG, "createCancelFileTransferCallBack");
        mCancelFileTransferCallBack = new CancelFileTransferCallBack() {
            @Override
            public void onCancelFileTransferResult(int code) {
                /*
                 * 常见几个错误码解释如下：
                 * code:0 取消文件发送成功
                 * code:11 取消发送文件不存在
                 * code:5
                 * （1）调用取消发送文件接口，传的参数为空。
                 * （2）取消发送的文件大小超过100M
                 */
                setTextView("onCancelFileTransferResult: " + code);
            }
        };
    }
}