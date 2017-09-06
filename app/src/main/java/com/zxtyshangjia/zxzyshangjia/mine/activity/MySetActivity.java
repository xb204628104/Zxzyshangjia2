package com.zxtyshangjia.zxzyshangjia.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.app.Myappalication;
import com.zxtyshangjia.zxzyshangjia.commen.utils.WheelViewPopwindow;
import com.zxtyshangjia.zxzyshangjia.control.activity.MessagelistActivity;
import com.zxtyshangjia.zxzyshangjia.login.activity.LoginActivity;
import com.zxtyshangjia.zxzyshangjia.login.activity.ModifyPasswordActivity;

import java.util.ArrayList;

import static com.zxtyshangjia.zxzyshangjia.R.id.setting_bt_ok;

/**
 * 我的-设置页 商家资料
 */

public class MySetActivity extends Activity {

    /**
     *  商家名称
     */

    private EditText mShopNameET;
    private String mName;

    /**
     * 头像
     */
    private ImageView mHeadPic;
    private String mImgUrl;

    /**
     * 联系电话
     */

    private  EditText mCallPhoneET;
    private String mCallPhone;


    /**
     * 商家地址
     */
    private  EditText mAdressET;
    private String mAdress;

    /**
     * 商家营业时间
     */
    private  EditText mBusinesstimeET;
    private String mBusinesstime;

    /**
     * 商家公告
     */
    private  EditText mAnnouncementET;
    private String mAnnouncement;

    /**
     * 保存
     */
    private Button mSave;

    /**
     * 退出
     */
    private TextView mDropOut;

    /**
     * 修改密码
     */

    private TextView mPassword;

    /**
     * 我的消息按钮
     */

    private TextView mMessage;

    /**
     * 商家是否营业
     */
    private LinearLayout mBusinessLL;
    private TextView mIsBusinessTv;

    private ArrayList<String> mList = new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        mList.add("营业");
        mList.add("不营业");
        initView();
        initData();
        initClick();

    }

    //点击监听事件
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == mPassword.getId()){
                //修改密码
                Intent intent=new Intent(MySetActivity.this,ModifyPasswordActivity.class);
                startActivity(intent);
            }else if(v.getId() == mMessage.getId()){
                //我的消息
                Intent intent=new Intent(MySetActivity.this,MessagelistActivity.class);
                startActivity(intent);

            } else if(v.getId() == mSave.getId()){
                //保存



            } else if(v.getId() == mDropOut.getId()){
                //退出
                //TODO 这里需要改  记录是否是登录状态 但是现在还没写呢
                Intent intent=new Intent(MySetActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();


            }else if(v.getId() == mBusinessLL.getId()){
                //弹出营业 不营业 选项框
                new WheelViewPopwindow(MySetActivity.this, "分类", mList,mIsBusinessTv).showPop();
            }

        }
    };

    private void initData() {
        //页面初始化
        mImgUrl = getIntent().getStringExtra("imgUrl");
        mName = getIntent().getStringExtra("mName");
        mCallPhone = getIntent().getStringExtra("mCallphone");

        Myappalication.getGlideManager().inputImage(mImgUrl,mHeadPic); //头像
        mShopNameET.setText(mName); //商家名称
        mCallPhoneET.setText(mCallPhone);   //电话



    }

    public void initView(){
        mShopNameET= (EditText) findViewById(R.id.name_setting_et);
        mPassword= (TextView) findViewById(R.id.password_setting_tv);
        mMessage= (TextView) findViewById(R.id.message_set_tv);
        mHeadPic= (ImageView) findViewById(R.id.headpic_setting_iv);
        mCallPhoneET= (EditText) findViewById(R.id.phone_setting_et);
        mAdressET= (EditText) findViewById(R.id.adress_setting_et);
        mBusinesstimeET= (EditText) findViewById(R.id.time_setting_et);
        mAnnouncementET= (EditText) findViewById(R.id.setting_et_gonggao);
        mSave= (Button) findViewById(setting_bt_ok);
        mDropOut = (TextView) findViewById(R.id.drop_out_set_tv);
        mBusinessLL = (LinearLayout) findViewById(R.id.business_ll);
        mIsBusinessTv = (TextView) findViewById(R.id.is_business_tv);
    }


    private void initClick(){
        mPassword.setOnClickListener(mClickListener);
        mMessage.setOnClickListener(mClickListener);
        mSave.setOnClickListener(mClickListener);
        mDropOut.setOnClickListener(mClickListener);
        mBusinessLL.setOnClickListener(mClickListener);

    }


}
