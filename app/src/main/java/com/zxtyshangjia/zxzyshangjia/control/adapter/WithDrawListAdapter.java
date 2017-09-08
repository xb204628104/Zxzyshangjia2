package com.zxtyshangjia.zxzyshangjia.control.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.control.bean.WithDrawListData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 18222 on 2017/9/7.
 */

public class WithDrawListAdapter extends BaseAdapter implements View.OnClickListener {


    private Context ctx;
    private List<WithDrawListData> list;
    private LayoutInflater inflater;
    private String mAccount;
    private String mName;
    private MMCallback mCallback;

    @Override
    public void onClick(View v) {
        mCallback.click(v);
    }

    public interface MMCallback {
        public void click(View v);
    }

    public WithDrawListAdapter(Context ctx,List<WithDrawListData> list,MMCallback callback) {
        this.ctx = ctx;
        this.list = list;
        inflater = LayoutInflater.from(ctx);
        mCallback = callback;
    }

    @Override
    public int getCount() {
        return list!=null? list.size():0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.withdraw_list_item,null);
            holder = new ViewHolder();
            holder.mDate = (TextView) convertView.findViewById(R.id.recording_date);
            holder.mIsReject = (TextView) convertView.findViewById(R.id.is_turn_down);
            holder.mPrice = (TextView) convertView.findViewById(R.id.withdraw_price_tv);
            holder.mNameAndAccount = (TextView) convertView.findViewById(R.id.name_and_account_tv);
            holder.mDealWith = (TextView) convertView.findViewById(R.id.deal_with);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        WithDrawListData item = (WithDrawListData) getItem(position);
        /**
         * 日期
         */
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
        Date date2 = new Date(Long.parseLong(item.ctime) * 1000);
        String date = dateFormat.format(date2);
        holder.mDate.setText(date);
        //"status": "0" //1是成功 9是驳回
        if(item.status.equals("1")){
            holder.mIsReject.setText("已成功");
        }else if(item.status.equals("9")){
            holder.mIsReject.setText("已驳回");
            holder.mDealWith.setVisibility(View.VISIBLE);
        }else if(item.status.equals("0")){
            holder.mIsReject.setText("待付款");
            holder.mDealWith.setVisibility(View.GONE);
        }

        holder.mPrice.setText(item.total_price);
        holder.mNameAndAccount.setText(item.name+"-"+item.account);
        mAccount = item.account;
        mName = item.name;

        //设置点击事件
        holder.mDealWith.setOnClickListener(this);
        holder.mDealWith.setTag(position);
        return convertView;
    }

    class ViewHolder{
        /**
         * 日期
         */
        TextView mDate;
        /**
         * 是否驳回
         */
        TextView mIsReject;

        /**
         *  提现金额
         */
         TextView mPrice;
        /**
         * 支付宝名称和支付宝账号
         */
         TextView mNameAndAccount;
        /**
         * 处理
         */
        TextView mDealWith;

    }

    //刷新数据
    public void refreshData(List<WithDrawListData> list, boolean refreshOrLoad) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }



}
