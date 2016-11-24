package com.chniccs.lock;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ccs on 16/11/23.
 */

public class LockActivity extends AppCompatActivity {
    private DevicePolicyManager policyManager;
    private ComponentName componentName;
    private Button mBtn;
    private TextView mTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        LockScreen(null);
        mTv = (TextView) findViewById(R.id.tv_active_manager);
        mBtn = (Button) findViewById(R.id.btn_active);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activeManager();
            }
        });
    }

    public void LockScreen(View v) {
        policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, LockReceiver.class);
        if (policyManager.isAdminActive(componentName)) {//判断是否有权限(激活了设备管理器)
            policyManager.lockNow();// 直接锁屏
            finish();
//            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    // 解除绑定
    public void Bind(View v) {
        if (componentName != null) {
            policyManager.removeActiveAdmin(componentName);
            activeManager();
        }
    }

    @Override
    protected void onResume() {//重写此方法用来在第一次激活设备管理器之后锁定屏幕
        if (policyManager != null && policyManager.isAdminActive(componentName)) {
//            policyManager.lockNow();
//            android.os.Process.killProcess(android.os.Process.myPid());
            Toast.makeText(this, R.string.activited_des, Toast.LENGTH_SHORT).show();
            mTv.setText(R.string.activited_des);
            mBtn.setText(R.string.exit);
            mBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
        super.onResume();
    }

    private void activeManager() {
        //使用隐式意图调用系统方法来激活指定的设备管理器
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "一键锁屏");
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}

