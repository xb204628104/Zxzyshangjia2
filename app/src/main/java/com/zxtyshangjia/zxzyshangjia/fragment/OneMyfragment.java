package com.zxtyshangjia.zxzyshangjia.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zxtyshangjia.zxzyshangjia.R;

/**
 * Created by 18222 on 2017/8/7.
 */

public class OneMyfragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=View.inflate(getActivity(), R.layout.mian_one,null);
        return view;
    }
}
