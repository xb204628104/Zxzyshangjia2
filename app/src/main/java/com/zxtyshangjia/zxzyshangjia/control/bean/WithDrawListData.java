package com.zxtyshangjia.zxzyshangjia.control.bean;

/**
 * Created by 18222 on 2017/9/7.
 */

public class WithDrawListData {

    /**
     *  {
     *    "w_id": "1", //提现的id
     "mix_id": "1", //提现的用户或者商家的id
     "ctime": "1498800968", //时间戳
     "name": "刘柱", //用户的名称
     "account": "18522713541", //账号
     "pay_type": "0", // 0是支付宝 1是微信
     "status": "0" //1是成功 9是驳回
     "total_price": "1.00"  //提现的金额
     },
     */

    public String w_id;
    public String mix_id;
    public String ctime;
    public String name;
    public String account;
    public String pay_type;
    public String status;
    public String total_price;



}
