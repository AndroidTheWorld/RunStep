package ling.runstep.trackshow;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.OnStopTraceListener;
import com.baidu.trace.TraceLocation;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.listener.SaveListener;
import ling.runstep.R;
import ling.runstep.bean.HistoryRecord;
import ling.runstep.bean.MyUser;
import ling.runstep.utils.AppUtil;
import ling.runstep.utils.ToastUtil;


/**
 * 轨迹追踪
 */
@SuppressLint("NewApi")
public class TrackUploadFragment extends Fragment implements View.OnClickListener, Chronometer.OnChronometerTickListener {

    public static long time = 0;
    /**
     * 开启轨迹服务监听器
     */
    protected static OnStartTraceListener startTraceListener = null;
    /**
     * 停止轨迹服务监听器
     */
    protected static OnStopTraceListener stopTraceListener = null;
    // 覆盖物
    protected static OverlayOptions overlay;
    protected static MapStatusUpdate msUpdate = null;
    protected static boolean isInUploadFragment = true;
    /**
     * 图标
     */
    private static BitmapDescriptor realtimeBitmap;
    // 路线覆盖物
    private static PolylineOptions polyline = null;
    private static List<LatLng> pointList = new ArrayList<LatLng>();
    protected TextView tvEntityName = null;

    protected boolean isTraceStart = false;
    /**
     * 刷新地图线程(获取实时点)
     */
    protected RefreshThread refreshThread = null;
    //计时器
    private Chronometer chronometer;
    //chronometer 的计算时间的miss
    private int miss = 0;
    //距离
    private TextView tvDistance;
    //速度
    private TextView tvSpeed;
    //卡路里
    private TextView tvColorie;
    //距离
    private double distance;
    //耗时
    private double useTime;
    //卡路里
    private double colorie;
    //速度
    private float speed = 0.0f;
    private Button btnStartTrace = null;
    private Button btnStopTrace = null;
    private Button btnOperator = null;
    //重新开始
    private Button btnRestartTrace = null;
    private Geofence geoFence = null;
    /**
     * 开始和结束时间
     */
    private long startTime;
    private long endTime;
    /**
     * 采集周期（单位 : 秒）
     */
    private int gatherInterval = 10;
    private View view = null;
    private LayoutInflater mInflater = null;
    //速度 单位 : 公里/小时
    private float speedString;
    //开始和结束点
    private LatLng startLatlng;
    private LatLng endLatlng;
    private boolean isFirst = true;
    //高度 单位：米
    private int altitude;
    //当前定位时间（格式 : yyyy-MM-dd HH:mm:ss, eg : 2015-01-01 14:01:01）
    private String currentTimeString;
    private StringBuffer sb = new StringBuffer();
    private Handler handler = new Handler();

    /**
     * 添加地图覆盖物
     */
    protected static void addMarker() {

        if (null != msUpdate) {
            StartSportActivity.mBaiduMap.setMapStatus(msUpdate);
        }

        // 路线覆盖物
        if (null != polyline) {
            StartSportActivity.mBaiduMap.addOverlay(polyline);
        }

        // 围栏覆盖物
        if (null != Geofence.fenceOverlay) {
            StartSportActivity.mBaiduMap.addOverlay(Geofence.fenceOverlay);
        }

        // 实时点覆盖物
        if (null != overlay) {
            StartSportActivity.mBaiduMap.addOverlay(overlay);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trackupload, container, false);
        // 初始化
        init();
        // 初始化监听器
        initListener();
        // 设置采集周期
        setInterval();
        // 设置http请求协议类型
        setRequestType();
        mInflater = inflater;
        return view;
    }
private HistoryRecord historyRecord;
    private MyUser myUser;
    /**
     * 初始化
     */
    private void init() {
        btnStartTrace = (Button) view.findViewById(R.id.btn_startTrace);
        btnStopTrace = (Button) view.findViewById(R.id.btn_stopTrace);
        btnStopTrace.setVisibility(View.GONE);
        btnOperator = (Button) view.findViewById(R.id.btn_operator);
        btnRestartTrace = (Button) view.findViewById(R.id.btn_restartTrace);
        btnRestartTrace.setVisibility(View.GONE);
        tvEntityName = (TextView) view.findViewById(R.id.tv_entityName);
        tvColorie = (TextView) view.findViewById(R.id.tv_calorie);
        tvDistance = (TextView) view.findViewById(R.id.tv_distance);
        tvSpeed = (TextView) view.findViewById(R.id.tv_speed);
        chronometer = (Chronometer) view.findViewById(R.id.chronometer);
        chronometer.setOnChronometerTickListener(this);
        chronometer.setFormat("hh:mm:ss");

        historyRecord = new HistoryRecord();

        //开始按钮
        btnStartTrace.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //开始的时间
//                startTime = System.currentTimeMillis();
                Toast.makeText(getActivity(), "正在开启轨迹服务，请稍候", Toast.LENGTH_LONG).show();
                //开启轨迹服务
                startTrace();
                //初始化距离
                tvDistance.setText("0.0");
                //初始化速度
                tvSpeed.setText("0.0");
                //初始化卡路里
                tvColorie.setText("0.0");
            }
        });
