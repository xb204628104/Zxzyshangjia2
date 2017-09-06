package com.zxtyshangjia.zxzyshangjia.commen.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 存取键值对
 * Created by LvJinrong on 2017/8/30.
 */

public class SpUtils {
    private static SharedPreferences sp;
    private static SpUtils instance = new SpUtils();

    public SpUtils() {
    }

    public static SpUtils getInstance(Context context) {
        if (sp == null) {//懒汉式
            sp = context.getSharedPreferences("ms", Context.MODE_PRIVATE);
        }
        return instance;
    }

    /**
     * 把数据保存到SharedPerference中
     */
    public void save(String key, Object value) {
        //获取Editor对象
        SharedPreferences.Editor editor = sp.edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        }
        //提交保存数据
        editor.commit();
    }

    /**
     * 读取数据
     */
    public String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public int getInt(String key, Integer defValue) {
        return sp.getInt(key, defValue);
    }

    public boolean getBoolean(String key, Boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    public double getFloat(String key, Float defValue) {
        return sp.getFloat(key, defValue);
    }

    /**
     * 移除数据
     */
    public void remove(String key) {
        sp.edit().remove(key).commit();
    }
}
