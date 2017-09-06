package com.zxtyshangjia.zxzyshangjia.mine.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.app.Myappalication;
import com.zxtyshangjia.zxzyshangjia.commen.Api;
import com.zxtyshangjia.zxzyshangjia.commen.network.HttpUtil;
import com.zxtyshangjia.zxzyshangjia.commen.network.PostCallBack;
import com.zxtyshangjia.zxzyshangjia.commen.utils.SpUtils;
import com.zxtyshangjia.zxzyshangjia.commen.utils.ToastUtil;
import com.zxtyshangjia.zxzyshangjia.control.bean.MerChantIncomeBean;
import com.zxtyshangjia.zxzyshangjia.login.bean.BaseBean;
import com.zxtyshangjia.zxzyshangjia.mine.activity.MyCollectActivity;
import com.zxtyshangjia.zxzyshangjia.mine.activity.MyDrawActivity;
import com.zxtyshangjia.zxzyshangjia.mine.activity.MyEvaluationActivity;
import com.zxtyshangjia.zxzyshangjia.mine.activity.MySetActivity;
import com.zxtyshangjia.zxzyshangjia.mine.bean.ShopDetailBean;
import com.zxtyshangjia.zxzyshangjia.mine.bean.SignShowPicBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by LvJinrong on 2017/9/3.
 */

public class ThreeMyfragment extends Fragment {

    private View view;
    /**
     * 头像
     */
    private ImageView mHeadPicIV;
    private String mImgUrl;

    /**
     * 公司名称
     */
    private TextView mCompanyNameTV;
    private String mCompanyName;

    /**
     * 手机号
     */
    private TextView mTelPhoneNumTV;
    private String mTelPhoneNum;

    /**
     * 签到 点击事件
     */
    private FrameLayout mCheckInFL;
    /**
     * 二维码 点击事件
     */
    private LinearLayout mQrCodeLL;

    /**
     * 设置 点击事件
     */
    private LinearLayout mSettingLL;

    /**
     * 收藏夹  点击事件
     */
    private LinearLayout mCollectLL;

    /**
     * 评论 点击事件
     */
    private LinearLayout mEvaluationLL;

    /**
     * 抽奖  点击事件
     */
    private LinearLayout mLotteryll;

    /**
     * 我的股数  多少股
     */
    private TextView mShareNumTV;

    /**
     * 麦穗个数
     */
    private TextView mWheatNumTV;

    /**
     * 缺少的麦穗个数
     */
    private TextView mWheatLackNumTV;

    /**
     * 到达几股
     */
    private TextView mShareToNumTV;

    /**
     * 商家id
     */

    private String shop_id;

    /**
     * 曲线图
     */
    private LineChart mChart;
    //x轴的数据
    ArrayList<String> xValues = new ArrayList<>();
    //y轴的数据
    ArrayList<Entry> yValues = new ArrayList<>();

    /**
     * 获取的签到广告图相关的信息
     */
    private String mPicUrl;    //图片的地址
    private String mAdvertisingUrl;  //广告链接
    private String mSignNum;  //签到次数
    private String mPrice;    //签到能领取的钱数
    private String isTrue;    //是否能签到 1是不能 0是能
    private String isSign;    //是否已经签到  1是已经签到

    private TextView receivebean;

