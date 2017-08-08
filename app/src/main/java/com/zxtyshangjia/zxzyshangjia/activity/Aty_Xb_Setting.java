package com.zxtyshangjia.zxzyshangjia.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxtyshangjia.zxzyshangjia.R;

/**
 * Created by 18222 on 2017/8/7.
 */

public class Aty_Xb_Setting extends Activity {
    //名称的按钮
    private EditText setting_et_mingcheng;
    //修改密码的按钮
    private TextView setting_tv_mima;
    //我的消息按钮
    private TextView setting_tv_wode;
    //头像的按钮
    private ImageView setting_iv_touxiang;
    //联系电话的按钮
    private  EditText setting_et_phone;
    //商家地址按钮
    private  EditText setting_et_adress;
    //商家营业时间按钮
    private  EditText setting_et_time;
    //公告的按钮
    private  EditText setting_et_gonggao;
    //保存的按钮
    private Button setting_bt_ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mian_setting);
        initView();
        initClick();
        //隐藏光标
        //setting_et_mingcheng.setCursorVisible(false);
    }


    public void initView(){
        setting_et_mingcheng= (EditText) findViewById(R.id.setting_et_mingcheng);
        setting_tv_mima= (TextView) findViewById(R.id.setting_tv_mima);
        setting_tv_wode= (TextView) findViewById(R.id.setting_tv_wode);
        setting_iv_touxiang= (ImageView) findViewById(R.id.setting_iv_touxiang);
        setting_et_phone= (EditText) findViewById(R.id.setting_et_phone);
        setting_et_adress= (EditText) findViewById(R.id.setting_et_adress);
        setting_et_time= (EditText) findViewById(R.id.setting_et_time);
        setting_et_gonggao= (EditText) findViewById(R.id.setting_et_gonggao);
        setting_bt_ok= (Button) findViewById(R.id.setting_bt_ok);
    }
    private void initClick(){
        setting_tv_mima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Aty_Xb_Setting.this,Aty_Xb_Possword.class);
                startActivity(intent);
            }
        });
    }
}
