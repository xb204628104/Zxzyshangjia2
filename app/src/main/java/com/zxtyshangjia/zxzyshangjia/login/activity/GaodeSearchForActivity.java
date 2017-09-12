package com.zxtyshangjia.zxzyshangjia.login.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.commen.utils.ToastUtil;
import com.zxtyshangjia.zxzyshangjia.control.adapter.PoiSearchAdapter;
import com.zxtyshangjia.zxzyshangjia.login.bean.PoiResultData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 18222 on 2017/9/10.
 */

public class GaodeSearchForActivity extends Activity implements PoiSearch.OnPoiSearchListener,GeocodeSearch.OnGeocodeSearchListener {

    /**
     *返回
     */
    private LinearLayout mReturnLL;
    /**
     * 填写搜索条件
     */
    private EditText mSearchTextET;

    /**
     * 搜索
     */
    private TextView mSearchForTv;

    /**
     *  地图和上面的地址 所在的布局  未点击具体搜索结果的时候不显示 点击了具体结果时显示 然后列表不显示
     */
    private FrameLayout mMapAndResultFL;

    /**
     * 结果列表
     */
    private ListView mResultLV;

    /**
     * 地图
     */

    private MapView mMapView;

    /**
     * 地图上的点标识的地址
     */
    private TextView mMapAddressTV;
    private String mMapAddress;

    /**
     * 手动填写的详细地址
     */
    private EditText mDetailAddress;
    private String mDetailAdd;

    /**
     * 确定按钮
     */
    private TextView mMakeSure;

    /**
     *  关键字 city
     */
    private String city;

    /**
     *  POI搜索相关
     */
    private String deepType = "";         // poi搜索类型
    private PoiSearch.Query query;        // Poi查询条件类

    private PoiSearch poiSearch;

    private PoiResult poiResult ; // poi返回的结果

    private List<PoiItem> poiItems = new ArrayList<>();// poi数据

    private List<PoiItem> poiItems1 = new ArrayList<>();


    private double latitude;

    private double longititude;

    /**
     * 适配器
     */
    private PoiSearchAdapter adapter;

    /**
     *  传给适配器的数组
     */
    private ArrayList<PoiResultData> resultList = new ArrayList<>();

    private String adcode;

    AMap aMap = null;

