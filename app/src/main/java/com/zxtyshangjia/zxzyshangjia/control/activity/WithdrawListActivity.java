package com.zxtyshangjia.zxzyshangjia.control.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.app.Myappalication;
import com.zxtyshangjia.zxzyshangjia.commen.Api;
import com.zxtyshangjia.zxzyshangjia.commen.network.HttpUtil;
import com.zxtyshangjia.zxzyshangjia.commen.network.PostCallBack;
import com.zxtyshangjia.zxzyshangjia.commen.utils.SpUtils;
import com.zxtyshangjia.zxzyshangjia.commen.utils.ToastUtil;
import com.zxtyshangjia.zxzyshangjia.commen.utils.refresh.BGANormalRefreshViewHolder;
import com.zxtyshangjia.zxzyshangjia.commen.utils.refresh.BGARefreshLayout;
import com.zxtyshangjia.zxzyshangjia.control.adapter.WithDrawListAdapter;
import com.zxtyshangjia.zxzyshangjia.control.bean.WithDrawListBean;
import com.zxtyshangjia.zxzyshangjia.control.bean.WithDrawListData;
import com.zxtyshangjia.zxzyshangjia.login.bean.BaseBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.zxtyshangjia.zxzyshangjia.app.Myappalication.getContext;

/**
 * Created by 18222 on 2017/9/7.
 * 提现记录页
 */

public class WithdrawListActivity extends Activity implements WithDrawListAdapter.MMCallback {

    /**
     * 下拉刷新 上拉加载 布局
     */
    private BGARefreshLayout mWithdrawRefresh;

    /**
     *  提现记录列表
     */
    private ListView mWithdrawLV;

    //数组
    private List<WithDrawListData> list = new ArrayList<>();

    /**
     * 判断是下拉刷新还是加载更多
     */
    private boolean refreshOrLoad = true;
    private int startIndex = 1;

    /**
     * 下拉刷新加载更多 头脚布局选择
     */
    private BGANormalRefreshViewHolder refreshViewHolder;

    /**
     *  适配器
     */
    private WithDrawListAdapter withDrawListAdapter;

    private Object object;

    private WithDrawListBean bean;

    private String shop_id;