    private String codeUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.three_fragment, null);
        shop_id = SpUtils.getInstance(getActivity()).getString("shop_id", "");
        bindView();
        initData();
        initClick();
        return view;
    }

    //商家详情回调
    private PostCallBack shopDetailBack = new PostCallBack() {
        @Override
        public void onSuccess(Object data) {
            if (data != null && data instanceof ShopDetailBean) {
                ShopDetailBean bean = (ShopDetailBean) data;
                if (bean.flag.equals("success")) {
                    //获取数据成功
                    //头像
                    if(!bean.data.head_pic.equals("0")){
                        Myappalication.getGlideManager().inputImageCircle(bean.data.img, mHeadPicIV);
                    }
                    mImgUrl = bean.data.img;
                    mCompanyName = bean.data.name;
                    mTelPhoneNum = bean.data.account;
                    mCompanyNameTV.setText(bean.data.name);
                    mTelPhoneNumTV.setText(bean.data.account);
                    mShareNumTV.setText(bean.data.piles);
                    mWheatNumTV.setText("麦穗"+bean.data.integral);
                    mWheatLackNumTV.setText(bean.data.remain);
                    int i = Integer.valueOf(bean.data.piles) + 1;
                    mShareToNumTV.setText(i + "");
                    codeUrl = bean.data.code;


                }
            }

        }

        @Override
        public void onError(int code, String msg) {

        }
    };

    // 曲线图监听回调
    private PostCallBack chartBack = new PostCallBack() {
        @Override
        public void onSuccess(Object data) {
            if(data!= null && data instanceof MerChantIncomeBean){
                MerChantIncomeBean bean = (MerChantIncomeBean) data;
                if (bean.flag.equals("success")){
                    //获取成功
                    XAxis xAxis = mChart.getXAxis();
                    //设置X轴的文字在底部
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    //从x轴进入的动画
                    mChart.animateX(2000);
                    String[] xdata = bean.data.x_date.split(",");
                    for (int i = 0; i < xdata.length; i++) {
                        xValues.add(xdata[i]);
                    }
                    String[] ydata = bean.data.day_line.split(",");
                    for (int i = 0; i < ydata.length; i++) {
                        yValues.add(new Entry(Float.parseFloat(ydata[i]), i));
                    }
                    LineDataSet dataSet = new LineDataSet(yValues, "股票曲线");
                    ArrayList<LineDataSet> dataSets = new ArrayList<>();
                    //将数据加入dataSets
                    dataSets.add(dataSet);
                    //构建一个LineData  将dataSets放入
                    LineData lineData = new LineData(xValues, dataSets);
                    //将数据插入
                    mChart.setData(lineData);
                }else {
                    ToastUtil.showToast(bean.message);
                }


            }

        }

        @Override
        public void onError(int code, String msg) {

        }
    };


    //获取签到的广告图等信息的回调
    private PostCallBack signDataBack = new PostCallBack() {
        @Override
        public void onSuccess(Object data) {
            if(data != null && data instanceof SignShowPicBean){
                SignShowPicBean bean = (SignShowPicBean) data;
                if(bean.flag.equals("success")){
                    //获取成功
                    mPicUrl = bean.data.pic;
                    mAdvertisingUrl = bean.data.url;
                    mSignNum = bean.data.number;
                    mPrice = bean.data.price;
                    isSign = bean.data.is_sign;
                    isTrue = bean.data.is_true;
                }else {
                    ToastUtil.showToast(bean.message);
                }
            }

        }

        @Override
        public void onError(int code, String msg) {

        }
    };


    //商家签到回调
    private PostCallBack signBack = new PostCallBack() {
        @Override
        public void onSuccess(Object data) {
            if (data != null && data instanceof BaseBean){
                BaseBean bean = (BaseBean) data;
                if(bean.flag.equals("success")){
                    ToastUtil.showToast("签到成功");
                    //设置背景颜色为灰色  不可签到状态
                    receivebean.setBackgroundResource(R.drawable.shape_button_bean_un);
                    initAdvertising();  //初始化签到的广告图等信息
                }else {
                    ToastUtil.showToast(bean.message);
                }
            }

        }

        @Override
        public void onError(int code, String msg) {

        }
    };


    //监听点击
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            if (v.getId() == mCheckInFL.getId()) {
                //签到
                popWindow();

            } else if (v.getId() == mQrCodeLL.getId()) {
                //二维码
                popWindowCode();

            } else if (v.getId() == mSettingLL.getId()) {
                //设置
                Intent intent = new Intent(getActivity(), MySetActivity.class);
                intent.putExtra("imgUrl",mImgUrl);   //封面
                intent.putExtra("mName",mCompanyName);  //商家名称
                intent.putExtra("mCallphone",mTelPhoneNum); //商家联系电话
                startActivity(intent);

            } else if (v.getId() == mCollectLL.getId()) {
                //收藏夹
                Intent intent = new Intent(getActivity(), MyCollectActivity.class);
                startActivity(intent);
            } else if (v.getId() == mEvaluationLL.getId()) {
                //评论
                Intent intent = new Intent(getActivity(), MyEvaluationActivity.class);
                startActivity(intent);
            } else if (v.getId() == mLotteryll.getId()) {
                //抽奖
                Intent intent = new Intent(getActivity(), MyDrawActivity.class);
                String mUrlDraw = "https://te.zxty.me/index.php/Merchant/Appluckdraw/luckdraw/shop_id/"+shop_id;
                intent.putExtra("url",mUrlDraw);
                intent.putExtra("type",1000);
                startActivity(intent);

            }

        }
    };


    //页面初始化
    private void initData() {
        initShopDat();  //初始化商家的一些资料
        initChart();    //初始化曲线图
        initAdvertising();  //初始化签到的广告图等信息
    }

    //初始化签到的广告图等信息
    private void initAdvertising() {
        Map<String,String> map = new HashMap<>();
        map.put("type","0");
        map.put("mix_id",shop_id);
        new HttpUtil(Api.SIDN_SHOWPIC,map, SignShowPicBean.class,signDataBack).postExecute();

    }

    //初始化曲线图
    private void initChart() {
        Map<String,String> map = new HashMap<>();
        new HttpUtil(Api.MEBER_INCOME_CHART,map, MerChantIncomeBean.class,chartBack).postExecute();

    }


    //初始化商家的一些资料 ：头像 昵称 账号 麦穗等
    private void initShopDat() {
        Map<String, String> map = new HashMap<>();
        map.put("shop_id", shop_id);
        new HttpUtil(Api.MERCHANT_DETAIL, map, ShopDetailBean.class, shopDetailBack).postExecute();


    }

    //点击事件
    private void initClick() {
        mCheckInFL.setOnClickListener(mClickListener);
        mQrCodeLL.setOnClickListener(mClickListener);
        mSettingLL.setOnClickListener(mClickListener);
        mCollectLL.setOnClickListener(mClickListener);
        mEvaluationLL.setOnClickListener(mClickListener);
        mLotteryll.setOnClickListener(mClickListener);


    }


    //绑定控件
    private void bindView() {

        //头像
        mHeadPicIV = (ImageView) view.findViewById(R.id.head_portrait_mine_iv);
        //公司名称
        mCompanyNameTV = (TextView) view.findViewById(R.id.company_name_mine_tv);
        //手机号
        mTelPhoneNumTV = (TextView) view.findViewById(R.id.phone_mine_tv);
        //签到按钮
        mCheckInFL = (FrameLayout) view.findViewById(R.id.check_in_mine_fl);
        //二维码按钮
        mQrCodeLL = (LinearLayout) view.findViewById(R.id.qr_code_mine_ll);
        //设置按钮
        mSettingLL = (LinearLayout) view.findViewById(R.id.setting_mine_ll);
        //收藏夹按钮
        mCollectLL = (LinearLayout) view.findViewById(R.id.collect_mine_ll);
        //评价按钮
        mEvaluationLL = (LinearLayout) view.findViewById(R.id.evaluation_mine_ll);
        //抽奖按钮
        mLotteryll = (LinearLayout) view.findViewById(R.id.lottery_mine_ll);
        //我的股数 多少股
        mShareNumTV = (TextView) view.findViewById(R.id.share_num_mine_tv);
        //麦穗个数
        mWheatNumTV = (TextView) view.findViewById(R.id.wheat_num_mine_tv);
        //还差多少麦穗
        mWheatLackNumTV = (TextView) view.findViewById(R.id.wheat_lack_mine_tv);
        //到多少股
        mShareToNumTV = (TextView) view.findViewById(R.id.share_to_mine_tv);
        //曲线图
        mChart = (LineChart) view.findViewById(R.id.chart_mine);

    }

    private void popWindowCode(){
        View view = View.inflate(getActivity(),R.layout.my_qr_code,null);
        ImageView mQrCode = (ImageView) view.findViewById(R.id.qr_code_iv);
        Myappalication.getGlideManager().inputImage("https://te.zxty.me/"+codeUrl,mQrCode);
        WindowManager wm = (WindowManager) Myappalication.getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth(); //获取屏幕的宽度
        final PopupWindow pop = new PopupWindow(view, width / 4 * 3, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
        pop.setTouchable(true);
        pop.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        backgroundAlpha(0.5f);
        pop.setBackgroundDrawable(new BitmapDrawable());
        //pop.showAsDropDown(v);
        //在屏幕的中央显示
        pop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);

    }

    private void popWindow() {
        View view = View.inflate(getActivity(), R.layout.sign_bean_window, null);
        ImageView adverPic = (ImageView) view.findViewById(R.id.advertising_iv);
        TextView signNum = (TextView) view.findViewById(R.id.sign_day_num_tv);
        TextView mBeanNum = (TextView) view.findViewById(R.id.today_bean_num_tv);
        receivebean = (TextView) view.findViewById(R.id.receive_bean_tv);
        ImageView close = (ImageView) view.findViewById(R.id.close_window);
        Myappalication.getGlideManager().inputImage("https://te.zxty.me"+mPicUrl,adverPic);
        signNum.setText(mSignNum+"天");
        mBeanNum.setText(mPrice);
        if(isSign.equals("1")){
            //设置背景颜色为灰色  不可签到状态
            receivebean.setBackgroundResource(R.drawable.shape_button_bean_un);
        }else {
            //设置背景颜色为蓝色 可签到状态
            receivebean.setBackgroundResource(R.drawable.shape_button_bean);

        }
        WindowManager wm = (WindowManager) Myappalication.getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth(); //获取屏幕的宽度
        final PopupWindow pop = new PopupWindow(view, width / 4 * 3, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //点击事件
        adverPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击跳转广告页
                Intent intent = new Intent(getActivity(),MyDrawActivity.class);
                intent.putExtra("url",mAdvertisingUrl);
                intent.putExtra("type",1000);
                startActivity(intent);
            }
        });
        receivebean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击领取众享豆 签到
                Map<String,String> map = new HashMap<>();
                map.put("shop_id",shop_id);
                new HttpUtil(Api.SHOP_SIGN,map, BaseBean.class,signBack).postExecute();

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击关闭
                pop.dismiss();
                backgroundAlpha(1.0f);
            }
        });

        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
        pop.setTouchable(true);
        pop.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        backgroundAlpha(0.5f);
        pop.setBackgroundDrawable(new BitmapDrawable());
        //pop.showAsDropDown(v);
        //在屏幕的中央显示
        pop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);

    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        //让窗口背景后面的任何东西变暗
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getActivity().getWindow().setAttributes(lp);
    }



}
