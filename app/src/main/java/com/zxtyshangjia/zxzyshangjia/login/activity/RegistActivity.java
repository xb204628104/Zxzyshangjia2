package com.zxtyshangjia.zxzyshangjia.login.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.app.Myappalication;
import com.zxtyshangjia.zxzyshangjia.commen.Api;
import com.zxtyshangjia.zxzyshangjia.commen.network.HttpUtil;
import com.zxtyshangjia.zxzyshangjia.commen.network.PostCallBack;
import com.zxtyshangjia.zxzyshangjia.commen.utils.ToastUtil;
import com.zxtyshangjia.zxzyshangjia.commen.utils.WheelViewPopwindow;
import com.zxtyshangjia.zxzyshangjia.login.bean.AreaData;
import com.zxtyshangjia.zxzyshangjia.login.bean.BaseBean;
import com.zxtyshangjia.zxzyshangjia.login.bean.GetAreaBean;
import com.zxtyshangjia.zxzyshangjia.login.bean.ShopSortBean;
import com.zxtyshangjia.zxzyshangjia.login.bean.ShopSortData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by LvJinrong on 2017/2/29.
 * 商家注册页
 */

public class RegistActivity  extends TakePhotoActivity  {

    /**
     *   省
     */
    private TextView mProvince;
    private String mProvinceID = "";

    /**
     *  市
     */
    private TextView mCity;
    private String mCityID = "";

    /**
     * 区
     */
    private TextView mArea;
    private String mAreaID = "";
    private String mAreaName;

    /**
     * 详细地址 点击事件
     */
    private LinearLayout mDetailAddressLL;

    /**
     *  详细地址 显示
     */
    private TextView mDetailAddressTV;
    /**
     * 上传图片
     */

    com.jph.takephoto.app.TakePhoto takePhoto;

    private View popview;

    private PopupWindow popwindow;

    /**
     * 获取验证码倒计时
     */

   Thread thread ;

    private Boolean tag = true;

    private int i = 60;

    /**
     * 手机号
     */
    private EditText mCallphoneNumET;
    private String mCallphoneNum;
    /**
     * 图形码
     */
    private EditText mGraphicCodeET;
    private String mGraphicCode;
    /**
     * 图形码 图片
     */
    private ImageView mGraphicCodeIV;
    /**
     * 验证码
     */
    private EditText mVerificationCodeET;
    private String mVerificationCode;
    /**
     * 获取验证码
     */
    private TextView mVerificationCodeTV;

    /**
     * 密码
     */
    private EditText mPasswordET;
    private String mPassword;
    /**
     * 确认密码
     */
    private EditText mConfirmPwordET;
    private String mConfirmPword;
    /**
     * 店名
     */
    private EditText mShopNameET;
    private String mShopName;
    /**
     * 分类
     */
    private TextView mShopSortTV;
    private String mShopSort;
    /**
     * 推荐人手机号
     */
    private EditText mRecommendNumET;
    private String mRecommendNum;

    /**
     * 资质 上传的照片
     */
    private ImageView picIV1;
    private ImageView picIV2;
    private ImageView picIV3;

    /**
     * 注册
     */
    private TextView mRegistSubmitTV;


    /**
     * 图形验证码链接
     */
    private String mUrl = "";

    /**
     * 获取图形码、获取验证码时传的参数 唯一值
     */
    int val = (int) (Math.random() * 100 + 1);
    private String mUserKey = System.currentTimeMillis() + "" + val;

    /**
     * 分类集合
     */
    private List<String> shopSortList = new ArrayList<>();

    private List<ShopSortData> datas = new ArrayList<>();

    /**
     * 记录当前有几张资质图
     */
    private int picNum;

    /**
     * 三张图片的存储路径
     */
    private String pic1Path;
    private String pic2Path;
    private String pic3Path;

    /**
     * 三张图片的文件
     */
    private File pic1ImageFile;
    private File pic2ImageFile;
    private File pic3ImageFile;

    /**
     * 分类id
     */
    private String class_id ;

    private int j;

    private Object object;

    private GetAreaBean bean;

    private ArrayList<String> mAreaList = new ArrayList<>();