    private String w_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.withdraw_list_activity);
        shop_id = SpUtils.getInstance(WithdrawListActivity.this).getString("shop_id","");
        initView();
        initData();
        initClick();
        mWithdrawRefresh.beginRefreshing();
    }

    /**
     * 修改提现信息回调
     */
    private PostCallBack editBack = new PostCallBack() {
        @Override
        public void onSuccess(Object data) {
            if(data != null && data instanceof BaseBean){
                BaseBean bean = (BaseBean) data;
                if(bean.flag.equals("success")){
                    ToastUtil.showToast("修改成功");
                    mWithdrawRefresh.beginRefreshing();
                }else {
                    ToastUtil.showToast(bean.message);
                }
            }
        }

        @Override
        public void onError(int code, String msg) {

        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mWithdrawRefresh.endRefreshing();
                    break;
                case 1:
                    mWithdrawRefresh.endLoadingMore();
                    break;
            }
        }
    };

    private void initClick() {
        //初始化监听事件
        mWithdrawRefresh.setDelegate(refreshLayoutDelegate);

    }

    private void initData() {
        refreshViewHolder = new BGANormalRefreshViewHolder(getContext(), true, true);
        mWithdrawRefresh.setRefreshViewHolder(refreshViewHolder);
        withDrawListAdapter = new WithDrawListAdapter(WithdrawListActivity.this, new ArrayList<WithDrawListData>(),this);
        mWithdrawLV.setAdapter(withDrawListAdapter);

    }

    private void initView() {
        mWithdrawRefresh = (BGARefreshLayout) findViewById(R.id.withdraw_refresh);
        mWithdrawLV = (ListView) findViewById(R.id.withdraw_listview);


    }

    /**
     * 下拉刷新、上拉加载监听器
     */
    private BGARefreshLayout.BGARefreshLayoutDelegate refreshLayoutDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {
        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            startIndex = 1;
            refreshOrLoad = true;
            loadData(startIndex);
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            refreshOrLoad = false;
            loadData(++startIndex);
            return true;  //此处一定要返回true，否则不会出现正在加载的窗口;
        }
    };

    private void loadData(int startIndex) {

        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
//                .url(Api.WITHDRAW_LIST + "?p=" + startIndex + "&shop_id=" + shop_id+"&type=2")
                .url(Api.WITHDRAW_LIST + "?p=" + startIndex + "&mix_id=1&type=2")
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
                    Class responseClass = WithDrawListBean.class;
                    final String res = response.body().string();
                    Log.e("++++++++++++=========", res);
                    object = new Gson().fromJson(res, responseClass);
                } catch (Exception e) {
                    Log.e("++++++++++++=======", "error");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mWithdrawRefresh.endRefreshing();
                        }
                    });
                }
                if (object != null && object instanceof WithDrawListBean) {
                    bean = (WithDrawListBean) object;
                    if (bean.flag.equals("success")) {
                        //获取成功
                        if (refreshOrLoad) {
                            //下拉刷新
                            if (bean.data.size() > 0) {
                                list.clear();
                                list.addAll(bean.data);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        withDrawListAdapter.refreshData(list, refreshOrLoad);
                                        Message msg = new Message();
                                        msg.what = 0;
                                        mHandler.sendMessage(msg);
                                    }
                                });
                            }

                        } else {
                            //上拉加载更多
                            if (bean.data == null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        refreshViewHolder.hideLoadingMoreImg();
                                        refreshViewHolder.updateLoadingMoreText("没有更多了");
                                        Message msg = new Message();
                                        msg.what = 1;
                                        mHandler.sendMessageDelayed(msg, 2000);
                                    }
                                });
                            } else {
                                list.addAll(bean.data);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        withDrawListAdapter.refreshData(list, refreshOrLoad);
                                        refreshViewHolder.showLoadingMoreImg();
                                        refreshViewHolder.updateLoadingMoreText("正在加载...");
                                        Message msg = new Message();
                                        msg.what = 1;
                                        mHandler.sendMessageDelayed(msg, 2000);
                                    }
                                });
                            }
                        }
                    } else {
                        ToastUtil.showToast(bean.message);
                        if (refreshOrLoad) {
                            Message msg = new Message();
                            msg.what = 0;
                            mHandler.sendMessage(msg);
                        } else {
                            Message msg = new Message();
                            msg.what = 1;
                            mHandler.sendMessageDelayed(msg, 2000);
                        }
                    }
                }

            }
        });

    }


    @Override
    public void click(View v) {
        popWindow(v);

    }

    //弹出处理框
    private void popWindow(View v){
        View view = View.inflate(this,R.layout.zf_modify_windoew,null);
        ImageView close = (ImageView) view.findViewById(R.id.close_window_top);
        final EditText account = (EditText) view.findViewById(R.id.zfb_account_window_et);
        final EditText name = (EditText) view.findViewById(R.id.zfb_name_window_et);
        TextView makeSure = (TextView) view.findViewById(R.id.make_sure);
        String mAccount = list.get((Integer)v.getTag()).account;
        String mName = list.get((Integer)v.getTag()).name;
        w_id = list.get((Integer)v.getTag()).w_id;
        account.setText(mAccount);
        name.setText(mName);
        WindowManager wm = (WindowManager) Myappalication.getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth(); //获取屏幕的宽度
        final PopupWindow pop = new PopupWindow(view, width / 4 * 3, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击关闭
                pop.dismiss();
                backgroundAlpha(1.0f);

            }
        });
        makeSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //点击确定修改
                String zh = account.getText().toString();
                String mc = name.getText().toString();
                if(zh.isEmpty() || mc.isEmpty()){
                    ToastUtil.showToast("账号或者姓名不能为空");
                }else {
                    //修改提现的信息
                    Map<String,String> map = new HashMap<>();
                    map.put("w_id",w_id);
                    map.put("account",zh);
                    map.put("name",mc);
                    new HttpUtil(Api.EDITWITHDRAW,map, BaseBean.class,editBack).postExecute();
                    pop.dismiss();
                    backgroundAlpha(1.0f);
                }
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
        pop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);

    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp =getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        //让窗口背景后面的任何东西变暗
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }
}
