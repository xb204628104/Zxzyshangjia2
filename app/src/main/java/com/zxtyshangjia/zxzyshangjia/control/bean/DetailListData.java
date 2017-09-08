package com.zxtyshangjia.zxzyshangjia.control.bean;

/**
 * Created by 18222 on 2017/9/8.
 */

public class DetailListData {
    /**
     * "price": "0",  //钱数
     "ctime": "1494319269",
     "m_id": "1",  //用户的id
     "pay_type": "0",  //支付的方式 1:支付宝  2:微信  3:银行卡
     "monitor": "0",  //混合 0是加  1是减
     "shop_id": "0",//商家的id
     "year_time": "2017-05-09",  //交易时间月
     "time": "16:41", //交易时间
     "head_pic": "0" //用户的头像，这个不为0就是用户的头像
     "name": "",  //标题前面的名称
     "other_price": "0.00",//用户提现的手续费
     "type": "1",  //账单类型:1转账  2:收益 3：消费
     "is_appraise": 1 //等于1就是不能评价了
     */
    public String price;
    public String ctime;
    public String m_id;
    public String pay_type;
    public String monitor;
    public String shop_id;
    public String year_time;
    public String time;
    public String head_pic;
    public String name;
    public String other_price;
    public String type;
    public String is_appraise;



}
