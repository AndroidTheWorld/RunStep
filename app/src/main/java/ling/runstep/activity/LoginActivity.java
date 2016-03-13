package ling.runstep.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import de.greenrobot.event.EventBus;
import ling.runstep.R;
import ling.runstep.base.BaseActivity;
import ling.runstep.base.BaseApplication;
import ling.runstep.bean.MyUser;
import ling.runstep.event.SendToEvent;
import ling.runstep.utils.AppUtil;
import ling.runstep.utils.ToastUtil;

/**
 * Created by Jalyn on 2016/3/7.
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @ViewInject(R.id.edt_login_phone)
    private EditText phoneLoginEdt;

    @ViewInject(R.id.edt_login_pw)
    private EditText pwLoginEdt;

    @ViewInject(R.id.btn_login_sure)
    private Button sureLoginBtn;

    @ViewInject(R.id.btn_login_cancel)
    private Button cancelLoginBtn;

    @ViewInject(R.id.tv_to_register)
    private TextView toRegisterTv;

    private Intent intent;
    private String username, password, phoneLoginStr, pwLoginStr;
    private MyUser myUser;
    private String mobilePhoneNumber, objectId, createdAt, sessionToken, sex, height, weight, hobby, signature;
    private Integer age;

    //登录
    @Event(value = R.id.btn_login_sure)
    private void sureLoginClick(View view) {
        phoneLoginStr = phoneLoginEdt.getText().toString().trim();
        pwLoginStr = pwLoginEdt.getText().toString().trim();

        BaseApplication.setMyUser(myUser);

        myUser.setUsername(phoneLoginStr);
        myUser.setPassword(pwLoginStr);
        myUser.login(getApplicationContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                ToastUtil.showToastInThread("登录成功");

                EventBus.getDefault().post(new SendToEvent.sendUserName(phoneLoginStr));
                //在sp存账号。
                AppUtil.setUserPhone(phoneLoginStr);
                AppUtil.setLogin();
                //BmobUser.getCurrentUser(LoginActivity.this)拿到缓存在本地的用户对象
                AppUtil.setObjectId(BmobUser.getCurrentUser(LoginActivity.this).getObjectId());
                //存在SQLite
//                intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
                //获取用户的详情
                getUserDetail();
//                LoginActivity.this.finish();
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtil.showToastInThread("登录失败");
            }
        });
    }

    private void getUserDetail() {
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.addWhereEqualTo("username", AppUtil.getUserPhone());
        query.findObjects(this, new FindListener<MyUser>() {

            @Override
            public void onSuccess(List<MyUser> list) {
                for (MyUser myUser : list) {
                    if (AppUtil.getObjectId().equals(myUser.getObjectId())) {
                        //存在全局，以便调用
                        BaseApplication.setMyUser(myUser);

                    }
                }
            }

            @Override
            public void onError(int code, String msg) {
            }
        });
    }

    //去注册
    @Event(value = R.id.tv_to_register)
    private void toRegisterClick(View view) {
        intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }
    //取消
    @Event(value = R.id.btn_login_cancel)
    private void cancelLoginClickClick(View view) {
        ToastUtil.showToastInThread("取消");
    }

    @Override
    protected void init() {
//        myUser = new MyUser();
//        username = intent.getStringExtra("username");
//        password = intent.getStringExtra("password");
//        phoneLoginEdt.setText(username);
//        pwLoginEdt.setText(password);


    }


    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }


}
