package ling.runstep.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import de.greenrobot.event.EventBus;
import ling.runstep.R;
import ling.runstep.base.BaseActivity;
import ling.runstep.base.BaseApplication;
import ling.runstep.base.BaseFragment;
import ling.runstep.constant.Constant;
import ling.runstep.event.NavFragmentEvent;
import ling.runstep.event.SendDataEvent;
import ling.runstep.fragment.FindFragment;
import ling.runstep.fragment.MeFragment;
import ling.runstep.fragment.MsgFragment;
import ling.runstep.fragment.SportFragment;
import ling.runstep.utils.AppUtil;
import ling.runstep.utils.ToastUtil;
import ling.runstep.widget.QuickFragmentTabHost;


@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
//    public static String APPID = "15adf39506cccd67f4ae3a7ad27e1f16";
private String my;

    private final String[] TITLES = {"运动", "发现", "消息", "我的"};
    private final String[] TAGS = {"sport", "find", "msg", "me"};
    private final Class[] CLAZZ = {SportFragment.class, FindFragment.class, MsgFragment.class, MeFragment.class};
    private final int[] ICONS = {R.layout.fragment_sport_tab, R.layout.fragment_find_tab, R.layout.fragment_msg_tab, R.layout.fragment_me_tab};
    @ViewInject(android.R.id.tabhost)
    private QuickFragmentTabHost fragmentTabHost;
    private long mExitTime = 0;

    @Override
    protected void init() {

        EventBus.getDefault().register(this);
        AppUtil.setFirstVisit();
        fragmentTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        for (int i = 0; i < TITLES.length; i++) {
            View view = View.inflate(this, ICONS[i], null);
            fragmentTabHost.addTab(
                    fragmentTabHost.newTabSpec(
                            TAGS[i]).setIndicator(view),
                    CLAZZ[i],
                    null);
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }
    public void onEventMainThread(SendDataEvent event) {
        Intent intent = new Intent();
        Bundle bundle = event.getBundle();
        if (bundle != null)
            intent.putExtras(bundle);
        intent.setClass(this, event.getActivity());
        startActivity(intent);
    }


        // 用EventBus 来导航,订阅者
    public void onEventMainThread(NavFragmentEvent event) {
        BaseFragment fragment = event.fragment;
        Bundle bundle = event.bundle;
    }
    //重写返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (SystemClock.uptimeMillis() - mExitTime > Constant.appFinal.EXIT_TIME_GAP) {
                ToastUtil.showToastInThread("再按一次退出应用");
                mExitTime = SystemClock.uptimeMillis();
            } else {
                //退出登录
                BaseApplication.getMyUser().logOut(this);
//                退出程序
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
