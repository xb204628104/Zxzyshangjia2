package com.zxtyshangjia.zxzyshangjia.commen.utils;

import android.widget.Toast;

import com.zxtyshangjia.zxzyshangjia.app.Myappalication;

/**
 * Toast工具、防止重复显示
 * Created by LvJinrong on 2017/2/28.
 */

public class ToastUtil {
    private static String oldMsg;
    protected static Toast toast   = null;
    private static long oneTime=0;
    private static long twoTime=0;

    public static void showToast(String s){
        if(toast==null){
            toast = Toast.makeText(Myappalication.getContext(), s, Toast.LENGTH_SHORT);
            toast.show();
            oneTime= System.currentTimeMillis();
        }else{
            twoTime= System.currentTimeMillis();
            if(s.equals(oldMsg)){
                if(twoTime-oneTime> Toast.LENGTH_SHORT){
                    toast.show();
                }
            }else{
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
        }
        oneTime=twoTime;
    }
}
