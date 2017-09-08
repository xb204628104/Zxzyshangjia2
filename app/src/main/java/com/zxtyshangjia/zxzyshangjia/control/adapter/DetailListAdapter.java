package com.zxtyshangjia.zxzyshangjia.control.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.app.Myappalication;
import com.zxtyshangjia.zxzyshangjia.control.bean.DetailListData;

import java.util.List;

import static java.lang.Float.parseFloat;

/**
 * Created by 18222 on 2017/9/8.
 */

public class DetailListAdapter extends BaseAdapter {

    private Context ctx;
    private List<DetailListData> list;
    private LayoutInflater inflater;
    private String type;

    public DetailListAdapter(Context ctx,List<DetailListData> list) {
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
            convertView = inflater.inflate(R.layout.mingxi_detail_list_item,null);
            holder = new ViewHolder();
            holder.mDate = (TextView) convertView.findViewById(R.id.detail_date_tv);
            holder.mTime = (TextView) convertView.findViewById(R.id.detail_time_tv);
            holder.mImg = (ImageView) convertView.findViewById(R.id.my_head_img_iv);
            holder.mIncome = (TextView) convertView.findViewById(R.id.type_and_price_tv);
            holder.mNameAndType = (TextView) convertView.findViewById(R.id.name_and_type_tv);
            holder.mTotalIncome = (TextView) convertView.findViewById(R.id.total_income_tv);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        DetailListData item = (DetailListData) getItem(position);
        holder.mDate.setText(item.year_time);
        holder.mTime.setText(item.time);
        if(item.head_pic.equals("0")){
            holder.mImg.setImageResource(R.mipmap.default_img);
        }else {
            Myappalication.getGlideManager().inputImageCircle("https://te.zxty.me/"+item.head_pic, holder.mImg);
        }
        if(item.type.equals("2") && !item.price.equals("0")){
            holder.mIncome.setText("即时收益" + item.price);
        }else {
            holder.mIncome.setText(item.price);
        }

        //1转账 2:收益 3：消费 4：兑换 5：提现 6：退款 7：抽奖
        if(item.type.equals("1")){
            type = "转账";
        } else if(item.type.equals("2")){
            type = "收益";
        }else if(item.type.equals("3")){
            type = "消费";
        }else if(item.type.equals("4")){
            type = "兑换";
        }else if(item.type.equals("5")){
            type = "提现";
        }else if(item.type.equals("6")){
            type = "退款";
        }else if(item.type.equals("7")){
            type = "抽奖";
        }
        holder.mNameAndType.setText(item.name + "-" + type);
        float m = Float.parseFloat(item.price) + parseFloat(item.other_price);
        holder.mTotalIncome.setText(m+"");
        return convertView;
    }


    class ViewHolder{
        /**
         * 日期
         */
        private TextView mDate;
        /**
         * 时间
         */
        private TextView mTime;
        /**
         * 头像
         */
        private ImageView mImg;
        /**
         * 收益
         */
        private TextView mIncome;
        /**
         * 姓名和明细种类
         */
        private TextView mNameAndType;
        /**
         * 总收益
         */
        private TextView mTotalIncome;

    }

    //刷新数据
    public void refreshData(List<DetailListData> list, boolean refreshOrLoad) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }


}