    private List<AreaData> list = new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_activity);
        initView();
        initData();
        initClick();

    }

    //判断用户是否已经注册的回调
    private PostCallBack isRegisterBack = new PostCallBack() {
        @Override
        public void onSuccess(Object data) {
            if (data != null && data instanceof BaseBean) {
                BaseBean baseBean = (BaseBean) data;
                if (baseBean.flag.equals("error")) {
                    ToastUtil.showToast("该账号已被注册！");
                } else {
                    // 获取验证码
                    Map<String, String> map = new HashMap<>();
                    map.put("tel", mCallphoneNum);
                    map.put("type", "register");
                    map.put("port", "1");
                    map.put("verify_code", mGraphicCode);
                    map.put("key", mUserKey);
                    new HttpUtil(Api.SENDVERIFY, map, BaseBean.class, verifyBack).postExecute();
                }
            }

        }

        @Override
        public void onError(int code, String msg) {
            ToastUtil.showToast(msg);

        }
    };

    // 发送验证码回调
    private PostCallBack verifyBack = new PostCallBack() {
        @Override
        public void onSuccess(Object data) {
            if (data != null && data instanceof BaseBean) {
                BaseBean baseBean = (BaseBean) data;
                if (baseBean.flag.equals("success")) {
                    ToastUtil.showToast("信息已经发送，请注意查收");
                    changeBtnGetCode();
                } else {
                    ToastUtil.showToast(baseBean.message);
                }
            }
        }

        @Override
        public void onError(int code, String msg) {
            ToastUtil.showToast(msg);

        }
    };

    //获取商店种类的回调
    private PostCallBack shopSortBack = new PostCallBack() {
        @Override
        public void onSuccess(Object data) {
            if (data != null && data instanceof ShopSortBean) {
                ShopSortBean shopSortBean = (ShopSortBean) data;
                if (shopSortBean.flag.equals("success")) {
                    datas.addAll(shopSortBean.data);
                    for (int i = 0; i < datas.size(); i++) {
                        shopSortList.add(datas.get(i).name);
                    }
                    new WheelViewPopwindow(RegistActivity.this, "分类", shopSortList,mShopSortTV
                            ).showPop();

                }else {
                    ToastUtil.showToast(shopSortBean.message);
                }
            }

        }

        @Override
        public void onError(int code, String msg) {
            ToastUtil.showToast(msg);

        }

    };



    //验证验证码回调
    private PostCallBack isSureCodeBack = new PostCallBack() {
        @Override
        public void onSuccess(Object data) {
            if(data != null && data instanceof  BaseBean){
                BaseBean baseBean = (BaseBean) data;
                if(baseBean.flag.equals("success")){
                    //验证码正确 进行注册 操作
                    Map<String,String> map = new HashMap<>();
                    map.put("account",mCallphoneNum);
                    map.put("name",mShopName);
                    map.put("password",mPassword);
                    map.put("re_password",mConfirmPword);
                    map.put("recommend",mRecommendNum);
                    map.put("vc",mVerificationCode);
                    map.put("app_type","1");
                    map.put("class_id",class_id);

                    Map<String,File> fileMap = new HashMap<>();
                    fileMap.put("photo_a_" + System.currentTimeMillis() +".jpg",pic1ImageFile);
                    if( pic2ImageFile!= null){
                        fileMap.put("photo_a_" + System.currentTimeMillis() +".jpg",pic2ImageFile);
                    }
                    if(pic3ImageFile != null){
                        fileMap.put("photo_a_" + System.currentTimeMillis() +".jpg",pic3ImageFile);
                    }

                    new HttpUtil(Api.SHOP_REGISTER,map,"pic",fileMap,BaseBean.class,registerBack).postExecute();
                }else {
                    ToastUtil.showToast(baseBean.message);
                }
            }

        }

        @Override
        public void onError(int code, String msg) {
            ToastUtil.showToast(msg);

        }
    };

    //注册回调
    private PostCallBack registerBack = new PostCallBack() {
        @Override
        public void onSuccess(Object data) {
            if(data != null && data instanceof BaseBean){
                BaseBean baseBean = (BaseBean) data;
                if(baseBean.flag.equals("success")){
                    ToastUtil.showToast("注册成功");
                    Intent intent= new Intent(RegistActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    ToastUtil.showToast(baseBean.message);
                }
            }

        }

        @Override
        public void onError(int code, String msg) {
            ToastUtil.showToast(msg);

        }
    };

    //获取省级
    private void loadData(){
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url(Api.GET_PEOVINCE)
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    Log.e("++++++++++++=========", String.valueOf(response));
                    Class responseClass = GetAreaBean.class;
                    final String res = response.body().string();
                    Log.e("++++++++++++=========", res);
                    object = new Gson().fromJson(res, responseClass);
                } catch (Exception e) {
                    Log.e("++++++++++++=======", "error");
                }
                if(object != null && object instanceof GetAreaBean){
                    bean = (GetAreaBean) object;
                    if (bean.flag.equals("success")){
                        //获取成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                list.clear();
                                mAreaList.clear();
                                list.addAll(bean.data);
                                for (int i = 0; i < list.size(); i++) {
                                    mAreaList.add(list.get(i).area_name);
                                }

                                new WheelViewPopwindow(RegistActivity.this, mAreaList, mProvince, new WheelViewPopwindow.OnSuccessClick() {
                                    @Override
                                    public void onSuccessClick(int position, String content) {
                                        mProvinceID = list.get(position).area_id;
                                        mProvince.setText(list.get(position).area_name);
                                        mCity.setText("选择市");
                                        mArea.setText("选择区");
                                        mCityID = "";
                                        mAreaID = "";
                                    }
                                }).showPop();
                            }
                        });
                    }
                }
            }
        });
    }


    /**
     * 获取市级回调
     */
    private PostCallBack cityBack = new PostCallBack() {
        @Override
        public void onSuccess(Object data) {
            if(data != null && data instanceof GetAreaBean){
                bean = (GetAreaBean) data;
                if(bean.flag.equals("success")){
                    //获取成功
                    list.clear();
                    mAreaList.clear();
                    list.addAll(bean.data);
                    for (int i = 0; i < list.size(); i++) {
                        mAreaList.add(list.get(i).area_name);
                    }
                    new WheelViewPopwindow(RegistActivity.this, mAreaList, mCity, new WheelViewPopwindow.OnSuccessClick() {
                        @Override
                        public void onSuccessClick(int position, String content) {
                            mCityID = list.get(position).area_id;
                            mCity.setText(list.get(position).area_name);
                            mArea.setText("选择区");
                            mAreaID = "";
                        }
                    }).showPop();

                }
            }
        }

        @Override
        public void onError(int code, String msg) {

        }
    };

    //获取区级回调
    private PostCallBack areaBack = new PostCallBack() {
        @Override
        public void onSuccess(Object data) {
            if(data != null && data instanceof GetAreaBean){
                bean = (GetAreaBean) data;
                if (bean.flag.equals("success") && bean.data!=null){
                    //获取成功
                    list.clear();
                    mAreaList.clear();
                    list.addAll(bean.data);
                    for (int i = 0; i < list.size(); i++) {
                        mAreaList.add(list.get(i).area_name);
                    }
                    new WheelViewPopwindow(RegistActivity.this, mAreaList, mArea, new WheelViewPopwindow.OnSuccessClick() {
                        @Override
                        public void onSuccessClick(int position, String content) {
                            mAreaID = list.get(position).area_id;
                            mArea.setText(list.get(position).area_name);
                            mAreaName = list.get(position).area_name;
                        }
                    }).showPop();
                }
            }
        }

        @Override
        public void onError(int code, String msg) {

        }
    };

    //点击事件
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == mGraphicCodeIV.getId()) {
                //图形验证码点击事件 点击切换图形验证码
                mUserKey = System.currentTimeMillis() + "" + val;
                mUrl = "https://te.zxty.me/index.php/Api/Login/verify?key=" + mUserKey;
                Myappalication.getGlideManager().inputImage(mUrl, mGraphicCodeIV);
            } else if (v.getId() == mVerificationCodeTV.getId()) {
                //获取验证码点击事件  先验证用户是否已经存在
                mCallphoneNum = mCallphoneNumET.getText().toString();    //手机号
                mGraphicCode = mGraphicCodeET.getText().toString();      //图形码
                if(mCallphoneNum.equals("")){
                    ToastUtil.showToast("电话号码不能为空");
                }else if(mGraphicCode.equals("")){
                    ToastUtil.showToast("请填写图形码");
                }else if (!LoginActivity.isMobileNO(mCallphoneNum)) {
                    ToastUtil.showToast("请填写正确的手机号码");
                }else {
                    Map<String, String> map = new HashMap<>();
                    map.put("account", mCallphoneNum);
                    map.put("type", "1");
                    new HttpUtil(Api.IS_REGOSTER, map, BaseBean.class, isRegisterBack).postExecute();

                }

            } else if (v.getId() == mShopSortTV.getId()) {
                //商铺分类 点击事件 点击分类 底部弹出选项卡
                Map<String, String> map = new HashMap<>();
                new HttpUtil(Api.CLASSLIST, map, ShopSortBean.class, shopSortBack).postExecute();

            } else if(v.getId() == picIV1.getId()){
                //点击第一张图  记录当前位置  弹出底部选项框
                picNum = 1;
                showTakePhoto();

            } else if(v.getId() == picIV2.getId()){
                //点击第二张图  同上
                picNum = 2;
                showTakePhoto();

            } else if(v.getId() == picIV3.getId()){
                //点击第三张图
                picNum = 3;
                showTakePhoto();
            } else if (v.getId() == mRegistSubmitTV.getId()) {
                //注册 点击注册 数据检验 验证验证码是否准确 调用注册接口
                mCallphoneNum = mCallphoneNumET.getText().toString();   //手机号
                mShopName = mShopNameET.getText().toString();    //店名 商家的名称
                mPassword = mPasswordET.getText().toString();    //密码
                mConfirmPword = mConfirmPwordET.getText().toString(); //确认密码
                mRecommendNum = mRecommendNumET.getText().toString();  //推荐人的手机号
                mVerificationCode = mVerificationCodeET.getText().toString();  //验证码
                mShopSort = mShopSortTV.getText().toString();   //分类

                if(mCallphoneNum.equals("")){
                    ToastUtil.showToast("手机号不能为空");
                    return;
                }else if (!LoginActivity.isMobileNO(mCallphoneNum)) {
                    ToastUtil.showToast("请填写正确的手机号码");
                    return;
                }else if(mVerificationCode.equals("")){
                    ToastUtil.showToast("请填写验证码");
                    return;
                }else if(mPassword.equals("")){
                    ToastUtil.showToast("请填写密码");
                    return;
                }else if(mConfirmPword.equals("")){
                    ToastUtil.showToast("请填写确认密码");
                    return;
                }else if(!mPassword.equals(mConfirmPword)){
                    ToastUtil.showToast("您两次填写的密码不一致");
                    return;
                } else if(mShopName.equals("")){
                    ToastUtil.showToast("店名不能为空");
                    return;
                }else if(mShopSort.equals("")){
                    ToastUtil.showToast("请选择店铺分类");
                    return;
                } else if(mRecommendNum.equals("")){
                    ToastUtil.showToast("推荐人手机号不能为空");
                    return;
                }else if(pic1ImageFile == null && pic2ImageFile==null && pic3ImageFile == null){
                    ToastUtil.showToast("请上传相关资质");
                    return;
                }

                for(j = 0; j < datas.size(); j++){
                    if(mShopSort.equals(datas.get(j).name)){
                        break;
                    }
                }
                class_id = datas.get(j).class_id;

                //验证验证码
                Map<String,String> map = new HashMap<>();
                map.put("account",mCallphoneNum);
                map.put("vc",mVerificationCode);
                map.put("port","1");
                new HttpUtil(Api.ISSURECODE,map,BaseBean.class,isSureCodeBack).postExecute();

            } else if (v.getId() == mProvince.getId()) {
                //选择省
                loadData();
            }else if (v.getId() == mCity.getId()) {
                //选择市
                Map<String,String> map = new HashMap<>();
                map.put("province",mProvinceID);
                new HttpUtil(Api.GET_CITY,map,GetAreaBean.class,cityBack).postExecute();

            }else if (v.getId() == mArea.getId()) {
                //选择区
                Map<String,String> map = new HashMap<>();
                map.put("city",mCityID);
                new HttpUtil(Api.GET_AREA,map,GetAreaBean.class,areaBack).postExecute();

            }else if (v.getId() == mDetailAddressLL.getId()) {
                //跳转高德地图 选择详细地址
                mShopSort = mShopSortTV.getText().toString();   //分类
                if(mProvinceID.equals("") || mCityID.equals("") || mAreaID.equals("") || mShopSort.equals("")){
                    ToastUtil.showToast("请先选择省市区及分类");
                }else {

                    Intent intent = new Intent(RegistActivity.this,GaodeSearchForActivity.class);
                    intent.putExtra("city",mAreaName);
                    intent.putExtra("deepType",mShopSort);
                    startActivityForResult(intent,0);
                }
            }
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String change01 = data.getStringExtra("change01");
        String change02 = data.getStringExtra("change02");
        // 根据上面发送过去的请求吗来区别

    }





    private void initView() {
        mCallphoneNumET = (EditText) findViewById(R.id.callphone_num_et);
        mGraphicCodeET = (EditText) findViewById(R.id.graphic_code_et);
        mRecommendNumET = (EditText) findViewById(R.id.recommend_num_et);
        mPasswordET = (EditText) findViewById(R.id.password_et);
        mConfirmPwordET = (EditText) findViewById(R.id.confirm_pd_et);
        mGraphicCodeIV = (ImageView) findViewById(R.id.graphic_code_iv);
        mShopNameET = (EditText) findViewById(R.id.shop_name_et);
        mShopSortTV = (TextView) findViewById(R.id.sort_tv);
        mRecommendNumET = (EditText) findViewById(R.id.recommend_num_et);
        mVerificationCodeTV = (TextView) findViewById(R.id.verification_code_tv);
        mRegistSubmitTV = (TextView) findViewById(R.id.regist_submit_tv);
        picIV1 = (ImageView) findViewById(R.id.picture_iv1);
        picIV2 = (ImageView) findViewById(R.id.picture_iv2);
        picIV3 = (ImageView) findViewById(R.id.picture_iv3);
        mVerificationCodeET = (EditText) findViewById(R.id.verification_code_et);

        mProvince = (TextView) findViewById(R.id.address_province);
        mCity = (TextView) findViewById(R.id.address_city);
        mArea = (TextView) findViewById(R.id.address_area);
        mDetailAddressLL = (LinearLayout) findViewById(R.id.address_detail_ll);
        mDetailAddressTV = (TextView) findViewById(R.id.address_detail_tv);
    }

    private void initData() {
        //  页面初始化  验证码
        mUrl = "https://te.zxty.me/index.php/Api/Login/verify?key=" + mUserKey;
        Myappalication.getGlideManager().inputImage(mUrl, mGraphicCodeIV);
    }


    private void initClick() {
        //初始化点击事件
        mGraphicCodeIV.setOnClickListener(mClickListener);
        mVerificationCodeTV.setOnClickListener(mClickListener);
        mShopSortTV.setOnClickListener(mClickListener);
        mRegistSubmitTV.setOnClickListener(mClickListener);
        picIV1.setOnClickListener(mClickListener);
        picIV2.setOnClickListener(mClickListener);
        picIV3.setOnClickListener(mClickListener);
        mProvince.setOnClickListener(mClickListener);
        mCity.setOnClickListener(mClickListener);
        mArea.setOnClickListener(mClickListener);
        mDetailAddressLL.setOnClickListener(mClickListener);

    }



    //弹出底部选项框 处理拍照等
    private void showTakePhoto() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        popview = layoutInflater.inflate(R.layout.pop_picture_select, null);
        RelativeLayout btnBendi = (RelativeLayout) popview.findViewById(R.id.btn_bendi);
        RelativeLayout btnCancel = (RelativeLayout) popview.findViewById(R.id.btn_cancel);
        RelativeLayout btnCapture = (RelativeLayout) popview.findViewById(R.id.btn_paizhao);
        popwindow = new PopupWindow(popview, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        popwindow.setBackgroundDrawable(new ColorDrawable(0x80000000));
        popwindow.setAnimationStyle(R.style.AnimBottom);
        popwindow.setTouchable(true);
        popwindow.setOutsideTouchable(true);
        popwindow.setFocusable(true);
        //虚拟按钮遮挡pop 所以要添加这条
        popwindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (!popwindow.isShowing()) {
            //虚拟按钮遮挡pop 使用activity.getWindow().getDecorView()
            popwindow.showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        }
        //取消
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popwindow.isShowing()) {
                    popwindow.dismiss();
                }
            }
        });

        //本地相册
        btnBendi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTakePhoto();
                takePhoto.onPickMultiple(1);
                if (popwindow.isShowing()) {
                    popwindow.dismiss();
                }
            }
        });

        //拍照
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTakePhoto();
                File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
                if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                Uri imageUri = Uri.fromFile(file);
                takePhoto.onPickFromCapture(imageUri);
                if (popwindow.isShowing()) {
                    popwindow.dismiss();
                }
            }
        });

    }


    /**
     * 设置TakePhoto  照片的相关设置
     */
    private void setTakePhoto() {
        takePhoto = null;
        if (takePhoto == null) {
            takePhoto = getTakePhoto();
            TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
            builder.setCorrectImage(true);
            takePhoto.setTakePhotoOptions(builder.create());  //是否纠正照片旋转
            CompressConfig config = new CompressConfig.Builder()
                    .setMaxSize(700000) //压缩最大值
                    .setMaxPixel(800)
                    .enableReserveRaw(false) //压缩后是否保存原图
                    .create();
            takePhoto.onEnableCompress(config, false);//压缩图像
        }
    }


    //照片上传成功的回调
    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        ArrayList<TImage> images = result.getImages();
        if (picNum == 1) {
            //图一
            if (images.get(0).getCompressPath() == null) {
                pic1Path = images.get(0).getOriginalPath();
            } else {
                pic1Path = images.get(0).getCompressPath();
            }
            Glide.with(this)
                    .load(pic1Path)
                    .centerCrop()
                    .into(picIV1);
            picIV2.setVisibility(View.VISIBLE);
            pic1ImageFile = new File(pic1Path);
        } else if (picNum == 2) {
            //图二
            if (images.get(0).getCompressPath() == null) {
                pic2Path = images.get(0).getOriginalPath();
            } else {
                pic2Path = images.get(0).getCompressPath();
            }
            Glide.with(this)
                    .load(pic2Path)
                    .centerCrop()
                    .into(picIV2);
            picIV3.setVisibility(View.VISIBLE);
            pic2ImageFile = new File(pic2Path);
        } else if (picNum == 3) {
            //图三
            if (images.get(0).getCompressPath() == null) {
                pic3Path = images.get(0).getOriginalPath();
            } else {
                pic3Path = images.get(0).getCompressPath();
            }
            Glide.with(this)
                    .load(pic3Path)
                    .centerCrop()
                    .into(picIV3);
            pic3ImageFile = new File(pic3Path);
        }
    }

    //照片上传失败的回调
    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //操作取消 比如按下返回键 回调
    @Override
    public void takeCancel() {
        super.takeCancel();
        Toast.makeText(this, "操作取消", Toast.LENGTH_SHORT).show();
    }


    //获取验证码倒计时方法
    private void changeBtnGetCode() {
        thread = new Thread() {
            @Override
            public void run() {
                if (tag) {
                    while (i > 0) {
                        i--;
                        RegistActivity.this
                                .runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mVerificationCodeTV.setText("获取验证码（"
                                                + i + "）");
                                        mVerificationCodeTV.setClickable(false);
                                    }
                                });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    tag = false;
                }
                i = 60;
                tag = true;
                if (RegistActivity.this != null) {
                    RegistActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mVerificationCodeTV.setText("获取验证码");
                            mVerificationCodeTV.setClickable(true);
                        }
                    });
                }
            };
        };
        thread.start();
    }

}
