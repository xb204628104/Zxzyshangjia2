package com.zxtyshangjia.zxzyshangjia.control.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zxtyshangjia.zxzyshangjia.R;

/**
 * Created by 18222 on 2017/9/6.
 * 绑定支付宝 跳转添加账号
 *
 */

public class BindAccountActivity extends Activity {


    private TextView mAddAcountTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bind_account_activity);
        mAddAcountTV = (TextView) findViewById(R.id.add_account_tv);
        mAddAcountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BindAccountActivity.this,AddAccountActivity.class));

            }
        });

    }

}
