package com.zxtyshangjia.zxzyshangjia.mall.adapter;

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
import com.zxtyshangjia.zxzyshangjia.mall.bean.CommodityData;

import java.util.List;

/**
 * Created by 18222 on 2017/9/1.
 */

public class CommodityAdapter extends BaseAdapter {

    private Context ctx;
    private List<CommodityData> list;
    private LayoutInflater inflater;

    public CommodityAdapter(Context ctx,List<CommodityData> list) {
        this.ctx = ctx;
        this.list = list;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
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
            convertView = inflater.inflate(R.layout.commodity_list_item,null);
            holder = new ViewHolder();
            holder.commodityPic = (ImageView) convertView.findViewById(R.id.commodity_list_item_pic);
            holder.commodityName = (TextView) convertView.findViewById(R.id.commodity_list_item_name);
            holder.wheatNum = (TextView) convertView.findViewById(R.id.wheat_num);
            holder.commodityBuyNum = (TextView) convertView.findViewById(R.id.month_buy_num);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        CommodityData item = (CommodityData) getItem(position);
        if (!TextUtils.isEmpty(item.cover_pic)){
            Myappalication.getGlideManager().inputImageNoCrop("https://te.zxty.me/"+item.cover_pic, holder.commodityPic);
        } else{
            holder.commodityPic.setImageResource(R.mipmap.default_img);
        }
        holder.commodityName.setText(item.name);
        holder.wheatNum.setText(item.price);
        holder.commodityBuyNum.setText("月销"+item.sales);
        return convertView;
    }

    class ViewHolder{

        /**
         *  商品图片
         */
        private ImageView commodityPic;
        /**
         * 商品名称
         */
        private TextView commodityName;
        /**
         * 麦穗数量
         */
        private TextView wheatNum;
        /**
         * 商品月销量
         */
        private TextView commodityBuyNum;


    }


    //刷新数据
    public void refreshData(List<CommodityData> list, boolean refreshOrLoad) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

}
