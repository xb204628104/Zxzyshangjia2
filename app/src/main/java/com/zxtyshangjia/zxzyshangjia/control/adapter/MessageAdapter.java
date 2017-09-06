package com.zxtyshangjia.zxzyshangjia.control.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.control.bean.MessageData;

import java.util.List;

/**
 * Created by 18222 on 2017/9/6.
 */

public class MessageAdapter extends BaseAdapter {
    private Context ctx;
    private List<MessageData> list;
    private LayoutInflater inflater;

    public MessageAdapter(Context ctx,List<MessageData> list) {
        this.ctx = ctx;
        this.list = list;
        inflater = LayoutInflater.from(ctx);
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

        ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.message_list_item,null);
            holder = new ViewHolder();
            holder.mtime = (Button)convertView.findViewById(R.id.message_time_butten);
            holder.mTitle = (TextView) convertView.findViewById(R.id.message_title_tv);
            holder.mDate = (TextView) convertView.findViewById(R.id.message_date_tv);
            holder.mPrice = (TextView) convertView.findViewById(R.id.message_price_tv);
            holder.mTitle1 = (TextView) convertView.findViewById(R.id.message_above_title_tv);
            holder.mContent = (TextView) convertView.findViewById(R.id.message_content_tv);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        MessageData item = (MessageData) getItem(position);
        holder.mtime.setText(item.time);
        holder.mTitle.setText(item.title);
        holder.mDate.setText(item.year_time);
        holder.mPrice.setText(item.price);
        holder.mTitle1.setText(item.title);
        holder.mContent.setText(item.content);
        return convertView;
    }


    class ViewHolder{
        /**
         * 时间
         */
        private Button mtime;
        /**
         * 标题
         */
        private TextView mTitle;
        /**
         * 日期
         */
        private TextView mDate;
        /**
         * 价格
         */
        private TextView mPrice;
        /**
         * 底下的标题
         */
        private TextView mTitle1;
        /**
         * 内容
         */
        private TextView mContent;
    }

    //刷新数据
    public void refreshData(List<MessageData> list, boolean refreshOrLoad) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

}
