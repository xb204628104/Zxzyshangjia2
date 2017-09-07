package com.zxtyshangjia.zxzyshangjia.control.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.commen.Api;
import com.zxtyshangjia.zxzyshangjia.commen.network.HttpUtil;
import com.zxtyshangjia.zxzyshangjia.commen.network.PostCallBack;
import com.zxtyshangjia.zxzyshangjia.commen.utils.SpUtils;
import com.zxtyshangjia.zxzyshangjia.commen.utils.ToastUtil;
import com.zxtyshangjia.zxzyshangjia.control.activity.DetailActivity;
import com.zxtyshangjia.zxzyshangjia.control.activity.MessagelistActivity;
import com.zxtyshangjia.zxzyshangjia.control.activity.WithdrawActivity;
import com.zxtyshangjia.zxzyshangjia.control.bean.AboutEarningBean;
import com.zxtyshangjia.zxzyshangjia.control.bean.MerChantIncomeBean;
import com.zxtyshangjia.zxzyshangjia.login.bean.BaseBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LvJinrong on 2017/8/31.
 */

public class OneMyfragment extends Fragment {

    private View view;

    /**
     * 明细 点击控件
     */
    private LinearLayout mBillDetailLL;
    /**
     * 提现点击控件
     */
    private LinearLayout mWithdrawLL;

    /**
     * 麦穗
     */
    private TextView mWheatNumTV;
    private String mWheatNum;

    /**
     * 众享豆
     */
    private TextView mBeanNumTV;
    private String mBeanNum;

    /**
     * 昨日收益
     */
    private TextView mEarningsTV;
    private String mEarning;

    /**
     * 消息列表控件
     */
    private FrameLayout mMessageListFL;

    /**
     * 商户id
     */
    private String shop_id;

    /**
     * 收益曲线  一下几个是和曲线相关的数据
     */
    private LineChart mChart;

    //x轴的数据
    ArrayList<String> xValues = new ArrayList<>();

    //y轴的数据

    ArrayList<Entry> yValues = new ArrayList<>();

