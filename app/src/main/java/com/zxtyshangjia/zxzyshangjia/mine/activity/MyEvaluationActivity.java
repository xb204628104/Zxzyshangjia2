package com.zxtyshangjia.zxzyshangjia.mine.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.commen.Api;
import com.zxtyshangjia.zxzyshangjia.commen.utils.ListViewForScrollView;
import com.zxtyshangjia.zxzyshangjia.commen.utils.SpUtils;
import com.zxtyshangjia.zxzyshangjia.commen.utils.ToastUtil;
import com.zxtyshangjia.zxzyshangjia.commen.utils.refresh.BGANormalRefreshViewHolder;
import com.zxtyshangjia.zxzyshangjia.commen.utils.refresh.BGARefreshLayout;
import com.zxtyshangjia.zxzyshangjia.mine.adapter.EvalutationAdapter;
import com.zxtyshangjia.zxzyshangjia.mine.bean.EvaluationBean;
import com.zxtyshangjia.zxzyshangjia.mine.bean.EvalutionItem;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.zxtyshangjia.zxzyshangjia.app.Myappalication.getContext;

/**
 * 我的-评价页
 */

public class MyEvaluationActivity extends Activity {

    /**
     * 评价条数
     */
    private TextView mEvaluationNum;

    /**
     *  下拉刷新 上拉加载布局
     */
    private BGARefreshLayout mEvaluationRefresh;


    /**
     *  评价列表
     */
    private ListViewForScrollView mEvaluationList;

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
    private EvalutationAdapter evalutationAdapter;

    private String shop_id;

    private Object object;

    private EvaluationBean bean;

    private ArrayList<EvalutionItem> list = new ArrayList<>();

    private ScrollView mSV;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evaluation_activity);
        shop_id = SpUtils.getInstance(MyEvaluationActivity.this).getString("shop_id","");
        initView();
        initData();
        initClick();
        mEvaluationRefresh.beginRefreshing();
    }

    private void initClick() {
        //初始化监听事件
        mEvaluationRefresh.setDelegate(refreshLayoutDelegate);
    }

    private void initData() {

        refreshViewHolder = new BGANormalRefreshViewHolder(getContext(), true, true);
        mEvaluationRefresh.setRefreshViewHolder(refreshViewHolder);
        evalutationAdapter = new EvalutationAdapter(MyEvaluationActivity.this, new ArrayList<EvalutionItem>());
        mEvaluationList.setAdapter(evalutationAdapter);
        mSV.smoothScrollTo(0, 0);
    }

    private void initView() {
        mEvaluationNum= (TextView) findViewById(R.id.evaluation_num_tv);
        mEvaluationRefresh = (BGARefreshLayout) findViewById(R.id.evaluation_refresh);
        mEvaluationList = (ListViewForScrollView) findViewById(R.id.evaluation_list_lv);
        mSV = (ScrollView) findViewById(R.id.evaluation_sv);


    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mEvaluationRefresh.endRefreshing();
                    break;
                case 1:
                    mEvaluationRefresh.endLoadingMore();
                    break;
            }
        }
    };

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
                .url(Api.EVALUATION_LIST + "?" + "p=" + startIndex + "&" + "shop_id=" + 4+ "&" + "m_id=" + 1)
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
                    Class responseClass = EvaluationBean.class;
                    final String res = response.body().string();
                    Log.e("++++++++++++=========", res);
                    object = new Gson().fromJson(res, responseClass);
                } catch (Exception e) {
                    Log.e("++++++++++++=======", "error");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mEvaluationRefresh.endRefreshing();
                        }
                    });
                }
                if (object != null && object instanceof EvaluationBean) {
                    bean = (EvaluationBean) object;
                    if (bean.flag.equals("success")) {
                        //获取成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mEvaluationNum.setText(bean.data.count);
                            }
                        });

                        if (refreshOrLoad) {
                            //下拉刷新
                            if (bean.data.list.size() > 0) {
                                list.clear();
                                list.addAll(bean.data.list);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        evalutationAdapter.refreshData(list, refreshOrLoad);
                                        Message msg = new Message();
                                        msg.what = 0;
                                        mHandler.sendMessage(msg);
                                    }
                                });
                            }

                        } else {
                            //上拉加载更多
                            if (bean.data.list.size() == 0) {
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
                                list.addAll(bean.data.list);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        evalutationAdapter.refreshData(list, refreshOrLoad);
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
