package com.zxtyshangjia.zxzyshangjia.commen;

/**
 * Created by LvJinrong on 2017/2/28.
 */

public class Api {
    /**
     * 基站
     */
    public static final String BASE_URL = "https://te.zxty.me/index.php/Api";
    /**
     * 商户登录
     */
    public static final String USER_Login = BASE_URL + "/Merchant/login";
    /**
     * 修改密码
     */
    public static final String MOFDIFY_Password =  BASE_URL + "/Merchant/exchangePassword";

    /**
     * 验证用户是否已经注册
     */
    public static final String IS_REGOSTER = BASE_URL + "/Login/isRegister";

    /**
     *  发送验证码
     */
    public static final String SENDVERIFY = BASE_URL + "/Login/sendVerify";

    /**
     * 获取商店种类
     */

    public static final String CLASSLIST = BASE_URL + "/Shop/classList";

    /**
     * 验证验证码
     */
    public static final String ISSURECODE = BASE_URL + "/Login/isSureCode";

    /**
     * 注册
     */

    public static final String SHOP_REGISTER = BASE_URL +"/Merchant/shopRegister";

    /**
     * 找回密码
     */

    public static final String GET_PASS = BASE_URL + "/Merchant/getPass";

    /**
     * 控制台  麦穗 众享豆 昨日收益
     */

    public static final String MERCHANT = BASE_URL + "/Merchant/index";

    /**
     * 控制台 收益曲线
     */
    public static final String MERCHANT_INCOME = BASE_URL + "/Merchant/income";

    /**
     * 商家是否有未读消息
     */
    public static final String ISREAD_MESSAGE = BASE_URL + "/Message/isReadShop";

    /**
     * 商品的列表
     */
    public static final String GOODS_LIST = BASE_URL + "/Goods/goodsList";

    /**
     * 商家详情
     */

    public static final String MERCHANT_DETAIL= BASE_URL + "/Merchant/detail";

    /**
     * 我的——曲线图
     */
    public static final String MEBER_INCOME_CHART = BASE_URL + "/Member/income";

    /**
     * 获取签到的广告图
     */
    public static final String SIDN_SHOWPIC= BASE_URL + "/AboutUs/showPic";

    /**
     * 商家签到
     */
    public static final String SHOP_SIGN = BASE_URL + "/Merchant/sign";

    /**
     * 收藏列表
     */
    public static final String COLLECT_LIST = BASE_URL + "/Merchant/collectList";

    /**
     *  商家评价列表
     */
    public static final String EVALUATION_LIST = BASE_URL + "/Member/myAppAppraise";

    /**
     * 消息列表
     */
    public static final String MESSAGE_LIST = BASE_URL + "/Message/messageList";

    /**
     *  绑定的支付宝或者微信的账号列表
     */
    public static final String BIND_LIST = BASE_URL + "/Merchant/bindList";

    /**
     * 绑定支付宝等
     */
    public static final String BINDPAY = BASE_URL + "/Merchant/bindPay";

    /**
     * 删除支付宝 或者微信
     */
    public static final String DELECT_BIND = BASE_URL +"/Merchant/delBind";

    /**
     * 提现
     */
    public static final String WITHDRAW = BASE_URL+ "/Merchant/withDraw";

    /**
     * 提现列表
     */
    public static final String WITHDRAW_LIST = BASE_URL + "/Pool/withList";


}
