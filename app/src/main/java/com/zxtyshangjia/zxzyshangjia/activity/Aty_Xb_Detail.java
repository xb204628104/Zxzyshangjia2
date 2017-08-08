package com.zxtyshangjia.zxzyshangjia.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.zxtyshangjia.zxzyshangjia.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 18222 on 2017/8/8.
 */

public class Aty_Xb_Detail extends Activity {
    int year = 0;
    int monthOfYear = 0;
    int dayOfMonth = 0;
    int minute = 0;
    int houre = 0;
    //日期选择
    private TimePickerView pvTime;
    //刷新的按钮
    private TextView detail_tv_shuaxin;
    //查看的按钮
    private TextView detail_tv_look;
    //listviwe的按钮
    private ListView detail_listview;
    private  String[] item=new String[]{"全部","转账","收益","消费","兑换","提现","退款","抽奖"};
    private DatePicker dpPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_detail);
       detail_tv_shuaxin= (TextView) findViewById(R.id.detail_tv_shuaxin);
        detail_tv_look= (TextView) findViewById(R.id.detail_tv_look);
        dpPicker = (DatePicker) findViewById(R.id.dpPicker);
        Calendar calendar= Calendar.getInstance();
        // 获得日历对象
        Calendar c = Calendar.getInstance();
        // 获取当前年份
        year = c.get(Calendar.YEAR);
        // 获取当前月份
        monthOfYear = c.get(Calendar.MONTH);
        // 获取当前月份的天数
        dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        // 获取当前的小时数
        houre = c.get(Calendar.HOUR_OF_DAY);
        // 获取当前的分钟数
        minute = c.get(Calendar.MINUTE);
        dpPicker.init(year, monthOfYear, dayOfMonth, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Aty_Xb_Detail.this.year = year;
                Aty_Xb_Detail.this.monthOfYear = monthOfYear;
                Aty_Xb_Detail.this.dayOfMonth = dayOfMonth;
                showDate(year,monthOfYear,dayOfMonth);
                dpPicker.setVisibility(View.GONE);
            }
        });

        detail_tv_shuaxin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(Aty_Xb_Detail.this, "执行了", Toast.LENGTH_SHORT).show();
                Dialog dialog=new AlertDialog.Builder(Aty_Xb_Detail.this)
                        .setSingleChoiceItems(item, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (item[which]){
                                    case "全部":
                                        Toast.makeText(Aty_Xb_Detail.this, "全部", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        break;
                                    case "转账":
                                        Toast.makeText(Aty_Xb_Detail.this, "转账", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        break;
                                    case "收益":
                                        Toast.makeText(Aty_Xb_Detail.this, "收益", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        break;
                                    case "消费":
                                        Toast.makeText(Aty_Xb_Detail.this, "消费", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        break;
                                    case "兑换":
                                        Toast.makeText(Aty_Xb_Detail.this, "兑换", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        break;
                                    case "提现":
                                        Toast.makeText(Aty_Xb_Detail.this, "提现", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        break;
                                    case "退款":
                                        Toast.makeText(Aty_Xb_Detail.this, "退款", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        break;
                                    case "抽奖":
                                        Toast.makeText(Aty_Xb_Detail.this, "22", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        break;
                                }

                            }
                        })
                        .create();
                dialog.show();
            }
        });

        detail_tv_look.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
               //initView();
                dpPicker.setVisibility(View.VISIBLE);

            }
        });
    }
   /* private void initView() {
        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        //设置标题
        pvTime.setTitle("选择日期");
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR));
        pvTime.setTime(new Date());
        //设置是否循环
        pvTime.setCyclic(false);
        //设置是否可以关闭
        pvTime.setCancelable(true);
        //设置选择监听
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                Log.e("MainActivity", "当前选择时间：" + getTime(date));
            }
        });
        //显示
        pvTime.show();
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }*/
   private void showDate(int year, int monthOfYear, int dayOfMonth) {
       Toast.makeText(this, "日期是：" + year + "年" + monthOfYear + "月" + dayOfMonth + "日", Toast.LENGTH_SHORT).show();

   }
    //显示时间的方法
    private void showTime(int houre2, int minute2) {
        Toast.makeText(this, "时间是：" + houre2 + "时" + minute2 + "分", Toast.LENGTH_SHORT).show();

    }

}
