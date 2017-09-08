package com.zxtyshangjia.zxzyshangjia.control.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.commen.Api;
import com.zxtyshangjia.zxzyshangjia.commen.utils.SpUtils;
import com.zxtyshangjia.zxzyshangjia.commen.utils.ToastUtil;
import com.zxtyshangjia.zxzyshangjia.commen.utils.refresh.BGANormalRefreshViewHolder;
import com.zxtyshangjia.zxzyshangjia.commen.utils.refresh.BGARefreshLayout;
import com.zxtyshangjia.zxzyshangjia.control.adapter.DetailListAdapter;
import com.zxtyshangjia.zxzyshangjia.control.bean.DetailListBean;
import com.zxtyshangjia.zxzyshangjia.control.bean.DetailListData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.zxtyshangjia.zxzyshangjia.app.Myappalication.getContext;

/**
 * 控制台-明细页
 */

public class DetailActivity extends Activity {
    /**
     * 当前年
     */
    int year = 0;
    /**
     * 当前月
     */
    int mMonth = 0;

    int dayOfMonth = 0;

    /**
     * 筛选
     */
    private TextView mFilterTV;
    /**
     * 查看其他月份
     */
    private TextView mOtherMonthTV;
    /**
     * 明细账单列表
     */
    private ListView mDetailListview;

    /**
     * 筛选条件
     */
    private  String[] item=new String[]{"全部","转账","收益","消费","兑换","提现","退款","抽奖"};

    /**
     * 日历
     */
    private DatePicker dpPicker;

    //以下是和后台交互的参数
    private String shop_id;
    //1转账 2:收益 3：消费 4：兑换 5：提现 6：退款 7：抽奖  0:全部
    private String type = "0";

    private String mYearAndMonth;

    /**
     *  下拉刷新 上拉加载布局
     */
    private BGARefreshLayout mDetailRefresh;


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
     * 适配器
     */
    private DetailListAdapter detailListAdapter;

    private Object object;

    private DetailListBean bean;

    private ArrayList<DetailListData> list = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_detail);
        shop_id = SpUtils.getInstance(DetailActivity.this).getString("shop_id","");
        initView();
        initData();
        initClick();
        mDetailRefresh.beginRefreshing();


    }

    /**
     * 点击侦听
     */
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == mOtherMonthTV.getId()) {

//                //日期选择器显示
                dpPicker.setVisibility(View.VISIBLE);

            }else if(v.getId() == mFilterTV.getId()){

                //弹出筛选框 记录type值
                Dialog dialog=new AlertDialog.Builder(DetailActivity.this)
                        .setSingleChoiceItems(item, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (item[which]){
                                    //1转账 2:收益 3：消费 4：兑换 5：提现 6：退款 7：抽奖
                                    case "全部":
                                        type ="0";
                                        mDetailRefresh.beginRefreshing();
                                        dialog.dismiss();
                                        break;
                                    case "转账":
                                        type ="1";
                                        mDetailRefresh.beginRefreshing();
                                        dialog.dismiss();
                                        break;
                                    case "收益":
                                        type ="2";
                                        dialog.dismiss();
                                        break;
                                    case "消费":
                                        type ="3";
                                        mDetailRefresh.beginRefreshing();
                                        dialog.dismiss();
                                        break;
                                    case "兑换":
                                        type ="4";
                                        mDetailRefresh.beginRefreshing();
                                        dialog.dismiss();
                                        break;
                                    case "提现":
                                        type ="5";
                                        mDetailRefresh.beginRefreshing();
                                        dialog.dismiss();
                                        break;
                                    case "退款":
                                        type ="6";
                                        mDetailRefresh.beginRefreshing();
                                        dialog.dismiss();
                                        break;
                                    case "抽奖":
                                        type ="7";
                                        mDetailRefresh.beginRefreshing();
                                        dialog.dismiss();
                                        break;
                                }

                            }
                        })
                        .create();
                        dialog.show();

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
                .url(Api.BILL_DETAIL + "?p=" + startIndex + "&shop_id=" + shop_id + "&type=" + type + "&month=" + mMonth)
//                .url(Api.BILL_DETAIL + "?p=" + startIndex + "&shop_id=" + shop_id + "&type=" + type + "&month=0")
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    Log.e("++++++++++++=========", String.valueOf(response));
                    Class responseClass = DetailListBean.class;
                    final String res = response.body().string();
                    Log.e("++++++++++++=========", res);
                    object = new Gson().fromJson(res, responseClass);

                } catch (Exception e) {
                    Log.e("++++++++++++=======", "error");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDetailRefresh.endRefreshing();
                            object = null;
                            list.clear();
                        }
                    });
                }

                if (object != null && object instanceof DetailListBean) {
                    bean = (DetailListBean) object;
                    if (bean.flag.equals("success")) {
                        //获取成功
                        if (refreshOrLoad) {
                            //下拉刷新
                            if (bean.data.equals("")){
                                list.clear();
                            }else if (bean.data.size() > 0) {
                                list.clear();
                                list.addAll(bean.data);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    detailListAdapter.refreshData(list, refreshOrLoad);
                                    Message msg = new Message();
                                    msg.what = 0;
                                    mHandler.sendMessage(msg);
                                }
                            });

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
                                        detailListAdapter.refreshData(list, refreshOrLoad);
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


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mDetailRefresh.endRefreshing();
                    break;
                case 1:
                    mDetailRefresh.endLoadingMore();
                    break;
            }
        }
    };
    /**
     * 初始化点击事件
     */
    private void initClick() {
        mOtherMonthTV.setOnClickListener(mClickListener);
        mFilterTV.setOnClickListener(mClickListener);
        //初始化监听事件
        mDetailRefresh.setDelegate(refreshLayoutDelegate);


    }

    private void initData() {

        //初始化 日期时间
        initDate();
        //初始化明列表
        initList();

    }


    /**
     * 明细列表初始化设置
     */
    private void initList() {
        refreshViewHolder = new BGANormalRefreshViewHolder(getContext(), true, true);
        mDetailRefresh.setRefreshViewHolder(refreshViewHolder);
        detailListAdapter = new DetailListAdapter(DetailActivity.this, new ArrayList<DetailListData>());
        mDetailListview.setAdapter(detailListAdapter);
    }

    /**
     * 初始化日期时间选择器
     */
    private void initDate() {
        Calendar calendar= Calendar.getInstance();
        // 获得日历对象
        Calendar c = Calendar.getInstance();

        year = c.get(Calendar.YEAR);
        dpPicker.init(year, mMonth, dayOfMonth, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                DetailActivity.this.year = year;
                DetailActivity.this.mMonth = monthOfYear;
                DetailActivity.this.dayOfMonth = dayOfMonth;
                dpPicker.setVisibility(View.GONE);
                mDetailRefresh.beginRefreshing();

            }
        });

    }

    private void initView() {
        mFilterTV= (TextView) findViewById(R.id.detail_tv_filter);
        mOtherMonthTV= (TextView) findViewById(R.id.detail_tv_month);
        dpPicker = (DatePicker) findViewById(R.id.dpPicker);
        mDetailListview = (ListView) findViewById(R.id.detail_listview);
        mDetailRefresh = (BGARefreshLayout) findViewById(R.id.detail_refresh);

    }


}

