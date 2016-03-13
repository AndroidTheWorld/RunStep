package ling.runstep.trackshow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.OnEntityListener;
import com.baidu.trace.OnTrackListener;
import com.baidu.trace.Trace;
import com.baidu.trace.TraceLocation;

import org.xutils.view.annotation.ContentView;

import java.util.HashMap;
import java.util.Map;

import ling.runstep.R;


@SuppressLint("NewApi")
public class StartSportActivity extends FragmentActivity implements OnClickListener {

    /**
     * 轨迹服务
     */
    protected static Trace trace = null;

    /**
     * entity标识
     */
    protected static String entityName = null;

    /**
     * 鹰眼服务ID，开发者创建的鹰眼服务对应的服务ID
     */
    protected static long serviceId = 109378; // serviceId为开发者创建的鹰眼服务ID
    /**
     * 轨迹服务客户端
     */
    protected static LBSTraceClient client = null;
    /**
     * Entity监听器
     */
    protected static OnEntityListener entityListener = null;
    /**
     * Track监听器
     */
    protected static OnTrackListener trackListener = null;
    protected static MapView bmapView = null;
    protected static BaiduMap mBaiduMap = null;
    protected static Context mContext = null;
    /**
     * 轨迹服务类型（0 : 不建立socket长连接， 1 : 建立socket长连接但不上传位置数据，2 : 建立socket长连接并上传位置数据）
     */
    private int traceType = 2;
    private Button btnTrackUpload;
    private Button btnTrackQuery;
    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;
    private TrackUploadFragment mTrackUploadFragment;
    private TrackQueryFragment mTrackQueryFragment;
    private QueryFragment queryFragment;

