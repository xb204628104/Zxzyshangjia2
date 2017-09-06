package com.zxtyshangjia.zxzyshangjia.control.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.commen.Api;
import com.zxtyshangjia.zxzyshangjia.commen.utils.SpUtils;
import com.zxtyshangjia.zxzyshangjia.commen.utils.ToastUtil;
import com.zxtyshangjia.zxzyshangjia.commen.utils.refresh.BGANormalRefreshViewHolder;
import com.zxtyshangjia.zxzyshangjia.commen.utils.refresh.BGARefreshLayout;
import com.zxtyshangjia.zxzyshangjia.control.adapter.MessageAdapter;
import com.zxtyshangjia.zxzyshangjia.control.bean.MessageData;
import com.zxtyshangjia.zxzyshangjia.control.bean.MessageListBean;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.zxtyshangjia.zxzyshangjia.app.Myappalication.getContext;

/**
 * 控制台-消息列表页
 */

public class MessagelistActivity extends Activity {

    /**
     * 下拉刷新 下拉加载布局
     *
     */
    private BGARefreshLayout mRefreshLayout;

    /**
     * 消息列表布局
     */

    private ListView mMessageListLV;

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
     * 数组
     */
    private ArrayList<MessageData> list = new ArrayList<>();

    /**
     *  适配器
     */
    private MessageAdapter messageAdapter;

    private String shop_id;

    private Object object;

    private MessageListBean bean;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_message);
        shop_id = SpUtils.getInstance(MessagelistActivity.this).getString("shop_id","");
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.message_refresh);
        mMessageListLV = (ListView) findViewById(R.id.message_listview);
        initData();
        initClick();
        mRefreshLayout.beginRefreshing();


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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mRefreshLayout.endRefreshing();
                    break;
                case 1:
                    mRefreshLayout.endLoadingMore();
                    break;
            }
        }
    };


    private void initClick() {
        //初始化监听事件
        mRefreshLayout.setDelegate(refreshLayoutDelegate);
    }

    private void initData() {
        refreshViewHolder = new BGANormalRefreshViewHolder(getContext(), true, true);
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
        messageAdapter = new MessageAdapter(MessagelistActivity.this, new ArrayList<MessageData>());
        mMessageListLV.setAdapter(messageAdapter);

    }

    private void loadData(int startIndex) {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url(Api.MESSAGE_LIST + "?p=" + startIndex + "&m_id=" + shop_id+"&type=1")
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
                    Class responseClass = MessageListBean.class;
                    final String res = response.body().string();
                    Log.e("++++++++++++=========", res);
                    object = new Gson().fromJson(res, responseClass);
                } catch (Exception e) {
                    Log.e("++++++++++++=======", "error");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRefreshLayout.endRefreshing();
                        }
                    });
                }
                if (object != null && object instanceof MessageListBean) {
                    bean = (MessageListBean) object;
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
                                        messageAdapter.refreshData(list, refreshOrLoad);
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
                                        messageAdapter.refreshData(list, refreshOrLoad);
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

}
