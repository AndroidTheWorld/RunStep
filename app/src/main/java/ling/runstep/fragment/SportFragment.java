package ling.runstep.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import ling.runstep.R;
import ling.runstep.base.BaseFragment;
import ling.runstep.trackshow.StartSportActivity;
import ling.runstep.utils.ToastUtil;

/**
 * Created by Jalyn on 2016/1/19.
 */

//百度key : 7XvuztoSFjGWnXBgKxxbdqr7
@ContentView(R.layout.fragment_sport)
public class SportFragment extends BaseFragment {
    public MyLocationListenner myListener = new MyLocationListenner();
    // 定位相关
    LocationClient mLocClient;
    BaiduMap mBaiduMap;
    boolean isFirstLoc = true; // 是否首次定位
    //当前的标识
    BitmapDescriptor mCurrentMarker;

    @ViewInject(R.id.bmapView)
    private MapView mMapView;

    @ViewInject(R.id.iv_sport_history_normal)
    private ImageView ivSportHistoryNormal;
    //当前模式
    private MyLocationConfiguration.LocationMode mCurrentMode;

    @Override
    protected void init() {
        ToastUtil.showToastInUIThread("正在精确定位您的位置，请稍后...");
        // 地图初始化
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
//当前模式
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
//当前的标识图片
        mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_location);
        mBaiduMap
                .setMyLocationConfigeration(new MyLocationConfiguration(
                        mCurrentMode, true, mCurrentMarker));
//缩放级别[3.0--19.0]
        MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(16);
        mBaiduMap.animateMapStatus(u);

        // 定位初始化
        mLocClient = new LocationClient(getActivity().getApplicationContext());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @Event(value = R.id.iv_sport_history_normal)
    private void ViewHistoryClick(View view) {
//        getActivity().startActivity(new Intent(getActivity(), HistoricalRecordsActivity.class));
    }

    @Event(value = R.id.btn_sport_start)
    private void GoToStartActivityClick(View view) {
        getActivity().startActivity(new Intent(getActivity(), StartSportActivity.class));

    }

    @Override
    protected void initMState() {


    }

    @Override
    protected void initData() {

    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            Log.d("MyLocationListenner", "location==null:" + (location == null));
            if (location == null || mMapView == null) {
                return;
            }
//            Log.d("MyLocationListenner", "location.getAddress():" + location.getAddress());
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
}