    /**
     * 获取设备IMEI码
     *
     * @param context
     * @return
     */
    protected static String getImei(Context context) {
        String mImei = "NULL";
        try {
            mImei = ((TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        } catch (Exception e) {
            System.out.println("获取IMEI码失败");
            mImei = "NULL";
        }
        return mImei;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SDKInitializer.initialize(getApplicationContext());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start_sport);

        mContext = getApplicationContext();

        // 初始化轨迹服务客户端
        client = new LBSTraceClient(mContext);

        // 初始化entity标识
        entityName = getImei(mContext);

        // 初始化轨迹服务
        trace = new Trace(getApplicationContext(), serviceId, entityName,
                traceType);

        // 初始化组件
        initComponent();

        // 初始化OnEntityListener
        initOnEntityListener();

        // 初始化OnTrackListener
        initOnTrackListener();

        client.setOnTrackListener(trackListener);

        // 添加entity
        addEntity();

        // 设置默认的Fragment
        setDefaultFragment();

    }

    /**
     * 添加Entity
     */
    private void addEntity() {
        Geofence.addEntity();
    }

    /**
     * 初始化组件
     */
    private void initComponent() {
        // 初始化控件
        btnTrackUpload = (Button) findViewById(R.id.btn_trackUpload);
        btnTrackQuery = (Button) findViewById(R.id.btn_trackQuery);

        btnTrackUpload.setOnClickListener(this);
        btnTrackQuery.setOnClickListener(this);

        fragmentManager = getSupportFragmentManager();

        bmapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = bmapView.getMap();
        bmapView.showZoomControls(false);
    }

    /**
     * 设置默认的Fragment
     */
    private void setDefaultFragment() {
        handlerButtonClick(R.id.btn_trackUpload);

    }

    /**
     * 点击事件
     */
    public void onClick(View v) {
        handlerButtonClick(v.getId());
    }

    /**
     * 初始化OnEntityListener
     */
    private void initOnEntityListener() {
        entityListener = new OnEntityListener() {

            // 请求失败回调接口
            @Override
            public void onRequestFailedCallback(String arg0) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(),
                        "entity请求失败回调接口消息 : " + arg0, Toast.LENGTH_SHORT)
                        .show();
                Looper.loop();
            }

            // 添加entity回调接口
            @Override
            public void onAddEntityCallback(String arg0) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(),
                        "添加entity回调接口消息 : " + arg0, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            // 查询entity列表回调接口
            @Override
            public void onQueryEntityListCallback(String message) {

            }

            @Override
            public void onReceiveLocation(TraceLocation location) {
                if (mTrackUploadFragment != null) {
                    mTrackUploadFragment.showRealtimeTrack(location);
                }
                System.out.println("location : " + location.toString());
            }

        };
    }

    /**
     * 初始化OnTrackListener
     */
    private void initOnTrackListener() {

        trackListener = new OnTrackListener() {

            // 请求失败回调接口
            @Override
            public void onRequestFailedCallback(String arg0) {
                Looper.prepare();
                Toast.makeText(StartSportActivity.this, "track请求失败回调接口消息 : " + arg0, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            // 查询历史轨迹回调接口
            @Override
            public void onQueryHistoryTrackCallback(String arg0) {
                super.onQueryHistoryTrackCallback(arg0);
                mTrackQueryFragment.showHistoryTrack(arg0);
            }

            @Override
            public Map<String, String> onTrackAttrCallback() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("column1", "testColumnValue1");
                map.put("column2", "testColumnValue2");
                return map;
            }

        };
    }

    /**
     * 处理tab点击事件
     *
     * @param id
     */
    private void handlerButtonClick(int id) {
        // 重置button状态
        onResetButton();
        // 开启Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 隐藏Fragment
        hideFragments(transaction);

        switch (id) {
            case R.id.btn_trackQuery:
                TrackUploadFragment.isInUploadFragment = false;
                if (mTrackQueryFragment == null) {
                    mTrackQueryFragment = new TrackQueryFragment();
//                    queryFragment = new QueryFragment();
                    transaction.add(R.id.fragment_content, mTrackQueryFragment);
                } else {
                    transaction.show(mTrackQueryFragment);
                }
                mTrackQueryFragment.addMarker();
                btnTrackQuery.setTextColor(Color.rgb(0x00, 0x00, 0xd8));
                btnTrackQuery.setBackgroundColor(Color.rgb(0x99, 0xcc, 0xff));
                mBaiduMap.setOnMapClickListener(null);
                break;

            case R.id.btn_trackUpload:
                TrackUploadFragment.isInUploadFragment = true;
                if (mTrackUploadFragment == null) {
                    mTrackUploadFragment = new TrackUploadFragment();
                    transaction.add(R.id.fragment_content, mTrackUploadFragment);
                } else {
                    transaction.show(mTrackUploadFragment);
                }
                TrackUploadFragment.addMarker();
                btnTrackUpload.setTextColor(Color.rgb(0x00, 0x00, 0xd8));
                btnTrackUpload.setBackgroundColor(Color.rgb(0x99, 0xcc, 0xff));
                mBaiduMap.setOnMapClickListener(null);
                break;
        }
        // 事务提交
        transaction.commit();

    }

    /**
     * 重置button状态
     */
    private void onResetButton() {
        btnTrackQuery.setTextColor(Color.rgb(0x00, 0x00, 0x00));
        btnTrackQuery.setBackgroundColor(Color.rgb(0xFF, 0xFF, 0xFF));
        btnTrackUpload.setTextColor(Color.rgb(0x00, 0x00, 0x00));
        btnTrackUpload.setBackgroundColor(Color.rgb(0xFF, 0xFF, 0xFF));
    }

    /**
     * 隐藏Fragment
     */
    private void hideFragments(FragmentTransaction transaction) {

        if (mTrackQueryFragment != null) {
            transaction.hide(mTrackQueryFragment);
        }
        if (mTrackUploadFragment != null) {
            transaction.hide(mTrackUploadFragment);
        }
        // 清空地图覆盖物
        mBaiduMap.clear();
    }

    @Override
    protected void onPause() {
        super.onPause();
        TrackUploadFragment.isInUploadFragment = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.onDestroy();
//        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
