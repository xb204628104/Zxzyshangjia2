package com.zxtyshangjia.zxzyshangjia.login.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.commen.Api;
import com.zxtyshangjia.zxzyshangjia.commen.network.HttpUtil;
import com.zxtyshangjia.zxzyshangjia.commen.network.PostCallBack;
import com.zxtyshangjia.zxzyshangjia.commen.utils.SpUtils;
import com.zxtyshangjia.zxzyshangjia.commen.utils.ToastUtil;
import com.zxtyshangjia.zxzyshangjia.login.bean.BaseBean;

import java.util.HashMap;
import java.util.Map;

import static com.zxtyshangjia.zxzyshangjia.R.id.possword_et_yuanmima;

/**
 * 修改密码页    TODO  待完善
 * Created by LvJinrong on 2017/2/28.
 */

public class ModifyPasswordActivity extends Activity {
    /**
     * 原密码
     */
    private EditText oldPassword;
    /**
     * 密码
     */
    private EditText nowPassword;
    /**
     * 再次确认密码
     */
    private EditText confirmPassword;
    /**
     * 修改
     */
    private Button modifyPassword;
    /**
     * 原密码
     */
    private String oldPword;
    /**
     * 密码
     */
    private String nowPword;
    /**
     * 再次确认的密码
     */
    private String conPword;

    /**
     * 商家登录账号
     */
    private String account;

    /**
     * 判断商家登录状态，1是用只读密码登录
     */
    private String is_readonly;

    private PostCallBack modifyPwordBack = new PostCallBack() {
        @Override
        public void onSuccess(Object data) {
            if (data != null && data instanceof BaseBean) {
                BaseBean baseBean = (BaseBean) data;
                if (baseBean.flag.equals("success")) {
                    ToastUtil.showToast("密码修改成功");
                }else {
                    ToastUtil.showToast(baseBean.message);
                }

            }
        }

        @Override
        public void onError(int code, String msg) {
            ToastUtil.showToast(msg);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_possword_activity);
        initView();
        initClick();

    }

    public void initView() {
        oldPassword = (EditText) findViewById(possword_et_yuanmima);
        nowPassword = (EditText) findViewById(R.id.possword_et_mima);
        confirmPassword = (EditText) findViewById(R.id.possword_et_qurenmima);
        modifyPassword = (Button) findViewById(R.id.possword_bt_xiugai);
    }

    private void initClick() {
        modifyPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPword = oldPassword.getText().toString();
                nowPword = nowPassword.getText().toString();
                conPword = confirmPassword.getText().toString();
                if (oldPword.equals("")) {
                    ToastUtil.showToast("请输入原密码");
                } else if (nowPassword.equals("")) {
                    ToastUtil.showToast("请输入密码");
                } else if (conPword.equals("")) {
                    ToastUtil.showToast("请再次确认密码");
                } else if (!conPword.equals(nowPword)) {
                    ToastUtil.showToast("您两次输入的密码不一致");
                } else if (oldPword.equals(nowPword)) {
                    ToastUtil.showToast("您的新密码和旧密码相同");
                } else {
                    //修改密码

                    account = SpUtils.getInstance(ModifyPasswordActivity.this).getString("account","");
                    is_readonly = SpUtils.getInstance(ModifyPasswordActivity.this).getString("is_readonly","");
                    Map<String, String> map = new HashMap<>();
                    map.put("account", account);
                    map.put("old_password", oldPword);
                    map.put("new_password", nowPword);
                    map.put("r_password", conPword);
                    map.put("is_readonly", is_readonly);
                    new HttpUtil(Api.MOFDIFY_Password, map, BaseBean.class, modifyPwordBack).postExecute();

                }

            }
        });
    }
}
