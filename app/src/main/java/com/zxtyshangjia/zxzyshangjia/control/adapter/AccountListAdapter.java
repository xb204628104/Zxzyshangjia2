package com.zxtyshangjia.zxzyshangjia.control.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.commen.Api;
import com.zxtyshangjia.zxzyshangjia.commen.network.HttpUtil;
import com.zxtyshangjia.zxzyshangjia.commen.network.PostCallBack;
import com.zxtyshangjia.zxzyshangjia.commen.utils.SpUtils;
import com.zxtyshangjia.zxzyshangjia.commen.utils.ToastUtil;
import com.zxtyshangjia.zxzyshangjia.control.bean.BindListData;
import com.zxtyshangjia.zxzyshangjia.login.bean.BaseBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 18222 on 2017/9/7.
 */

public class AccountListAdapter extends BaseAdapter {

    private Context ctx;
    private List<BindListData> list;
    private LayoutInflater inflater;
    private String is_readonly;
    private int currentPosition;

    private PostCallBack deletaBindBack = new PostCallBack() {
        @Override
        public void onSuccess(Object data) {

            if(data != null && data instanceof BaseBean){
                BaseBean bean = (BaseBean) data;
                if(bean.flag.equals("success")){
                    ToastUtil.showToast("删除成功");
                    list.remove(currentPosition);//将集合中的数据删除
                    AccountListAdapter.this.notifyDataSetChanged();//更新适配器
                }
            }

        }

        @Override
        public void onError(int code, String msg) {

        }
    };


    public AccountListAdapter(Context ctx,List<BindListData> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        is_readonly = SpUtils.getInstance(ctx).getString("is_readonly","");
        ViewHolder holder ;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.my_account_list_item,null);
            holder = new ViewHolder();
            holder.mName = (TextView) convertView.findViewById(R.id.item_zfb_name_tv);
            holder.mAccount = (TextView) convertView.findViewById(R.id.item_zfb_account_tv);
            holder.mDeleteIv = (ImageView) convertView.findViewById(R.id.delete_account_iv);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final BindListData item = (BindListData) getItem(position);
        holder.mName.setText(item.name);
        holder.mAccount.setText(item.account);
        holder.mDeleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("提醒")
                        .setMessage("确定要删除该账号吗")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Map<String,String> map = new HashMap<>();
                                map.put("width_id",item.width_id);
                                map.put("is_readonly",is_readonly);
                                new HttpUtil(Api.DELECT_BIND,map, BaseBean.class,deletaBindBack).postExecute();
                                currentPosition = position;
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });


        return convertView;
    }




    class  ViewHolder{
        /**
         * 姓名
         */
        TextView mName;
        /**
         * 账号
         */
        TextView mAccount;
        /**
         * 删除
         */
        ImageView mDeleteIv;
    }


}
