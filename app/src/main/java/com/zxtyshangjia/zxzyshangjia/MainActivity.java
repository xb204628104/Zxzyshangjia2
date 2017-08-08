package com.zxtyshangjia.zxzyshangjia;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.BaseKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zxtyshangjia.zxzyshangjia.adapter.MyAdapter;
import com.zxtyshangjia.zxzyshangjia.fragment.OneMyfragment;
import com.zxtyshangjia.zxzyshangjia.fragment.ThreeMyfragment;
import com.zxtyshangjia.zxzyshangjia.fragment.TwoMyfragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //用于存储Fragment的集合
    private List<Fragment> list;
    //控制台的控件,分享的控件,我的的控件;
    private LinearLayout ll_three_kongzhi,ll_three_shape,ll_three_my;
    private TextView tv_three_kongzhi,tv_three_shape,tv_three_my;
    private ImageView iv_three_kongzhi,iv_three_shape,iv_three_my;
    //viewpager的控件
    private ViewPager main_viewpager;
    private long fristTime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_three);
        initView();//初始化控件
        initClick();//点击事件
        iv_three_kongzhi.setImageResource(R.mipmap.kongzhitai_blue);
        tv_three_kongzhi.setTextColor(Color.parseColor("#228ee1"));
        list=new ArrayList<Fragment>();
        list.add(new OneMyfragment());
        list.add(new TwoMyfragment());
        list.add(new ThreeMyfragment());
        MyAdapter myAdapter=new MyAdapter(getSupportFragmentManager(),list);
        main_viewpager.setAdapter(myAdapter);
        main_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        iv_three_kongzhi.setImageResource(R.mipmap.kongzhitai_blue);
                        tv_three_kongzhi.setTextColor(Color.parseColor("#228ee1"));
                        iv_three_shape.setImageResource(R.mipmap.gongxiang_black);
                        iv_three_my.setImageResource(R.mipmap.wode_black);
                        tv_three_shape.setTextColor(Color.BLACK);
                        tv_three_my.setTextColor(Color.BLACK);
                        break;
                    case 1:
                        iv_three_shape.setImageResource(R.mipmap.gongxiang_bule);
                        tv_three_shape.setTextColor(Color.parseColor("#228ee1"));
                        iv_three_kongzhi.setImageResource(R.mipmap.kongzhitai_black);
                        tv_three_kongzhi.setTextColor(Color.BLACK);
                        iv_three_my.setImageResource(R.mipmap.wode_black);
                        tv_three_my.setTextColor(Color.BLACK);
                        break;
                    case 2:
                        iv_three_my.setImageResource(R.mipmap.wode_blue);
                        tv_three_my.setTextColor(Color.parseColor("#228ee1"));
                        iv_three_kongzhi.setImageResource(R.mipmap.kongzhitai_black);
                        iv_three_shape.setImageResource(R.mipmap.gongxiang_black);
                        tv_three_kongzhi.setTextColor(Color.BLACK);
                        tv_three_shape.setTextColor(Color.BLACK);
                        break;
                }

            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }
    //控件
    public void initView(){
        ll_three_kongzhi= (LinearLayout) findViewById(R.id.ll_three_kongzhi);
        ll_three_my=(LinearLayout) findViewById(R.id.ll_three_kongzhi);
        ll_three_shape=(LinearLayout) findViewById(R.id.ll_three_kongzhi);
        main_viewpager= (ViewPager) findViewById(R.id.main_viewpager);
        iv_three_kongzhi= (ImageView) findViewById(R.id.iv_three_kongzhi);
        iv_three_my= (ImageView) findViewById(R.id.iv_three_my);
        iv_three_shape= (ImageView) findViewById(R.id.iv_three_shape);
        tv_three_kongzhi= (TextView) findViewById(R.id.tv_three_kongzhi);
        tv_three_my= (TextView) findViewById(R.id.tv_three_my);
        tv_three_shape= (TextView) findViewById(R.id.tv_three_shape);
    }
    public void initClick(){
        ll_three_kongzhi.setOnClickListener(this);
        ll_three_shape.setOnClickListener(this);
        ll_three_my.setOnClickListener(this);
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime-fristTime>2000){
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    fristTime=secondTime;
                    return true;
                }else {
                    moveTaskToBack(true);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }



    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_three_kongzhi:
                main_viewpager.setCurrentItem(0);
                iv_three_kongzhi.setImageResource(R.mipmap.kongzhitai_blue);
                tv_three_kongzhi.setTextColor(Color.parseColor("#228ee1"));
                iv_three_shape.setImageResource(R.mipmap.gongxiang_black);
                iv_three_my.setImageResource(R.mipmap.wode_black);
                tv_three_shape.setTextColor(Color.BLACK);
                tv_three_my.setTextColor(Color.BLACK);
                //Toast.makeText(this, "0", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_three_shape:
                main_viewpager.setCurrentItem(1);
                iv_three_shape.setImageResource(R.mipmap.gongxiang_bule);
                tv_three_shape.setTextColor(Color.parseColor("#228ee1"));
                iv_three_kongzhi.setImageResource(R.mipmap.kongzhitai_black);
                tv_three_kongzhi.setTextColor(Color.BLACK);
                iv_three_my.setImageResource(R.mipmap.wode_black);
                tv_three_my.setTextColor(Color.BLACK);
                //Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_three_my:
                main_viewpager.setCurrentItem(2);
                iv_three_my.setImageResource(R.mipmap.wode_blue);
                tv_three_my.setTextColor(Color.parseColor("#228ee1"));
                iv_three_kongzhi.setImageResource(R.mipmap.kongzhitai_black);
                iv_three_shape.setImageResource(R.mipmap.gongxiang_black);
                tv_three_kongzhi.setTextColor(Color.BLACK);
                tv_three_shape.setTextColor(Color.BLACK);
                //Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                break;
        }
    }



}
