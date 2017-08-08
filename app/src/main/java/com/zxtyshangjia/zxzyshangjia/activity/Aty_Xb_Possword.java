package com.zxtyshangjia.zxzyshangjia.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zxtyshangjia.zxzyshangjia.R;

/**
 * Created by 18222 on 2017/8/8.
 */

public class Aty_Xb_Possword extends Activity {
    //原密码按钮
    private EditText possword_et_yuanmima;
    //密码的按钮
    private  EditText possword_et_mima;
    //确认密码的按钮
    private  EditText possword_et_qurenmima;
    //修改的按钮
    private Button possword_bt_xiugai;
    private String yuanmima;
    private String mima;
    private String qurenmima;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_possword);

        initView();//初始化
        textChange te=new textChange();//edittext监听器
        //监听事件
        possword_et_yuanmima.addTextChangedListener(te);
        possword_et_mima.addTextChangedListener(te);
        possword_et_qurenmima.addTextChangedListener(te);
    }
    public void initView(){
        possword_et_yuanmima= (EditText) findViewById(R.id.possword_et_yuanmima);
        possword_et_mima= (EditText) findViewById(R.id.possword_et_mima);
        possword_et_qurenmima= (EditText) findViewById(R.id.possword_et_qurenmima);
        possword_bt_xiugai= (Button) findViewById(R.id.possword_bt_xiugai);

    }

    @Override
    protected void onPause() {


        super.onPause();
    }

    //EditText监听器
    class textChange implements TextWatcher {
        @Override
        public void afterTextChanged(Editable arg0) {
        }
        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }
        @Override
//文本正在改变的时候 触发
        public void onTextChanged(CharSequence cs, int start, int before,

                                  int count) {

            boolean Sign1 = possword_et_yuanmima.getText().length() > 0;

            boolean Sign2 = possword_et_mima.getText().length() > 0;

            boolean Sign3 = possword_et_qurenmima.getText().length() > 0;

            if (Sign1&Sign2&Sign3) {

                possword_bt_xiugai.setBackgroundResource(R.drawable.shape_button);

                possword_bt_xiugai.setEnabled(true);

            }


            else {

                possword_bt_xiugai.setBackgroundResource(R.drawable.shape_black);

                possword_bt_xiugai.setEnabled(false);

            }

        }

    }
}
