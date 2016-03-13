package ling.runstep.base;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import org.xutils.x;

import cn.bmob.v3.Bmob;
import ling.runstep.bean.MyUser;

//import com.baidu.mapapi.SDKInitializer;


/**
 * Created by Jalyn on 2016/1/19.
 */
public class BaseApplication extends Application {
    public static String username, mobilePhoneNumber, objectId, createdAt, sessionToken;
    public static Integer age;
    public static String sex, height, weight, hobby, signature;
    private static MyUser myUser;

    public static MyUser getMyUser() {
        return myUser;
    }

    public static void setMyUser(MyUser myUser) {
        BaseApplication.myUser = myUser;
    }

//    //获取当前用户
//    public static MyUser getCurrentUser() {
//        username = (String) myUser.getObjectByKey(AppUtil.getApplicationContext(), "username");
//        mobilePhoneNumber = (String) myUser.getObjectByKey(AppUtil.getApplicationContext(), "mobilePhoneNumber");
//        objectId = (String) myUser.getObjectByKey(AppUtil.getApplicationContext(), "objectId");
//        createdAt = (String) myUser.getObjectByKey(AppUtil.getApplicationContext(), "createdAt");
//        sessionToken = (String) myUser.getObjectByKey(AppUtil.getApplicationContext(), "sessionToken");
//        age = (Integer) myUser.getObjectByKey(AppUtil.getApplicationContext(), "age");
//        sex = (String) myUser.getObjectByKey(AppUtil.getApplicationContext(), "sex");
//        hobby = (String) myUser.getObjectByKey(AppUtil.getApplicationContext(), "hobby");
//        height = (String) myUser.getObjectByKey(AppUtil.getApplicationContext(), "height");
//        weight = (String) myUser.getObjectByKey(AppUtil.getApplicationContext(), "weight");
//        signature = (String) myUser.getObjectByKey(AppUtil.getApplicationContext(), "signature");
//        return null;
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
//        初始化百度地图组件
        SDKInitializer.initialize(getApplicationContext());
        // 初始化 Bmob SDK
        Bmob.initialize(this, "e734ccec3c05f90e21e2b7bf6b6418f2");

//        myUser = new MyUser();

//        getCurrentUser();
    }
}
