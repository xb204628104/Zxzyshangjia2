package com.zxtyshangjia.zxzyshangjia.control.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.app.Myappalication;
import com.zxtyshangjia.zxzyshangjia.control.bean.WithDrawListData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 18222 on 2017/9/7.
 */

public class WithDrawListAdapter extends BaseAdapter {


    private Context ctx;
    private List<WithDrawListData> list;
    private LayoutInflater inflater;
    private String mAccount;
    private String mName;

    public WithDrawListAdapter(Context ctx,List<WithDrawListData> list) {
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
        holder.mDealWith.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 弹出修改支付宝框
                popWindow();
            }
        });
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

    //弹出处理框
    private void popWindow(){
        View view = View.inflate(ctx,R.layout.zf_modify_windoew,null);
        ImageView close = (ImageView) view.findViewById(R.id.close_window_top);
        EditText account = (EditText) view.findViewById(R.id.zfb_account_window_et);
        EditText name = (EditText) view.findViewById(R.id.zfb_name_window_et);
        TextView makeSure = (TextView) view.findViewById(R.id.make_sure);
        account.setText(mAccount);
        name.setText(mName);
        WindowManager wm = (WindowManager) Myappalication.getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth(); //获取屏幕的宽度
        final PopupWindow pop = new PopupWindow(view, width / 4 * 3, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        makeSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
        pop.setTouchable(true);
        pop.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        backgroundAlpha(0.5f);
        pop.setBackgroundDrawable(new BitmapDrawable());
        //pop.showAsDropDown(v);
        //在屏幕的中央显示
//        pop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);

    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
//        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//        lp.alpha = bgAlpha; //0.0-1.0
//        //让窗口背景后面的任何东西变暗
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        getActivity().getWindow().setAttributes(lp);
    }

}
