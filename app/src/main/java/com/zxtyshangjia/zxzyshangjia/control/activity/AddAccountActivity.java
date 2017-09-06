package com.zxtyshangjia.zxzyshangjia.control.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.commen.utils.SpUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 18222 on 2017/9/6.
 * 添加支付宝账号
 */

public class AddAccountActivity extends Activity {

    private EditText mAccountET;
    private EditText mNameET;
    private String mAccount;
    private String mName;
    private TextView mAddConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_account_activity);
        mAccountET = (EditText) findViewById(R.id.zfb_account_et);
        mNameET = (EditText) findViewById(R.id.real_name_et);
        mAccount = mAccountET.getText().toString();
        mName = mNameET.getText().toString();
        String shop_id = SpUtils.getInstance(this).getString("shop_id","");
        String is_readonly = SpUtils.getInstance(this).getString("is_readonly","");
        mAddConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> map = new HashMap<>();
                
            }
        });

    }
}
