package com.zxtyshangjia.zxzyshangjia.control.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.commen.Api;
import com.zxtyshangjia.zxzyshangjia.commen.network.HttpUtil;
import com.zxtyshangjia.zxzyshangjia.commen.network.PostCallBack;
import com.zxtyshangjia.zxzyshangjia.commen.utils.SpUtils;
import com.zxtyshangjia.zxzyshangjia.commen.utils.ToastUtil;
import com.zxtyshangjia.zxzyshangjia.control.bean.BindListBean;
import com.zxtyshangjia.zxzyshangjia.login.bean.BaseBean;
import com.zxtyshangjia.zxzyshangjia.mine.bean.ShopDetailBean;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 控制台-提现
 */

public class WithdrawActivity extends Activity {
    /**
     * 提现记录列表按钮 点击跳转提现记录列表页
     */
    private TextView mLithdrawalsList;

    /**
     *  点击事件绑定支付宝 or 查看账号
     */
    private LinearLayout mBindzfLL;

    /**
     *  请绑定支付宝账号  这里如果已经绑定账号则不显示
     */
    private TextView mPlessBindTV;

    /**
     *  显示账号和姓名 这里如果还未绑定则不显示
     */
    private LinearLayout mAccountDataLL;


    /**
     *  姓名
     */
    private TextView mName;

    /**
     *  支付宝账号
     */
    private TextView mZFBAccount;

    /**
     * 提现金额
     */
    private EditText mAmountOfMoneyET;

    /**
     *  可用余额
     */
    private TextView mbalanceTV;

    /**
     * 确认提现按钮
     */
    private  TextView mConfirm;

    private String shop_id;

    private Object object;

    private BindListBean bean;

    private int index;

    private String is_readonly;

    private String width_id = "";

    private String mbalance;


    @Override
    protected void onResume() {
        super.onResume();
        bindView();
        initData();
        initClick();
    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.withdrawals_acyivity);
        shop_id = SpUtils.getInstance(this).getString("shop_id","");
//        bindView();
//        initData();
//        initClick();
    }


    /**
     * 用户提现回调
     */

    private PostCallBack withdrawBack = new PostCallBack() {
        @Override
        public void onSuccess(Object data) {
            if(data != null && data instanceof  BaseBean){
                BaseBean bean = (BaseBean) data;
                if(bean.flag.equals("success")){
                    ToastUtil.showToast(bean.message);
                }
            }else {
                ToastUtil.showToast(bean.message);
            }

        }

        @Override
        public void onError(int code, String msg) {

        }
    };

    /**
     * 可用余额回调
     */
    private PostCallBack shopDetailBack = new PostCallBack() {
        @Override
        public void onSuccess(Object data) {
            if (data != null && data instanceof ShopDetailBean) {
                ShopDetailBean bean = (ShopDetailBean) data;
                if (bean.flag.equals("success")) {
                    //获取数据成功
                    mbalanceTV.setText("可用余额￥"+bean.data.wallet);
                    mbalance = bean.data.wallet;
                }
            }

        }

        @Override
        public void onError(int code, String msg) {

        }
    };

    //点击监听
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == mLithdrawalsList.getId() ){

                //跳转提现记录页
                startActivity(new Intent(WithdrawActivity.this,WithdrawListActivity.class));

            }else if(v.getId() == mBindzfLL.getId()){
                //添加账号 or 切换账号
                startActivity(new Intent(WithdrawActivity.this,BindAccountActivity.class));
            }else if(v.getId() == mConfirm.getId()){

                //提现

                String price = mAmountOfMoneyET.getText().toString();
                if(price.equals("")){
                    ToastUtil.showToast("请填写提现金额");

                }else if(Float.parseFloat(price) > Float.parseFloat(mbalance)){
                    ToastUtil.showToast("当前余额不足");

                }else if(width_id.isEmpty()){
                    ToastUtil.showToast("请先绑定支付宝");
                }else {
                    Map<String,String> map = new HashMap<>();
                    map.put("type","1");
                    map.put("mix_id",shop_id);
                    map.put("width_id",width_id);
                    map.put("is_readonly",is_readonly);
                    map.put("price",price);
                    new HttpUtil(Api.WITHDRAW,map, BaseBean.class,withdrawBack).postExecute();
                }

            }

        }
    };

    private void initClick() {
        mLithdrawalsList.setOnClickListener(mClickListener);
        mBindzfLL.setOnClickListener(mClickListener);
        mConfirm.setOnClickListener(mClickListener);

    }

    private void initData() {

        initAccount();   //初始化姓名账号
        initShopDat();  //初始化商家的一些资料
        index = SpUtils.getInstance(WithdrawActivity.this).getInt("index",0);
        is_readonly = SpUtils.getInstance(WithdrawActivity.this).getString("is_readonly","");



    }


    private void initShopDat() {
        Map<String, String> map = new HashMap<>();
        map.put("shop_id", shop_id);
        new HttpUtil(Api.MERCHANT_DETAIL, map, ShopDetailBean.class, shopDetailBack).postExecute();
    }

    //初始化支付宝账号和姓名 如果没有绑定支付宝则显示 绑定支付宝
    private void initAccount() {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url(Api.BIND_LIST + "?id_type=1&mix_id=" +shop_id+"&p=1")
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    Log.e("++++++++++++=========", String.valueOf(response));
                    Class responseClass = BindListBean.class;
                    final String res = response.body().string();
                    Log.e("++++++++++++=========", res);
                    object = new Gson().fromJson(res, responseClass);
                } catch (Exception e) {
                    Log.e("++++++++++++=======", "error");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPlessBindTV.setVisibility(View.VISIBLE);
                            mAccountDataLL.setVisibility(View.GONE);
                        }
                    });
                    object = null;
                }
                if(object != null && object instanceof BindListBean){
                    bean = (BindListBean) object;
                    if(bean.flag.equals("success")){
                        runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mPlessBindTV.setVisibility(View.GONE);
                                    mAccountDataLL.setVisibility(View.VISIBLE);
                                    mName.setText(bean.data.get(index).name);
                                    mZFBAccount.setText(bean.data.get(index).account);
                                    width_id = bean.data.get(index).width_id;

                                }
                            });
                        }
                }
            }
        });
    }

    private void bindView() {
        mLithdrawalsList = (TextView) findViewById(R.id.withdraw_list_tv);
        mBindzfLL = (LinearLayout) findViewById(R.id.pless_bind_zfb_ll);
        mPlessBindTV = (TextView) findViewById(R.id.pless_bind_zfb_tv);
        mAccountDataLL = (LinearLayout) findViewById(R.id.have_bind_ll);
        mName = (TextView) findViewById(R.id.zfb_name_tv);
        mZFBAccount = (TextView) findViewById(R.id.zfb_account_tv);
        mAmountOfMoneyET = (EditText) findViewById(R.id.amount_money_et);
        mbalanceTV = (TextView) findViewById(R.id.balance_tv);
        mConfirm = (TextView) findViewById(R.id.confirm_tv);


    }



}
