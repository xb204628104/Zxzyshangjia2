package com.zxtyshangjia.zxzyshangjia.mine.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.app.Myappalication;
import com.zxtyshangjia.zxzyshangjia.mine.bean.EvalutionItem;

import java.util.List;

/**
 * Created by 18222 on 2017/9/6.
 */

public class EvalutationAdapter extends BaseAdapter {

    private Context ctx;
    private List<EvalutionItem> list;
    private LayoutInflater inflater;

    public EvalutationAdapter(Context ctx,List<EvalutionItem> list) {
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
            convertView = inflater.inflate(R.layout.evaluation_list_item,null);
            holder = new ViewHolder();
            holder.mHeadImg = (ImageView) convertView.findViewById(R.id.headimg_evaluation_item_iv);
            holder.mName = (TextView) convertView.findViewById(R.id.name_evaluation_item_tv);
            holder.mStar = (RatingBar) convertView.findViewById(R.id.star_evaluation_item_rtb);
            holder.mTime = (TextView) convertView.findViewById(R.id.time_evaluation_item_tv);
            holder.mContent = (TextView) convertView.findViewById(R.id.count_evaluation_item_tv);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        EvalutionItem item = (EvalutionItem) getItem(position);
        if (!TextUtils.isEmpty(item.head_pic)){
            Myappalication.getGlideManager().inputImageCircle(item.head_pic, holder.mHeadImg);
        } else{
            holder.mHeadImg.setImageResource(R.mipmap.default_img);
        }
        holder.mName.setText(item.mem_name);
        holder.mStar.setRating(Float.parseFloat(item.star));
        holder.mTime.setText(item.time);
        holder.mContent.setText(item.content);
        return convertView;
    }

    class ViewHolder{
        /**
         * 头像
         */
        private ImageView mHeadImg;

        /**
         * 用户名
         */
        private TextView mName;

        /**
         * 星级评价
         */
        private RatingBar mStar;

        /**
         * 日期时间
         */
        private TextView mTime;

        /**
         * 评价内容
         */
        private TextView mContent;

    }
    //刷新数据
    public void refreshData(List<EvalutionItem> list, boolean refreshOrLoad) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }
}
