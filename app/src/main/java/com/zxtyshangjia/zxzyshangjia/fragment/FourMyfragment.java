package com.zxtyshangjia.zxzyshangjia.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxtyshangjia.zxzyshangjia.R;


/**
 * Created by LvJinrong on 2017/9/1.
 */

public class FourMyfragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=View.inflate(getActivity(), R.layout.four_fragment,null);
        return view;
    }




}