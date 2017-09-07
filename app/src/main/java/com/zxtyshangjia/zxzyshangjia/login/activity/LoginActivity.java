package com.zxtyshangjia.zxzyshangjia.login.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zxtyshangjia.zxzyshangjia.MainActivity;
import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.commen.Api;
import com.zxtyshangjia.zxzyshangjia.commen.network.HttpUtil;
import com.zxtyshangjia.zxzyshangjia.commen.network.PostCallBack;
import com.zxtyshangjia.zxzyshangjia.commen.utils.SpUtils;
import com.zxtyshangjia.zxzyshangjia.commen.utils.ToastUtil;
import com.zxtyshangjia.zxzyshangjia.login.bean.DatasBean;
import com.zxtyshangjia.zxzyshangjia.login.bean.LoginDateBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录页
 */

public class LoginActivity extends Activity {

    /**
     * 注册
     */
    private TextView tv_regist;
    /**
     * 忘记密码
     */
    private TextView tv_forgetword;
    /**
     * 手机号
     */
    private EditText et_phonenumber;
    /**
     * 登录密码
     */
    private EditText et_loginword;
    /**
     * 登录
     */
    private Button bt_login;
    /**
     * 密码
     */
    private String password = null;
    /**
     * 手机号
     */
    private String account = null;

    private List<DatasBean> userData = new ArrayList<>();


    //回调
    private PostCallBack loginCallBack = new PostCallBack() {

        @Override
        public void onSuccess(Object data) {
            if (data != null && data instanceof LoginDateBean) {
                LoginDateBean loginDateBean = (LoginDateBean) data;
                if (loginDateBean.flag.equals("success")) {
                    ToastUtil.showToast("登录成功");
                    SpUtils.getInstance(LoginActivity.this).save("account", account);
                    SpUtils.getInstance(LoginActivity.this).save("is_readonly", loginDateBean.data.is_readonly);
                    SpUtils.getInstance(LoginActivity.this).save("shop_id",loginDateBean.data.shop_id);
                    SpUtils.getInstance(LoginActivity.this).save("index",0);
                    Log.e("**********",account);
                    Log.e("**********",loginDateBean.data.is_readonly);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    ToastUtil.showToast(loginDateBean.message);
                }
            }
        }

        @Override
        public void onError(int code, String msg) {

            ToastUtil.showToast(msg);
        }
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initView();
        initClick();

    }

    //初始化控件
    public void initView() {
        et_phonenumber = (EditText) findViewById(R.id.et_phonenumber);
        et_loginword = (EditText) findViewById(R.id.et_loginword);
        tv_regist = (TextView) findViewById(R.id.tv_regist);
        tv_forgetword = (TextView) findViewById(R.id.tv_forgetword);
        bt_login = (Button) findViewById(R.id.bt_login);
    }

    private void initClick() {
        //设置注册的点击事件
        tv_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistActivity.class));
            }
        });

        //设置忘记密码的点击事件
        tv_forgetword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgetPasswordActivity.class));
            }
        });

        //设置登录按钮的点击事件
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = et_phonenumber.getText().toString();
                password = et_loginword.getText().toString();
                if (account.equals("")) {
                    ToastUtil.showToast("请填写手机号码");
                } else if (!isMobileNO(account)) {
                    ToastUtil.showToast("请填写正确的手机号码");
                } else if (password.equals("")) {
                    ToastUtil.showToast("密码不能为空");
                } else {
                    //登录
                    Map<String, String> map = new HashMap<>();
                    map.put("account", account);
                    map.put("password", password);
                    new HttpUtil(Api.USER_Login, map, LoginDateBean.class, loginCallBack).postExecute();
                }
            }
        });
    }

    /**
     *  
     *  * 验证手机格式 
     *  
     */
    public static boolean isMobileNO(String mobiles) {
   /* 
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188 
    联通：130、131、132、152、155、156、185、186 
    电信：133、153、180、189、（1349卫通） 
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9 
    */
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。  
        String telRegex = "[1][358]\\d{9}";
        return mobiles.matches(telRegex);
    }

}
