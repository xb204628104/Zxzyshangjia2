package com.zxtyshangjia.zxzyshangjia.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.bean.Collect;

import java.util.List;

/**
 * Created by 18222 on 2017/8/8.
 */

public class CollectAdapter extends BaseAdapter {
    private Context context;
    private List<Collect> list;

    public CollectAdapter(List<Collect> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
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
        MyViewHordel myviewhordel=null;
        if (convertView==null){
            myviewhordel=new MyViewHordel();
            convertView=View.inflate(context, R.layout.item_collect,null);
            myviewhordel.itemcollect_mingzi= (TextView) convertView.findViewById(R.id.itemcollect_mingzi);
            myviewhordel.itemcollect_time= (TextView) convertView.findViewById(R.id.itemcollect_time);
            myviewhordel.itemcollect_iv_touxiang= (ImageView) convertView.findViewById(R.id.itemcollect_iv_touxiang);
            convertView.setTag(myviewhordel);
        }else {
            myviewhordel= (MyViewHordel) convertView.getTag();
            myviewhordel.itemcollect_time.setText(list.get(position).getTime());
            myviewhordel.itemcollect_mingzi.setText(list.get(position).getName());
        }
        return convertView;
    }
}
class MyViewHordel{
   public TextView itemcollect_mingzi,itemcollect_time;
    public ImageView itemcollect_iv_touxiang;
}