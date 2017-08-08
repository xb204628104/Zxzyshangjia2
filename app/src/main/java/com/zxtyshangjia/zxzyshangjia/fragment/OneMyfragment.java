package com.zxtyshangjia.zxzyshangjia.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.activity.Aty_Xb_Detail;

/**
 * Created by 18222 on 2017/8/7.
 */

public class OneMyfragment extends Fragment {
//明细的按钮
    private LinearLayout one_ll_mingxi;
    //提现的按钮
    private LinearLayout one_ll_tixian;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=View.inflate(getActivity(), R.layout.mian_one,null);
        one_ll_mingxi = (LinearLayout) view.findViewById(R.id.one_ll_mingxi);
        one_ll_tixian = (LinearLayout) view.findViewById(R.id.one_ll_tixian);
        one_ll_mingxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Aty_Xb_Detail.class));
            }
        });
        return view;
    }
}
