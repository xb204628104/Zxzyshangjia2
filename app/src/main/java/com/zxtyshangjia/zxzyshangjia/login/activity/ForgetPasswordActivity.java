package com.zxtyshangjia.zxzyshangjia.login.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.app.Myappalication;
import com.zxtyshangjia.zxzyshangjia.commen.Api;
import com.zxtyshangjia.zxzyshangjia.commen.network.HttpUtil;
import com.zxtyshangjia.zxzyshangjia.commen.network.PostCallBack;
import com.zxtyshangjia.zxzyshangjia.commen.utils.ToastUtil;
import com.zxtyshangjia.zxzyshangjia.login.bean.BaseBean;

import java.util.HashMap;
import java.util.Map;

/**
 * 忘记密码找回密码页
 */

public class ForgetPasswordActivity extends Activity {

    /**
     * 手机号
     */
    private EditText mCallPhoneNumET;
    private String mCallPhoneNum;
    /**
     * 图形码
     */
    private EditText mGraphicCodeET;
    private String mGraphicCode;
    /**
     * 图形码 图片
     */
    private ImageView mGraphicCodeIV;
    /**
     * 验证码
     */
    private EditText mVerificationCodeET;
    private String mVerificationCode;
    /**
     * 获取验证码
     */
    private TextView mVerificationCodeTV;

    /**
     * 密码
     */
    private EditText mPasswordET;
    private String mPassword;
    /**
     * 确认密码
     */
    private EditText mConfirmPwordET;
    private String mConfirmPword;

    /**
     * 完成
     */
    private TextView mSubmitTV;

    /**
     * 获取图形码、获取验证码时传的参数 唯一值
     */
    int val = (int) (Math.random() * 100 + 1);
    private String mUserKey = System.currentTimeMillis() + "" + val;

    /**
     * 图形验证码链接
     */
    private String mUrl;

    /**
     * 获取验证码倒计时
     */

    Thread thread;

    private Boolean tag = true;

