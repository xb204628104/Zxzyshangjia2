package com.zxtyshangjia.zxzyshangjia.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.activity.Aty_Xb_Collect;
import com.zxtyshangjia.zxzyshangjia.activity.Aty_Xb_Draw;
import com.zxtyshangjia.zxzyshangjia.activity.Aty_Xb_Pingjia;
import com.zxtyshangjia.zxzyshangjia.activity.Aty_Xb_Setting;


/**
 * Created by 18222 on 2017/8/7.
 */

public class ThreeMyfragment extends Fragment  {

    private LinearLayout main_ll_shezhi;
    private LinearLayout main_ll_erweima;
    private LinearLayout main_ll_shoucangjia;
    private LinearLayout main_ll_pinglun;
    private LinearLayout main_ll_choujiang;
    private FrameLayout main_fl_qiandao;
    private ImageView main_iv_touxiang;
    private TextView main_tv_gongsiming;
    private TextView main_tv_phonenum;
    private TextView main_tv_maisui;
    private TextView main_tv_zero;
    private TextView main_tv_cha;
    private TextView main_tv_dao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=View.inflate(getActivity(), R.layout.activity_main,null);
        //设置的按钮
        main_ll_shezhi = (LinearLayout) view.findViewById(R.id.main_ll_shezhi);
        //二维码的按钮
        main_ll_erweima = (LinearLayout) view.findViewById(R.id.main_ll_erweima);
        //收藏夹的按钮
        main_ll_shoucangjia = (LinearLayout) view.findViewById(R.id.main_ll_shoucangjia);
        //评价的按钮
        main_ll_pinglun = (LinearLayout) view.findViewById(R.id.main_ll_pinglun);
        //抽奖的按钮
        main_ll_choujiang = (LinearLayout) view.findViewById(R.id.main_ll_choujiang);
        //签到的按钮
        main_fl_qiandao = (FrameLayout) view.findViewById(R.id.main_fl_qiandao);
        //main_iv_touxiang 头像的按钮
        main_iv_touxiang = (ImageView) view.findViewById(R.id.main_iv_touxiang);
        //公司名称的按钮
        main_tv_gongsiming = (TextView) view.findViewById(R.id.main_tv_gongsiming);
        //电话号的按钮
        main_tv_phonenum = (TextView) view.findViewById(R.id.main_tv_phonenum);
        //麦穗的按钮
        main_tv_maisui = (TextView) view.findViewById(R.id.main_tv_maisui);
        //0的按钮
        main_tv_zero = (TextView) view.findViewById(R.id.main_tv_zero);
        //还差多少的按钮
        main_tv_cha = (TextView) view.findViewById(R.id.main_tv_cha);
        //到多少股的按钮
        main_tv_dao = (TextView) view.findViewById(R.id.main_tv_dao);

        main_ll_shezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),Aty_Xb_Setting.class));
            }
        });

        main_ll_erweima.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
              Dialog dialog=new AlertDialog.Builder(getContext())
                      .setView(R.layout.item_erweima)
                      .create();
                dialog.show();
            }
        });
        main_ll_shoucangjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Aty_Xb_Collect.class);
                startActivity(intent);
            }
        });
        main_ll_pinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Aty_Xb_Pingjia.class);
                startActivity(intent);
            }
        });
        main_ll_choujiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Aty_Xb_Draw.class);
                startActivity(intent);


            }
        });

        return view;
    }

}