//停止按钮
        btnStopTrace.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //按钮停止时，标识符置为true
                isFirst = true;
                Toast.makeText(getActivity(), "正在停止轨迹服务，请稍候", Toast.LENGTH_SHORT).show();
                stopTrace();
                //停止按钮隐藏
                btnStopTrace.setVisibility(View.GONE);
                //把重新开始按钮隐藏
                btnRestartTrace.setVisibility(View.GONE);
            }
        });
//操作按钮
        btnOperator.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                geoFence = new Geofence(getActivity(), mInflater);
                if (geoFence.popupwindow != null && geoFence.popupwindow.isShowing()) {
                    geoFence.popupwindow.dismiss();
                    return;
                } else {
                    geoFence.initPopupWindowView();
                    geoFence.popupwindow.showAsDropDown(v, 0, 5);
                }
            }
        });
//重新开始
        btnRestartTrace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //先停止
//              stopTrace();
                //设置开始按钮消失
                btnStartTrace.setVisibility(View.GONE);
                //复位
                miss = 0;
                ToastUtil.showToastInThread("正在重新开启轨迹服务，请稍候");
                //开启轨迹服务
                startTrace();
                chronometer.start();
            }
        });
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        // 初始化开启轨迹服务监听器
        initOnStartTraceListener();
        // 初始化停止轨迹服务监听器
        initOnStopTraceListener();
    }

    /**
     * 开启轨迹服务
     */
    private void startTrace() {
        // 通过轨迹服务客户端client开启轨迹服务
        StartSportActivity.client.startTrace(StartSportActivity.trace, startTraceListener);
    }

    /**
     * 停止轨迹服务
     */
    private void stopTrace() {
        // 通过轨迹服务客户端client停止轨迹服务
        StartSportActivity.client.stopTrace(StartSportActivity.trace, stopTraceListener);
    }

    /**
     * 设置采集周期和打包周期
     */
    private void setInterval() {
        // 位置采集周期
        gatherInterval = 10;
        // 打包周期
        int packInterval = 60;
        StartSportActivity.client.setInterval(gatherInterval, packInterval);
    }

    /**
     * 设置请求协议
     */
    private void setRequestType() {
        int type = 0;
        StartSportActivity.client.setProtocolType(type);
    }

    /**
     * 查询entityList
     */
    @SuppressWarnings("unused")
    private void queryEntityList() {
        // entity标识列表（多个entityName，以英文逗号"," 分割）
        String entityNames = StartSportActivity.entityName;
        // 属性名称（格式为 : "key1=value1,key2=value2,....."）
        String columnKey = "";
        // 返回结果的类型（0 : 返回全部结果，1 : 只返回entityName的列表）
        int returnType = 0;
        // 活跃时间（指定该字段时，返回从该时间点之后仍有位置变动的entity的实时点集合）
        int activeTime = (int) (System.currentTimeMillis() / 1000 - 30);
        // 分页大小
        int pageSize = 10;
        // 分页索引
        int pageIndex = 1;

        StartSportActivity.client.queryEntityList(StartSportActivity.serviceId, entityNames, columnKey, returnType, activeTime,
                pageSize,
                pageIndex, StartSportActivity.entityListener);
    }

    /**
     * 查询实时轨迹
     */
    private void queryRealtimeTrack() {
        StartSportActivity.client.queryRealtimeLoc(StartSportActivity.serviceId, StartSportActivity.entityListener);
    }

    /**
     * 初始化OnStartTraceListener
     * <p/>
     * errorNo - 错误编码
     * 0：success，
     * 10000：开启服务请求发送失败，
     * 10001：开启服务失败，
     * 10002：参数错误，
     * 10003：网络连接失败，
     * 10004：网络未开启，
     * 10005：服务正在开启，
     * 10006：服务已开启，
     * 10007：服务正在停止，
     * 10008：开启缓存，
     * 10009：已开启缓存
     */
    private void initOnStartTraceListener() {
        // 初始化startTraceListener
        startTraceListener = new OnStartTraceListener() {

            // 开启轨迹服务回调接口（arg0 : 消息编码，arg1 : 消息内容，详情查看类参考）
            public void onTraceCallback(int arg0, String arg1) {
                // TODO Auto-generated method stub
                showMessage("开启轨迹服务回调接口消息 [消息编码 : " + arg0 + "，消息内容 : " + arg1 + "]", Integer.valueOf(arg0));
                if (0 == arg0 || 10006 == arg0 || 10008 == arg0) {
                    isTraceStart = true;
                    //开启更新线程
                    startRefreshThread(true);

                    miss = 0;
                    // 开始计时
                    chronometer.start();
                    //让开始按钮消失
                    btnStartTrace.setVisibility(View.GONE);
                    //设置重新开始按钮显示
                    btnRestartTrace.setVisibility(View.VISIBLE);
                    //设置停止按钮显示
                    btnStopTrace.setVisibility(View.VISIBLE);
                } else {
                    //让开始按钮显示
                    btnStartTrace.setVisibility(View.VISIBLE);
                }
            }

            // 轨迹服务推送接口（用于接收服务端推送消息，arg0 : 消息类型，arg1 : 消息内容，详情查看类参考）
            public void onTracePushCallback(byte arg0, String arg1) {
                showMessage("轨迹服务推送接口消息 [消息类型 : " + arg0 + "，消息内容 : " + arg1 + "]", null);
            }

        };
    }

    /**
     * 初始化OnStopTraceListener
     */
    private void initOnStopTraceListener() {
        // 初始化stopTraceListener
        stopTraceListener = new OnStopTraceListener() {

            // 轨迹服务停止成功
            public void onStopTraceSuccess() {
                showMessage("停止轨迹服务成功--速度为："
                        + speedString + "公里/小时--距离为"
                        + distance + "公里--当前时间"
                        + currentTimeString + "--高度"
                        + altitude + "米"
                        , Integer.valueOf(1));
                isTraceStart = false;
                startRefreshThread(false);

                //停止计时
                chronometer.stop();
                //得到消耗的时间，单位是秒（s）
                long minutes = Integer.parseInt(chronometer.getText().toString().split(":")[1]);
                long seconds = Integer.parseInt(chronometer.getText().toString().split(":")[2]);
                useTime = minutes * 60 + seconds;
                ToastUtil.showToastInThread("消耗的时间是" + useTime + "秒");

                //让开始按钮显示
                btnStartTrace.setVisibility(View.VISIBLE);
                //设置重新开始按钮消失
                btnRestartTrace.setVisibility(View.GONE);
                //设置停止按钮消失
                btnStopTrace.setVisibility(View.GONE);

                //记录在Bmob云端
                historyRecord.setSpeed(speedString + "");
//                historyRecord.setCalorie(calorie);
                historyRecord.setDistance(distance+"");
                historyRecord.setTotalTime(useTime + "");
                historyRecord.setUid(myUser);
                historyRecord.save(AppUtil.getApplicationContext(), new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(AppUtil.getApplicationContext(), "添加数据成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(AppUtil.getApplicationContext(), "添加数据失败", Toast.LENGTH_SHORT).show();
                    }
                });


            }

            // 轨迹服务停止失败（arg0 : 错误编码，arg1 : 消息内容，详情查看类参考）
            public void onStopTraceFailed(int arg0, String arg1) {
                showMessage("停止轨迹服务接口消息 [错误编码 : " + arg0 + "，消息内容 : " + arg1 + "]", null);
            }
        };
    }

    //计时器
    @Override
    public void onChronometerTick(Chronometer chronometer) {
        miss++;
        chronometer.setText(FormatMiss(miss));
    }

    // 将秒转化成小时分钟秒
    public String FormatMiss(int miss) {
        String hh = miss / 3600 > 9 ? miss / 3600 + "" : "0" + miss / 3600;
        String mm = (miss % 3600) / 60 > 9 ? (miss % 3600) / 60 + "" : "0" + (miss % 3600) / 60;
        String ss = (miss % 3600) % 60 > 9 ? (miss % 3600) % 60 + "" : "0" + (miss % 3600) % 60;
        return hh + ":" + mm + ":" + ss;
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 显示实时轨迹
     *
     * @param location
     */
    protected void showRealtimeTrack(TraceLocation location) {

        if (null == refreshThread || !refreshThread.refresh) {
            return;
        }
        //这里是能进来的。
        Log.d("TrackUploadFragment", "location:" + location);

        sb.append(location.toString() + "\n");

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        speedString = location.getSpeed();
        altitude = location.getAltitude();
        currentTimeString = location.getTime();

        if (Math.abs(latitude - 0.0) < 0.000001 && Math.abs(longitude - 0.0) < 0.000001) {
            showMessage("当前查询无轨迹点", null);
        } else {
            final LatLng latLng = new LatLng(latitude, longitude);
            pointList.add(latLng);
            if (isInUploadFragment) {

                //绘制实时点
                drawRealtimePoint(latLng);
                //这是异步的，用handler更新UI。
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //设置速度
                        tvSpeed.setText(speedString / 3.6 + " ");
                        //计算空间距离
                        getDistance(latLng);
                    }
                });
            }
        }
    }

    //更新实时距离
    private void getDistance(LatLng latLng) {
        //如果是第一次进来这个方法，不计算，并将标识符置为false，设置初始位置
        if (isFirst) {
            startLatlng = latLng;
            isFirst = !isFirst;
            return;
        }
        distance += DistanceUtil.getDistance(startLatlng, latLng);
        startLatlng = latLng;
//        设置距离
        tvDistance.setText(String.valueOf(distance));

    }

    /**
     * 绘制实时点
     */
    private void drawRealtimePoint(LatLng point) {

        StartSportActivity.mBaiduMap.clear();

        MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(18).build();

        msUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);

        realtimeBitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);

        overlay = new MarkerOptions().position(point)
                .icon(realtimeBitmap).zIndex(9).draggable(true);

        if (pointList.size() >= 2 && pointList.size() <= 10000) {
            // 添加路线（轨迹）
            polyline = new PolylineOptions().width(10)
                    .color(Color.RED).points(pointList);
        }
        addMarker();
    }

    protected void startRefreshThread(boolean isStart) {
        if (null == refreshThread) {
            refreshThread = new RefreshThread();
        }
        refreshThread.refresh = isStart;
        if (isStart) {
            if (!refreshThread.isAlive()) {
                refreshThread.start();
            }
        } else {
            refreshThread = null;
        }
    }

    private void showMessage(final String message, final Integer errorNo) {

        new Handler(StartSportActivity.mContext.getMainLooper()).post(new Runnable() {
            public void run() {
                Toast.makeText(StartSportActivity.mContext, message, Toast.LENGTH_LONG).show();

                if (null != errorNo) {
                    if (0 == errorNo.intValue() || 10006 == errorNo.intValue() || 10008 == errorNo.intValue()
                            || 10009 == errorNo.intValue()) {
                        btnStartTrace.setBackgroundColor(Color.rgb(0x99, 0xcc, 0xff));
                        btnStartTrace.setTextColor(Color.rgb(0x00, 0x00, 0xd8));
                    } else if (1 == errorNo.intValue() || 10004 == errorNo.intValue()) {
                        btnStartTrace.setBackgroundColor(Color.rgb(0xff, 0xff, 0xff));
                        btnStartTrace.setTextColor(Color.rgb(0x00, 0x00, 0x00));
                    }
                }
            }
        });
    }

    protected class RefreshThread extends Thread {

        protected boolean refresh = true;

        @Override
        public void run() {
            while (refresh) {
                // 查询实时轨迹
                queryRealtimeTrack();
                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException e) {
                    System.out.println("线程休眠失败");
                }
            }

        }
    }
}
