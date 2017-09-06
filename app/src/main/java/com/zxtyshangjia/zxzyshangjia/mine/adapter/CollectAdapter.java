package com.zxtyshangjia.zxzyshangjia.mine.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.app.Myappalication;
import com.zxtyshangjia.zxzyshangjia.mine.bean.CollectData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 18222 on 2017/9/5.
 */

public class CollectAdapter extends BaseAdapter {

    private Context ctx;
    private List<CollectData> list;
    private LayoutInflater inflater;

    public CollectAdapter(Context ctx,List<CollectData> list) {
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
        ViewHolder holder =null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.collect_list_item,null);
            holder = new ViewHolder();
            holder.mName = (TextView) convertView.findViewById(R.id.name_collect_item_tv);
            holder.mHeadImg = (ImageView) convertView.findViewById(R.id.headpic_collect_item_iv);
            holder.mtime = (TextView) convertView.findViewById(R.id.time_collect_item_tv);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        CollectData item = (CollectData) getItem(position);
        if (!TextUtils.isEmpty(item.head_pic)){
            Myappalication.getGlideManager().inputImageCircle(item.head_pic, holder.mHeadImg);
        } else{
            holder.mHeadImg.setImageResource(R.mipmap.default_img);
        }
        holder.mName.setText(item.nick_name);
        /* 时间 因为时间是毫秒数 所以这里要转成yyyy-MM-dd格式的 */
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date2 = new Date();
        date2.setTime(Long.parseLong(item.ctime));
        dateFormat.format(date2);
        return convertView;
    }

    //刷新数据
    public void refreshData(List<CollectData> list, boolean refreshOrLoad) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder{
        /**
         * 头像
         */
        ImageView mHeadImg;
        /**
         * 名称
         */
        TextView mName;
        /**
         * 时间
         */
        TextView mtime;
    }
}
