package com.zxtyshangjia.zxzyshangjia.bean;

/**
 * Created by 18222 on 2017/8/8.
 */

public class Collect {
    public String name;
    public String time;

    @Override
    public String toString() {
        return "Collect{" +
                "name='" + name + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
