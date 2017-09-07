package com.zxtyshangjia.zxzyshangjia.control.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.commen.Api;
import com.zxtyshangjia.zxzyshangjia.commen.utils.SpUtils;
import com.zxtyshangjia.zxzyshangjia.control.adapter.AccountListAdapter;
import com.zxtyshangjia.zxzyshangjia.control.bean.BindListBean;
import com.zxtyshangjia.zxzyshangjia.control.bean.BindListData;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 18222 on 2017/9/6.
 * 绑定支付宝 or 选择支付宝账号
 *
 */

public class BindAccountActivity extends Activity {

    /**
     * 添加账号
     */
    private TextView mAddAcountTV;
    /**
     * 已有账号列表
     */
    private ListView mAcountList;

    //数组
    private ArrayList<BindListData> list = new ArrayList<>();

    private String shop_id;

    private Object object;

    private BindListBean bean;

    /**
     *  适配器
     */
    private AccountListAdapter accountListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bind_account_activity);
        mAddAcountTV = (TextView) findViewById(R.id.add_account_tv);
        mAcountList = (ListView) findViewById(R.id.my_account_list);
        shop_id = SpUtils.getInstance(this).getString("shop_id","");
        //初始化账号列表
        initAccount();

        //添加账号点击事件
        mAddAcountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转添加支付宝账号
                startActivity(new Intent(BindAccountActivity.this,AddAccountActivity.class));
                finish();

            }
        });

        //账号列表点击事件
        mAcountList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击记录当钱点击账号的坐标  跳转提现页面 在提现页面显示该坐标下的账号信息
                SpUtils.getInstance(BindAccountActivity.this).save("index",position);
                startActivity(new Intent(BindAccountActivity.this,WithdrawActivity.class));
            }
        });


    }

    //初始化账号列表
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
            public void onResponse(Call call, Response response) throws IOException {
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
                            mAcountList.setVisibility(View.GONE);
                            SpUtils.getInstance(BindAccountActivity.this).save("index",0);

                        }
                    });


                }
                if(object != null && object instanceof BindListBean){
                    bean = (BindListBean) object;
                    if(bean.flag.equals("success")){
                        list.addAll(bean.data);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAcountList.setVisibility(View.VISIBLE);
                                accountListAdapter = new AccountListAdapter(BindAccountActivity.this,list);
                                mAcountList.setAdapter(accountListAdapter);

                            }
                        });

                    }


                }

            }
        });

    }

}
