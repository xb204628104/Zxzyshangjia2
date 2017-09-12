package com.zxtyshangjia.zxzyshangjia.control.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.login.bean.PoiResultData;

import java.util.List;

/**
 * Created by 18222 on 2017/9/11.
 */

public class PoiSearchAdapter extends BaseAdapter {

    private Context ctx;
    private List<PoiResultData> list;
    private LayoutInflater inflater;

    public PoiSearchAdapter(Context ctx,List<PoiResultData> list) {
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
        ViewHolder holder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.poi_result_item,null);
            holder = new ViewHolder();
            holder.mTitle = (TextView) convertView.findViewById(R.id.address_main);
            holder.mContent = (TextView) convertView.findViewById(R.id.address_detail);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        PoiResultData item = (PoiResultData) getItem(position);
        holder.mTitle.setText(item.title);
        holder.mContent.setText(item.content);
        return convertView;
    }


    class ViewHolder{
        TextView mTitle;
        TextView mContent;
    }


}
