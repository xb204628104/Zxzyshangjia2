package com.zxtyshangjia.zxzyshangjia.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.adapter.CollectAdapter;
import com.zxtyshangjia.zxzyshangjia.bean.Collect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 18222 on 2017/8/8.
 */

public class Aty_Xb_Collect extends Activity {
    private ListView listview;
    private List<Collect> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_collect);
        listview= (ListView) findViewById(R.id.collect_listview);
        list=new ArrayList<Collect>();
       Collect collect=new Collect();
        collect.setName("大魔王fker");
        //System.currentTimeMillis();
        collect.setTime("2017-8-8");
        list.add(collect);
        CollectAdapter collectAdapter=new CollectAdapter(list,this);
        listview.setAdapter(collectAdapter);


    }
}