    private int i = 60;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_activity);
        initView();
        initData();
        initClick();
    }

    // 发送验证码回调
    private PostCallBack verifyBack = new PostCallBack() {
        @Override
        public void onSuccess(Object data) {
            if (data != null && data instanceof BaseBean) {
                BaseBean baseBean = (BaseBean) data;
                if (baseBean.flag.equals("success")) {
                    ToastUtil.showToast("信息已经发送，请注意查收");
                    changeBtnGetCode();

                } else {
                    ToastUtil.showToast(baseBean.message);
                }
            }
        }

        @Override
        public void onError(int code, String msg) {
            ToastUtil.showToast(msg);

        }
    };

    //找回密码回调

    private PostCallBack getPassBack = new PostCallBack() {
        @Override
        public void onSuccess(Object data) {
            if (data != null && data instanceof BaseBean) {
                BaseBean baseBean = (BaseBean) data;
                if(baseBean.flag.equals("success")){
                    ToastUtil.showToast("找回密码成功");
                    Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        }

        @Override
        public void onError(int code, String msg) {

        }
    };


    //点击事件
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == mGraphicCodeIV.getId()) {
                //图形验证码点击事件 点击切换图形验证码
                mUserKey = System.currentTimeMillis() + "" + val;
                mUrl = "https://te.zxty.me/index.php/Api/Login/verify?key=" + mUserKey;
                Myappalication.getGlideManager().inputImage(mUrl, mGraphicCodeIV);
            } else if (v.getId() == mVerificationCodeTV.getId()) {
                //获取验证码点击事件
                mCallPhoneNum = mCallPhoneNumET.getText().toString();     //手机号
                mGraphicCode = mGraphicCodeET.getText().toString();      //图形码
                if (mCallPhoneNum.equals("")) {
                    ToastUtil.showToast("电话号码不能为空");
                } else if (mGraphicCode.equals("")) {
                    ToastUtil.showToast("请填写图形码");
                } else if (!LoginActivity.isMobileNO(mCallPhoneNum)) {
                    ToastUtil.showToast("请填写正确的手机号码");
                } else {
                    //获取短信验证码
                    Map<String, String> map = new HashMap<>();
                    map.put("tel", mCallPhoneNum);
                    map.put("type", "getPass");
                    map.put("port", "1");
                    map.put("verify_code", mGraphicCode);
                    map.put("key", mUserKey);
                    new HttpUtil(Api.SENDVERIFY, map, BaseBean.class, verifyBack).postExecute();
                }
            } else if (v.getId() == mSubmitTV.getId()) {
                //找回密码提交
                mCallPhoneNum = mCallPhoneNumET.getText().toString();     //手机号
                mGraphicCode = mGraphicCodeET.getText().toString();      //图形码
                mVerificationCode = mVerificationCodeET.getText().toString(); //验证码
                mPassword = mPasswordET.getText().toString();       //密码
                mConfirmPword = mConfirmPwordET.getText().toString();    //确认密码
                if (mCallPhoneNum.equals("")) {
                    ToastUtil.showToast("手机号码不能为空");
                } else if (!LoginActivity.isMobileNO(mCallPhoneNum)) {
                    ToastUtil.showToast("请填写正确的手机号码");
                } else if (mGraphicCode.equals("")) {
                    ToastUtil.showToast("请填写图形码");
                } else if (mVerificationCode.equals("")) {
                    ToastUtil.showToast("请填写验证码");
                } else if (mPassword.equals("")) {
                    ToastUtil.showToast("密码不能为空");
                } else if (mConfirmPword.equals("")) {
                    ToastUtil.showToast("请填写确认密码");
                } else if (!mPassword.equals(mConfirmPword)) {
                    ToastUtil.showToast("您两次填写的密码不一致");
                } else {
                    //找回密码
                    Map<String, String> map = new HashMap<>();
                    map.put("way", mCallPhoneNum);
                    map.put("vc", mVerificationCode);
                    map.put("password", mPassword);
                    map.put("r_password", mConfirmPword);
                    map.put("verify_code", mGraphicCode);
                    new HttpUtil(Api.GET_PASS, map, BaseBean.class, getPassBack).postExecute();


                }


            }

        }
    };

    private void initView() {
        mCallPhoneNumET = (EditText) findViewById(R.id.callphone_num_fg_et);
        mGraphicCodeET = (EditText) findViewById(R.id.graphic_code_fg_et);
        mGraphicCodeIV = (ImageView) findViewById(R.id.graphic_code_fg_iv);
        mVerificationCodeTV = (TextView) findViewById(R.id.verification_code_fg_tv);
        mVerificationCodeET = (EditText) findViewById(R.id.verification_code_fg_et);
        mPasswordET = (EditText) findViewById(R.id.password_fg_et);
        mConfirmPwordET = (EditText) findViewById(R.id.confirm_pd_fg_et);
        mSubmitTV = (TextView) findViewById(R.id.fg_password_submit_tv);
    }

    private void initData() {
        //  页面初始化  验证码
        mUrl = "https://te.zxty.me/index.php/Api/Login/verify?key=" + mUserKey;
        Myappalication.getGlideManager().inputImage(mUrl, mGraphicCodeIV);

    }

    private void initClick() {
        mGraphicCodeIV.setOnClickListener(mClickListener);
        mVerificationCodeTV.setOnClickListener(mClickListener);
        mSubmitTV.setOnClickListener(mClickListener);
    }

    //获取验证码倒计时方法
    private void changeBtnGetCode() {
        thread = new Thread() {
            @Override
            public void run() {
                if (tag) {
                    while (i > 0) {
                        i--;
                        ForgetPasswordActivity.this
                                .runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mVerificationCodeTV.setText("获取验证码（"
                                                + i + "）");
                                        mVerificationCodeTV.setClickable(false);
                                    }
                                });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    tag = false;
                }
                i = 60;
                tag = true;
                if (ForgetPasswordActivity.this != null) {
                    ForgetPasswordActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mVerificationCodeTV.setText("获取验证码");
                            mVerificationCodeTV.setClickable(true);
                        }
                    });
                }
            }

            ;
        };
        thread.start();
    }


}
