package ling.runstep.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import de.greenrobot.event.EventBus;
import ling.runstep.R;
import ling.runstep.activity.LoginActivity;
import ling.runstep.activity.MeDetailActivity;
import ling.runstep.base.BaseApplication;
import ling.runstep.base.BaseFragment;
import ling.runstep.bean.MyUser;
import ling.runstep.event.SendToEvent;
import ling.runstep.utils.AppUtil;
import ling.runstep.utils.ToastUtil;
import ling.runstep.utils.WheelDialog;

/**
 * Created by Jalyn on 2016/1/19.
 */
@ContentView(R.layout.fragment_me)
public class MeFragment extends BaseFragment {

//    历史记录
    @ViewInject(R.id.laout_me_history_records)
    RelativeLayout layout_history_records;

//    用户头像
    @ViewInject(R.id.iv_me_avatar)
    private ImageView ivMeAvatar;

//    用户名
    @ViewInject(R.id.tv_me_username)
    private TextView meUserNameTextView;

    //消息通知
    @ViewInject(R.id.tv_me_msg)
    private TextView meMsgTv;

    @Override
    protected void init() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initMState() {

    }

    @Event(value = R.id.iv_me_avatar)
    private void GoToDetailActivityClick(View view) {
        if (AppUtil.isLogin()) {
            getActivity().startActivity(new Intent(getActivity(), MeDetailActivity.class));
        } else {
            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }
    @Event(value = R.id.tv_unlogin)
    private void unLoginOnclick(View view) {
        AppUtil.setLogout();
        BmobUser.logOut(getContext());   //清除缓存用户对象
    }
    public void onEventMainThread(SendToEvent.sendUserName sendname) {
        if (sendname.equals("")) {
            meUserNameTextView.setText("空的");
        } else {
            meUserNameTextView.setText(sendname.loginNameString);
        }
    }
    @Override
    protected void initData() {

        meUserNameTextView.setText(BaseApplication.getMyUser().getMobilePhoneNumber());
//        Log.d("123", BaseApplication.getMyUser().getUsername());
//        Log.d("123",BaseApplication.getMyUser().getMobilePhoneNumber());
//        Log.d("123",BaseApplication.getMyUser().getSex());


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