    Marker marker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poi_gaode);
        initView();
        initData();
        initClick();
        mMapView.onCreate(savedInstanceState);


    }


    //poi搜索请求结果回调
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        Log.e("rCode =====",rCode+"");
        //解析result获取POI信息
        if(rCode == 1000){
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    resultList.clear();
                    poiResult = result;
                    poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys();
                    Log.e("***********",poiItems.size()+"");
                    if (poiItems != null && poiItems.size() > 0) {
                        for(PoiItem item : poiItems){
                            //获取经纬度对象
                            LatLonPoint llp = item.getLatLonPoint();
                            /**
                             * 新建poirusdata对象保存信息
                             */
                            PoiResultData poirusdata = new PoiResultData();
                            /**
                             * 获取经纬度
                             */
                            poirusdata.longititude = llp.getLongitude();
                            poirusdata.latitude = llp.getLatitude();
                            //获取标题
                            poirusdata.title = item.getTitle();
                            //获取内容
                            poirusdata.content = item.getSnippet();
                            resultList.add(poirusdata);

                        }
                        adapter = new PoiSearchAdapter(this,resultList);
                        mResultLV.setAdapter(adapter);
                        mResultLV.setOnItemClickListener(mItemClicl);

                        poiResult = null;
                    }
                }
            }else {

                ToastUtil.showToast("未搜索到结果");
            }
        }else {
            ToastUtil.showToast("加载失败");
        }
    }



    /**
     * 列表项点击侦听  显示以点击的地方为中心的地图并标记该点
     */
    private AdapterView.OnItemClickListener mItemClicl = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            //获取当前点击项的经纬度
            latitude = resultList.get(position).latitude;
            longititude = resultList.get(position).longititude;
            Log.e("***************",longititude+","+latitude);

            mMapAndResultFL.setVisibility(View.VISIBLE);
            mResultLV.setVisibility(View.GONE);

            //初始化地图  设置地图显示的中心点的经纬度及大小
            if (aMap == null) {
                aMap = mMapView.getMap();
            }
            UiSettings settings = aMap.getUiSettings();
            settings.setZoomControlsEnabled(false); //隐藏放大缩小按钮
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longititude), 100));  //经纬度及显示大小

            //把当前点击项的位置到显示地址的控件上
            mMapAddress =  resultList.get(position).title;
            mMapAddressTV.setText(mMapAddress);

            LatLng latLng = new LatLng(latitude,longititude);
            marker = aMap.addMarker(new MarkerOptions().position(latLng).title("").snippet(""));

            //绑定监听地图屏幕变化的事件
            aMap.setOnCameraChangeListener(mCameraListener);


        }
    };

    /**
     * 监听屏幕变化事件 得到屏幕中心位置的经纬度 然后把marker设置到该中心点
     * 然后把该经纬度代表的位置得到 显示在地址控件上
     */
    private AMap.OnCameraChangeListener mCameraListener = new AMap.OnCameraChangeListener() {
        @Override
        public void onCameraChange(CameraPosition cameraPosition) {

        }

        @Override
        public void onCameraChangeFinish(CameraPosition cameraPosition) {

            marker.remove();
            LatLng target = cameraPosition.target;
            latitude = target.latitude;
            longititude = target.longitude;
            LatLng latLng = new LatLng(latitude,longititude);
            marker = aMap.addMarker(new MarkerOptions().position(latLng).title("").snippet(""));
            LatLonPoint latLonPoint = new LatLonPoint(latitude,longititude);
            GeocodeSearch geocoderSearch = new GeocodeSearch(GaodeSearchForActivity.this);
            // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
            RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,GeocodeSearch.AMAP);
            geocoderSearch.getFromLocationAsyn(query);
            geocoderSearch.setOnGeocodeSearchListener(GaodeSearchForActivity.this);


        }
    };


    /**
     *  点击侦听
     */
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == mReturnLL.getId()){
                //返回
                finish();
            }else if(v.getId() == mSearchForTv.getId()){
                //搜索   如果没写搜索条件则不进行操作  否则传入关键字进行搜索操作
                deepType = mSearchTextET.getText().toString();
                //设置列表不可见 地图和地图上的显示地址的空间可见
                mMapAndResultFL.setVisibility(View.GONE);
                mResultLV.setVisibility(View.VISIBLE);
                if(!deepType.equals("")){
                    getLatlon(city,deepType);
                }
            }else if(v.getId() == mMakeSure.getId()){

                //确定按钮 得到经纬度和详细地址传回注册activity

            }
        }
    };



    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }


    //初始化搜索结果列表 进入该页面则显示以城市为区域的搜索结果的列表
    private void initData() {
        city = getIntent().getStringExtra("city");
        deepType = getIntent().getStringExtra("deepType");
        //先根据城市名获取城市编码 再根据关键字和城市编码搜索数据
        getLatlon(city,deepType);
    }


    //初始化控件
    private void initView() {

        mReturnLL = (LinearLayout) findViewById(R.id.poi_back_ll);
        mSearchTextET = (EditText) findViewById(R.id.poi_search_for_et);
        mSearchForTv = (TextView) findViewById(R.id.poi_cancel_tv);
        mMapAndResultFL = (FrameLayout) findViewById(R.id.map_and_result_fl);
        mMapView = (MapView) findViewById(R.id.map_local);
        mMapAddressTV = (TextView) findViewById(R.id.map_address_tv);
        mDetailAddress = (EditText) findViewById(R.id.address_detail_et);
        mMakeSure = (TextView) findViewById(R.id.address_resutl_makesure);
        mResultLV = (ListView) findViewById(R.id.poi_result_lv);

    }


    //知道地址名称获取经纬度和城市编码 然后通过城市编码和关键字检索数据的的方法
    private void getLatlon(String cityName, final String deepType){

        GeocodeSearch geocodeSearch=new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                if (i==1000){
                    if (geocodeResult!=null && geocodeResult.getGeocodeAddressList()!=null &&
                            geocodeResult.getGeocodeAddressList().size()>0){

                        GeocodeAddress geocodeAddress = geocodeResult.getGeocodeAddressList().get(0);
                        latitude = geocodeAddress.getLatLonPoint().getLatitude();//纬度
                        longititude = geocodeAddress.getLatLonPoint().getLongitude();//经度
                        adcode= geocodeAddress.getAdcode();//区域编码
                        Log.e("地理编码", geocodeAddress.getAdcode()+"");
                        Log.e("纬度latitude",latitude+"");
                        Log.e("经度longititude",longititude+"");
                        Log.e("################",adcode);
                        //初始化poi搜索 直接显示以city为范围的结果列表
                        query = new PoiSearch.Query(deepType,"",adcode);
                        //keyWord表示搜索字符串，
                        //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
                        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
                        query.setPageSize(30);// 设置每页最多返回多少条poiitem
                        query.setPageNum(0);//设置查询页码
                        query.setCityLimit(true);
                        //构造 PoiSearch 对象，并设置监听
                        poiSearch = new PoiSearch(GaodeSearchForActivity.this, query);
                        poiSearch.setOnPoiSearchListener(GaodeSearchForActivity.this);

                        //发送请求，异步搜索
                        poiSearch.searchPOIAsyn();

                    }else {
                        ToastUtil.showToast("地址名出错");
                    }
                }
            }
        });

        GeocodeQuery geocodeQuery=new GeocodeQuery(cityName.trim(),"29");
        geocodeSearch.getFromLocationNameAsyn(geocodeQuery);


    }

    //初始化点击事件
    private void initClick() {
        mReturnLL.setOnClickListener(mClickListener);
        mSearchForTv.setOnClickListener(mClickListener);
        mMakeSure.setOnClickListener(mClickListener);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

    }


    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

            if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null
                    && regeocodeResult.getRegeocodeAddress().getFormatAddress() != null) {

                mMapAddress = regeocodeResult.getRegeocodeAddress().getFormatAddress();
                ToastUtil.showToast(mMapAddress+"附近");
                mMapAddressTV.setText(mMapAddress);

            }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }
}