    /**
     * 消息上那个点  有未读消息的时候显示  没有则不显示
     * 初始化时就要去看有没有未读消息
     */
    private Button mPoint;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.one_fragment, null);
        initView();
        initData();
        initClick();
        return view;
    }


    //获取众享豆 昨日收益 麦穗等数据的回调
    private PostCallBack aboutEarnBack = new PostCallBack() {
        @Override
        public void onSuccess(Object data) {
            if (data != null && data instanceof AboutEarningBean) {
                AboutEarningBean aboutEarningBean = (AboutEarningBean) data;
                if (aboutEarningBean.flag.equals("success")) {
                    //获取数据成功
                    /* 麦穗 */
                    mWheatNum = aboutEarningBean.data.integral;
                    mWheatNumTV.setText(mWheatNum);
                    /* 众享豆 */
                    mBeanNum = aboutEarningBean.data.wallet;
                    mBeanNumTV.setText(mBeanNum);
                    /*昨日收益 */
                    mEarning = aboutEarningBean.data.sum_price;
                    mEarningsTV.setText(mEarning);
                } else {
                    ToastUtil.showToast(aboutEarningBean.message);
                }
            }

        }

        @Override
        public void onError(int code, String msg) {
            ToastUtil.showToast(msg);

        }
    };

    /**
     * 收益曲线 回调
     */
    private PostCallBack mMerchantIncomeBack = new PostCallBack() {
        @Override
        public void onSuccess(Object data) {
            if (data != null && data instanceof MerChantIncomeBean) {
                MerChantIncomeBean merChantIncomeBean = (MerChantIncomeBean) data;
                if (merChantIncomeBean.flag.equals("success")) {
                    XAxis xAxis = mChart.getXAxis();
                    //设置X轴的文字在底部
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    //从x轴进入的动画
                    mChart.animateX(2000);
                    String[] xdata = merChantIncomeBean.data.x_date.split(",");
                    for (int i = 0; i < xdata.length; i++) {
                        xValues.add(xdata[i]);
                    }
                    String[] ydata = merChantIncomeBean.data.day_line.split(",");
                    for (int i = 0; i < ydata.length; i++) {
                        yValues.add(new Entry(Float.parseFloat(ydata[i]), i));
                    }
                    LineDataSet dataSet = new LineDataSet(yValues, "收益曲线");
                    ArrayList<LineDataSet> dataSets = new ArrayList<>();
                    //将数据加入dataSets
                    dataSets.add(dataSet);
                    //构建一个LineData  将dataSets放入
                    LineData lineData = new LineData(xValues, dataSets);
                    //将数据插入
                    mChart.setData(lineData);
                } else {
                    ToastUtil.showToast(merChantIncomeBean.message);
                }
            }

        }

        @Override
        public void onError(int code, String msg) {
            ToastUtil.showToast(msg);

        }
    };

    //是否有未读消息回调
    private PostCallBack isReadMessageBack = new PostCallBack() {
        @Override
        public void onSuccess(Object data) {
            if(data != null && data instanceof  BaseBean){
                BaseBean baseBean = (BaseBean) data;
                if(baseBean.flag.equals("success")){
                    //有未读消息
                    mPoint.setVisibility(View.VISIBLE);
                }else {
                    //没有未读消息
                    mPoint.setVisibility(View.GONE);

                }
            }

        }

        @Override
        public void onError(int code, String msg) {

        }
    };

    //点击事件
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == mBillDetailLL.getId()) {
                //明细  跳转明细页
                startActivity(new Intent(getActivity().getApplicationContext(), DetailActivity.class));

            } else if (v.getId() == mWithdrawLL.getId()) {
                //提现  跳转提现页
                startActivity(new Intent(getActivity().getApplicationContext(), WithdrawActivity.class));

            } else if (v.getId() == mMessageListFL.getId()) {
                //消息列表  跳转消息列表页
                startActivity(new Intent(getActivity().getApplicationContext(), MessagelistActivity.class));

            }

        }
    };

    private void initData() {
        //初始化页面
        initEarn();
        initChart();
        initMessage();
    }


    private void initClick() {
        //绑定点击事件
        mBillDetailLL.setOnClickListener(mClickListener);
        mWithdrawLL.setOnClickListener(mClickListener);
        mMessageListFL.setOnClickListener(mClickListener);

    }

    private void initView() {
        mWheatNumTV = (TextView) view.findViewById(R.id.wheat_value_tv);    //麦穗
        mBeanNumTV = (TextView) view.findViewById(R.id.bean_value_TV);   //众享豆
        mEarningsTV = (TextView) view.findViewById(R.id.earnings_tv);   //昨日收益
        mPoint = (Button) view.findViewById(R.id.message_point_bt);     // 未读消息
        mMessageListFL = (FrameLayout) view.findViewById(R.id.message_list_fl);  //消息列表 有点击事件
        mBillDetailLL = (LinearLayout) view.findViewById(R.id.bill_detail_ll);  //明细  有点击事件
        mWithdrawLL = (LinearLayout) view.findViewById(R.id.withdraw_ll);   //提现  有点击事件
        mChart = (LineChart) view.findViewById(R.id.chart_control);
    }


    //初始化 麦穗 众享豆 昨日收益
    private void initEarn() {
        shop_id = SpUtils.getInstance(getContext()).getString("shop_id", "");
        Map<String, String> map = new HashMap<>();
        map.put("shop_id", shop_id);
        new HttpUtil(Api.MERCHANT, map, AboutEarningBean.class, aboutEarnBack).postExecute();

    }

    //初始化线性图表
    private void initChart() {
        Map<String, String> map = new HashMap<>();
        map.put("shop_id", shop_id);
        new HttpUtil(Api.MERCHANT_INCOME, map, MerChantIncomeBean.class, mMerchantIncomeBack).postExecute();
    }

    //初始化未读消息
    private void initMessage() {
        Map<String, String> map = new HashMap<>();
        map.put("shop_id", shop_id);
        new HttpUtil(Api.ISREAD_MESSAGE,map, BaseBean.class,isReadMessageBack).postExecute();

    }

}
