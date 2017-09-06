package com.zxtyshangjia.zxzyshangjia.mall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.commen.Api;
import com.zxtyshangjia.zxzyshangjia.commen.utils.SpUtils;
import com.zxtyshangjia.zxzyshangjia.commen.utils.ToastUtil;
import com.zxtyshangjia.zxzyshangjia.commen.utils.refresh.BGANormalRefreshViewHolder;
import com.zxtyshangjia.zxzyshangjia.commen.utils.refresh.BGARefreshLayout;
import com.zxtyshangjia.zxzyshangjia.mall.adapter.CommodityAdapter;
import com.zxtyshangjia.zxzyshangjia.mall.bean.CommodityData;
import com.zxtyshangjia.zxzyshangjia.mall.bean.CommodityListBean;
import com.zxtyshangjia.zxzyshangjia.mine.activity.MyDrawActivity;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by LvJinrong on 2017/9/1.
 * 众享商城页
 */

public class TwoMyfragment extends Fragment {

    private View view;

    /**
     * 兑换订单
     */
    private TextView mExchangeOrder;

    /**
     * 下拉刷新 下拉加载布局
     */
    private BGARefreshLayout mShareMallRefresh;
    /**
     * 商品列表
     */

    private GridView mCommodityListGV;

    /**
     * 商品列表数组
     */
    private ArrayList<CommodityData> list = new ArrayList<>();

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
     * 列表适配器
     */
    private CommodityAdapter commodityAdapter;


    private Object object;

    private CommodityListBean bean;

    private String mCommodityUrl;

    private String shop_id;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.two_fragment, null);
        shop_id = SpUtils.getInstance(getActivity()).getString("shop_id","");
        initView();
        initData();
        initClick();
        mShareMallRefresh.beginRefreshing();
        return view;
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mShareMallRefresh.endRefreshing();
                    break;
                case 1:
                    mShareMallRefresh.endLoadingMore();
                    break;
            }
        }
    };

    //列表项点击事件
    private AdapterView.OnItemClickListener mItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String g_id = list.get(position).g_id;
            mCommodityUrl = "https://te.zxty.me/index.php/Merchant/Appgoods/goodsDetail?shop_id="+shop_id+"&g_id="+g_id;
            Intent intent = new Intent(getActivity(), MyDrawActivity.class);
            intent.putExtra("type",-1000);
            intent.putExtra("url",mCommodityUrl);
            startActivity(intent);
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

    private void initClick() {
        //初始化监听事件
        mShareMallRefresh.setDelegate(refreshLayoutDelegate);
        mCommodityListGV.setOnItemClickListener(mItemClick);
        mExchangeOrder.setOnClickListener(mClickListener);

    }

    private void initView() {
        //绑定控件
        mExchangeOrder = (TextView) view.findViewById(R.id.exchange_order_tv);
        mShareMallRefresh = (BGARefreshLayout) view.findViewById(R.id.mall_refresh);
        mCommodityListGV = (GridView) view.findViewById(R.id.commodity_list_gv);

    }


    /**
     * 加载数据
     */
    private void loadData(int startIndex) {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url(Api.GOODS_LIST + "?" + "p=" + startIndex + "&" + "type=1")
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    Log.e("++++++++++++=========", String.valueOf(response));
                    Class responseClass = CommodityListBean.class;
                    final String res = response.body().string();
                    Log.e("++++++++++++=========", res);
                    object = new Gson().fromJson(res, responseClass);
                } catch (Exception e) {
                    Log.e("++++++++++++=======", "error");
                }
                if (object != null && object instanceof CommodityListBean) {
                    bean = (CommodityListBean) object;
                    if (bean.flag.equals("success")) {
                        //获取成功
                        if (refreshOrLoad) {
                            //下拉刷新
                            if (bean.data.size() > 0) {
                                list.clear();
                                list.addAll(bean.data);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        commodityAdapter.refreshData(list, refreshOrLoad);
                                        Message msg = new Message();
                                        msg.what = 0;
                                        mHandler.sendMessage(msg);
                                    }
                                });
                            }

                        } else {
                            //上拉加载更多
                            if (bean.data == null) {
                                getActivity().runOnUiThread(new Runnable() {
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
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        commodityAdapter.refreshData(list, refreshOrLoad);
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

    /**
     * 初始化设置
     */
    private void initData() {
        refreshViewHolder = new BGANormalRefreshViewHolder(getContext(), true, true);
        mShareMallRefresh.setRefreshViewHolder(refreshViewHolder);
        commodityAdapter = new CommodityAdapter(getActivity(), new ArrayList<CommodityData>());
        mCommodityListGV.setAdapter(commodityAdapter);

    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == mExchangeOrder.getId()){
                //兑换订单
                String url = "https://te.zxty.me/index.php/Merchant/Apporder/orderlist?shop_id="+shop_id;
                Intent intent = new Intent(getActivity(),MyDrawActivity.class);
                intent.putExtra("type",1000);
                intent.putExtra("url",url);
                startActivity(intent);
            }

        }
    };

}