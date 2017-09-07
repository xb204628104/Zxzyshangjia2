package com.zxtyshangjia.zxzyshangjia.control.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.commen.Api;
import com.zxtyshangjia.zxzyshangjia.commen.network.HttpUtil;
import com.zxtyshangjia.zxzyshangjia.commen.network.PostCallBack;
import com.zxtyshangjia.zxzyshangjia.commen.utils.SpUtils;
import com.zxtyshangjia.zxzyshangjia.commen.utils.ToastUtil;
import com.zxtyshangjia.zxzyshangjia.login.bean.BaseBean;

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
    private String shop_id;
    private String is_readonly;


    //绑定账号回调
    private PostCallBack bindBack = new PostCallBack() {
        @Override
        public void onSuccess(Object data) {
            if(data != null && data instanceof BaseBean){
                BaseBean bean = (BaseBean) data;
                if(bean.flag.equals("success")){
                    //绑定成功
                    ToastUtil.showToast("添加成功");
                    startActivity(new Intent(AddAccountActivity.this,BindAccountActivity.class));
                    finish();
                }
            }

        }

        @Override
        public void onError(int code, String msg) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_account_activity);
        mAccountET = (EditText) findViewById(R.id.zfb_account_et);
        mNameET = (EditText) findViewById(R.id.real_name_et);
        mAddConfirm = (TextView) findViewById(R.id.add_confirm_tv);
        shop_id = SpUtils.getInstance(this).getString("shop_id","");
        is_readonly = SpUtils.getInstance(this).getString("is_readonly","");
        mAddConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAccount = mAccountET.getText().toString();
                mName = mNameET.getText().toString();
                if(mName.equals("") || mAccount.equals("")){
                    ToastUtil.showToast("账号或密码不能为空");
                    return;
                }
                Map<String,String> map = new HashMap<>();
                map.put("type","0");
                map.put("account",mAccount);
                map.put("name",mName);
                map.put("id_type","1");
                map.put("mix_id",shop_id);
                map.put("is_readonly",is_readonly);
                new HttpUtil(Api.BINDPAY,map, BaseBean.class,bindBack).postExecute();

            }
        });

    }
}
