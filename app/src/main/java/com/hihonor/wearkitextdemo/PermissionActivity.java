/*
 * Copyright (c) Honor Device Co., Ltd. 2022-2022. All rights reserved.
 */

package com.hihonor.wearkitextdemo;

import static com.hihonor.mcs.fitness.wear.api.auth.Permission.DEVICE_MANAGER;
import static com.hihonor.mcs.fitness.wear.api.auth.Permission.NOTIFY;
import static com.hihonor.mcs.fitness.wear.api.auth.Permission.WEAR_USER_STATUS;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import com.hihonor.mcs.fitness.wear.WearKit;
import com.hihonor.mcs.fitness.wear.api.auth.AuthCallback;
import com.hihonor.mcs.fitness.wear.api.auth.AuthClient;
import com.hihonor.mcs.fitness.wear.api.auth.Permission;
import com.hihonor.mcs.fitness.wear.task.OnFailureListener;
import com.hihonor.mcs.fitness.wear.task.OnSuccessListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PermissionActivity
 *
 * @since 2022-07-04
 */
public class PermissionActivity extends BaseActivity {
    private RadioGroup radiogroup;

    private Map<String, Permission> authPermissionMap = new HashMap<String, Permission>() {
        {
            put(DEVICE_MANAGER.getName(), DEVICE_MANAGER);
            put(NOTIFY.getName(), NOTIFY);
            put(WEAR_USER_STATUS.getName(), WEAR_USER_STATUS);
        }
    };

    private AuthClient authClient;

    private Permission permission = DEVICE_MANAGER;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_permission);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init() {
        TAG = PermissionActivity.class.getSimpleName();
        Log.i(TAG, "init start");
        result = findViewById(R.id.result);
        radiogroup = (RadioGroup) findViewById(R.id.select_auto);
        authClient = WearKit.getAuthClient(this);
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.device_manager:
                        permission = DEVICE_MANAGER;
                        break;
                    case R.id.notify:
                        permission = NOTIFY;
                        break;
                    case R.id.wear_user_status:
                        permission = WEAR_USER_STATUS;
                        break;
                    default:
                        break;
                }
            }
        });
        super.init();
        Log.i(TAG, "init end");
    }

    /**
     * check Permission
     *
     * @param view view
     */
    public void checkPermission(View view) {
        Log.i(TAG, "checkPermission");
        authClient.checkPermission(permission).addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean isSuccess) {
                // true为授予，false为未授予
                setTextView("checkPermission onSuccess: " + isSuccess);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                setTextView("checkPermission onFailure: " + e.getMessage());
            }
        });
    }

    /**
     * check Permissions
     *
     * @param view view
     */
    public void checkPermissions(View view) {
        Log.i(TAG, "checkPermissions");
        List<String> permissionList = new ArrayList<>();
        permissionList.add(NOTIFY.getName());
        permissionList.add(DEVICE_MANAGER.getName());
        permissionList.add(WEAR_USER_STATUS.getName());

        Permission[] checkPermissions = new Permission[permissionList.size()];
        for (int i = 0; i < permissionList.size(); i++) {
            checkPermissions[i] = authPermissionMap.get(permissionList.get(i));
        }
        authClient.checkPermissions(checkPermissions).addOnSuccessListener(new OnSuccessListener<Boolean[]>() {
            @Override
            public void onSuccess(Boolean[] permissions) {
                // true为授予，false为未授予，按照权限的查询顺序返回对应的值
                StringBuilder builder = new StringBuilder("checkPermissions onSuccess:");
                for (int i = 0; i < permissions.length; i++) {
                    if (i == permissions.length - 1) {
                        builder.append(permissions[i]);
                    } else {
                        builder.append(permissions[i]);
                        builder.append(", ");
                    }
                }
                setTextView(builder.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                setTextView("checkPermissions onFailure: " + e.getMessage());
            }
        });
    }

    /**
     * request Permissions
     *
     * @param view view
     */
    public void requestPermissions(View view) {
        Log.i(TAG, "requestPermissions");
        List<String> requestPermissionList = new ArrayList<>();
        requestPermissionList.add(NOTIFY.getName());
        requestPermissionList.add(DEVICE_MANAGER.getName());
        requestPermissionList.add(WEAR_USER_STATUS.getName());

        Permission[] requestPermissions = new Permission[requestPermissionList.size()];
        for (int i = 0; i < requestPermissionList.size(); i++) {
            requestPermissions[i] = authPermissionMap.get(requestPermissionList.get(i));
        }
        AuthCallback callback = new AuthCallback() {
            @Override
            public void onOk(Permission[] permissions) {
                StringBuilder builder = new StringBuilder("requestPermission onOk:");
                builder.append(System.lineSeparator());
                for (int i = 0; i < permissions.length; i++) {
                    if (i == permissions.length - 1) {
                        builder.append(permissions[i].getName());
                    } else {
                        builder.append(permissions[i].getName());
                        builder.append(", ");
                    }
                }
                Log.i(TAG, "requestPermissions onOk result:" + builder);
                setTextView(builder.toString());
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "requestPermission onCancel");
                setTextView("requestPermission onCancel");
            }
        };
        authClient.requestPermissions(callback, requestPermissions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void object) {
                Log.i(TAG, "requestPermission onSuccess");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                setTextView("requestPermission onFailure: " + e.getMessage());
            }
        });
    }
}