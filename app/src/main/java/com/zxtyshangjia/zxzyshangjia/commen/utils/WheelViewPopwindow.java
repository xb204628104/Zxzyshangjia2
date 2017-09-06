package com.zxtyshangjia.zxzyshangjia.commen.utils;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;
import com.zxtyshangjia.zxzyshangjia.R;

import java.util.List;

/**
 * 单个数据滚动选择popwindow， 单例模式
 * Created by WangLu on 2017/3/4.
 */

public class WheelViewPopwindow {

    private PopupWindow popupWindow;
    private Activity activity;
    private List<String> data;
    private TextView textView;
    private String str = "";
    private OnSuccessClick click;
    private String titleStr = null;
    /**
     *
     * @param activity
     * @param title 标题
     * @param data
     * @param textView
     */
    public WheelViewPopwindow(Activity activity, String title, List<String> data, TextView textView) {
        this.activity = activity;
        this.data = data;
        this.textView = textView;
        this.titleStr = title;
    }

    /**
     *
     * @param activity
     * @param data
     * @param click  确定按钮点击事件
     */
    public WheelViewPopwindow(Activity activity, List<String> data, OnSuccessClick click) {
        this.activity = activity;
        this.data = data;
        this.click = click;
    }

    /**
     *
     * @param activity
     * @param data
     * @param textView
     * @param str 选择后选项后 ， 后面可以跟的String 比如 170cm   <-- str : cm
     */
    public WheelViewPopwindow(Activity activity, List<String> data, TextView textView, String str) {
        this.activity = activity;
        this.data = data;
        this.textView = textView;
        this.str = str;
    }
    /**
     *
     * @param activity
     * @param data
     * @param textView
     * @param click
     */
    public WheelViewPopwindow(Activity activity, List<String> data, TextView textView, OnSuccessClick click) {
        this.activity = activity;
        this.data = data;
        this.textView = textView;
        this.click = click;
    }
    public void showPop() {
        // 一个自定义的布局，作为显示的内容
        final View contentView = LayoutInflater.from(activity).inflate(
                R.layout.popwindow_wheelview_oneself, null);
        // 设置按钮的点击事件
        TextView cancel = (TextView) contentView.findViewById(R.id.popwindow_scrollchoose_cancel);
        TextView success = (TextView) contentView.findViewById(R.id.popwindow_scrollchoose_success);
        TextView title = (TextView) contentView.findViewById(R.id.popwindow_scrollchoose_title);
        if (!TextUtils.isEmpty(titleStr)) {
            title.setText(titleStr);
        } else {
            title.setText("");
        }
        final WheelView wheelView = (WheelView) contentView.findViewById(R.id.popwindow_scrollchoose_wheelview);
        wheelView.setWheelAdapter(new ArrayWheelAdapter(activity));
        wheelView.setSkin(WheelView.Skin.Holo);
        wheelView.setWheelData(data);
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextSize = 16;
        style.textSize = 14;
        wheelView.setStyle(style);

        popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x80000000));
        popupWindow.setAnimationStyle(R.style.AnimBottom);
        //虚拟按钮遮挡pop 所以要添加这条
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (!popupWindow.isShowing()) {
            //虚拟按钮遮挡pop 使用activity.getWindow().getDecorView()
            popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(data.get(wheelView.getCurrentPosition()))) {
                    if (click != null) {
                        click.onSuccessClick(wheelView.getCurrentPosition(),
                                data.get(wheelView.getCurrentPosition()));
                    }
                    if (textView != null) {
                        textView.setText(data.get(wheelView.getCurrentPosition()) + str);

                    }

                }
                popupWindow.dismiss();
            }
        });
    }

    /** 确定按钮点击回调 */
    public interface OnSuccessClick {
        /**
         *
         * @param position 点击的位置
         * @param content 该位置上的内容
         */
        void onSuccessClick(int position, String content);
    }
}
