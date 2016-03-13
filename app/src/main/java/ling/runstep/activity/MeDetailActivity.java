package ling.runstep.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
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
import ling.runstep.base.BaseActivity;
import ling.runstep.base.BaseApplication;
import ling.runstep.bean.MyUser;
import ling.runstep.event.SendToEvent;
import ling.runstep.utils.AppUtil;
import ling.runstep.utils.ToastUtil;
import ling.runstep.utils.WheelDialog;

/**
 * Created by Jalyn on 2016/3/8.
 */
@ContentView(R.layout.activity_me_detail)
public class MeDetailActivity extends BaseActivity {
    //    bomb的id
    private static String objectId;

    private MyUser newUser;
    //返回键
    @ViewInject(R.id.iv_me_detail_back)
    private ImageView ivMeDetailBack;

    private int ageIs;

    @ViewInject(R.id.layout_sex)
    private RelativeLayout LayoutUserSex;

    @ViewInject(R.id.layout_age)
    private RelativeLayout LayoutUserAge;

    @ViewInject(R.id.layout_hobby)
    private RelativeLayout LayoutUserHobby;

    @ViewInject(R.id.edt_user)
    private EditText edtUser;

    @ViewInject(R.id.tv_me_detail_sex)
    private TextView tvUserSex;

    @ViewInject(R.id.tv_me_detail_age)
    private TextView tvUserAge;

    @ViewInject(R.id.tv_me_detail_hobby)
    private TextView tvUserHobby;

    @ViewInject(R.id.tv_me_detail_sdf)
    private TextView tvUserSDF;

    @ViewInject(R.id.tv_me_detail_height)
    private TextView tvHeight;

    @ViewInject(R.id.tv_me_detail_weight)
    private TextView tvWeight;

    private DisplayMetrics dm = new DisplayMetrics();
    private int heightIs;
    private int weightIs;
    private WheelDialog wheelDialog;
    private Window window;
    private int width;
    private int height;
    /**
     * 修改个性签名
     */

    private AlertDialog.Builder changeSignatureDialog;
    private View viewChangeSignature;
    private LayoutInflater inflaterChangeSignature;
    /**
     * 修改兴趣
     */
    private AlertDialog.Builder changeHobbyDialog;
    private View viewChangeHobby;
    private LayoutInflater inflaterChangeHobby;
    /**
     * 修改性别
     * 需要重新修改
     */
    private AlertDialog.Builder changeSexDialog;
    private String[] sex_choice = {"男", "女"};
    private String sex;
    //记录单选选中的id
    private int SEX_ID = -1;

    @Override
    protected void init() {
//        newUser = BaseApplication.getMyUser();

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        final MyUser bmobUser = BmobUser.getCurrentUser(this, MyUser.class);
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.addWhereEqualTo("username", AppUtil.getUserPhone());
        query.findObjects(this, new FindListener<MyUser>() {

            @Override
            public void onSuccess(List<MyUser> list) {
                for (MyUser myUser : list) {
                    if (AppUtil.getObjectId().equals(myUser.getObjectId())) {
//                                获得信息
                        tvUserSex.setText(myUser.getSex());
                        tvUserAge.setText(myUser.getAge());
                        tvUserHobby.setText(myUser.getHobby());
                        tvUserSDF.setText(myUser.getSignature());
                        tvWeight.setText(myUser.getWeight());
                        tvHeight.setText(myUser.getHeight());
                        //存在全局，以便调用
                        BaseApplication.setMyUser(myUser);

                        Log.d("MeDetailActivity", "myUser:" + myUser);
                    }
                }
            }

            @Override
            public void onError(int code, String msg) {
                //直接获取缓存在本地的数据
                tvUserSex.setText(bmobUser.getSex());
                tvUserAge.setText(bmobUser.getAge() + "");
                tvUserHobby.setText(bmobUser.getHobby());
                tvUserSDF.setText(bmobUser.getSignature());
                tvWeight.setText(bmobUser.getWeight());
                tvHeight.setText(bmobUser.getHeight());
            }
        });

    }


    /**
     * 修改体重
     */
    @Event(value = R.id.layout_weight)
    private void ChangeUserWeight(View view) {
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        wheelDialog = new WheelDialog(
                MeDetailActivity.this,
                new WheelDialog.SelecctListener() {
                    @Override
                    public void refreshSelectUi(String num, WheelDialog.CallBack back) {
                        weightIs = Integer.parseInt(num);
                        back.execute();
                    }
                },

                new WheelDialog.CallBack() {
                    @Override
                    public void execute() {
                        tvWeight.setText(weightIs + "KG");


                    }
                },
                heightIs,
                width,
                height,
                "体重",
                1);
        window = wheelDialog.getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
        wheelDialog.setCancelable(true);
        wheelDialog.setTitle("体重(KG)");
        wheelDialog.show();
    }

    /**
     * 修改身高
     */
    @Event(value = R.id.layout_height)
    private void ChangeUserHeight(View view) {
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        wheelDialog = new WheelDialog(
                MeDetailActivity.this,
                new WheelDialog.SelecctListener() {
                    @Override
                    public void refreshSelectUi(String num, WheelDialog.CallBack back) {
                        heightIs = Integer.parseInt(num);
                        back.execute();
                    }
                },

                new WheelDialog.CallBack() {
                    @Override
                    public void execute() {
                        tvHeight.setText(heightIs + "CM");
                    }
                },
                heightIs,
                width,
                height,
                "身高",
                1);
        window = wheelDialog.getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
        wheelDialog.setCancelable(true);
        wheelDialog.setTitle("身高(CM)");
        wheelDialog.show();
    }
//个性签名
    @Event(value = R.id.layout_signature)
    private void ChangeUserSignature(View view) {
        inflaterChangeSignature = LayoutInflater.from(this);
        viewChangeSignature = inflaterChangeSignature.inflate(R.layout.view_popu_user, null);
        changeSignatureDialog = new AlertDialog.Builder(this);
        changeSignatureDialog.setIcon(R.mipmap.ic_launcher)
                .setTitle("个性签名")
                .setView(viewChangeSignature)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText edtUserSDF = (EditText) viewChangeSignature.findViewById(R.id.edt_user);
                        tvUserSDF.setText(edtUserSDF.getText().toString());
                        ToastUtil.showToastInThread("修改成功");

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtil.showToastInThread("取消");
                    }
                })
                .create()
                .show();
    }
//兴趣
    @Event(value = R.id.layout_hobby)
    private void ChangeUserHobby(View view) {
        inflaterChangeHobby = LayoutInflater.from(this);
        viewChangeHobby = inflaterChangeHobby.inflate(R.layout.view_popu_user, null);
        changeHobbyDialog = new AlertDialog.Builder(this);
        changeHobbyDialog.setIcon(R.mipmap.ic_launcher)
                .setTitle("兴趣")
                .setView(viewChangeHobby)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText edtUserHobby = (EditText) viewChangeHobby.findViewById(R.id.edt_user);
                        tvUserHobby.setText(edtUserHobby.getText().toString());
                        ToastUtil.showToastInThread("修改成功");

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtil.showToastInThread("取消");
                    }
                })
                .create()
                .show();
    }

    /**
     * 修改年龄
     */

    @Event(value = R.id.layout_age)
    private void ChangeUserAge(View view) {

        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;

        wheelDialog = new WheelDialog(
                MeDetailActivity.this,
                new WheelDialog.SelecctListener() {
                    @Override
                    public void refreshSelectUi(String num, WheelDialog.CallBack back) {
                        ageIs = Integer.parseInt(num);
                        back.execute();
                    }
                },
                new WheelDialog.CallBack() {
                    @Override
                    public void execute() {
                        tvUserAge.setText(String.valueOf(ageIs) + "岁");

                    }
                },
                ageIs,
                width,
                height,
                "年龄（岁）",
                1);
        window = wheelDialog.getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
        wheelDialog.setCancelable(true);
        wheelDialog.setTitle("年龄");
        wheelDialog.show();
    }
//性别
    @Event(value = R.id.layout_sex)
    private void ChangeUserSex(View view) {
        changeSexDialog = new AlertDialog.Builder(this);
        changeSexDialog.setTitle("性别")
                .setIcon(R.mipmap.ic_launcher)
                .setSingleChoiceItems(sex_choice, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SEX_ID = which;
                        sex = sex_choice[which];
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (SEX_ID > 0) {
                            ToastUtil.showToastInThread("修改成功");
                            tvUserSex.setText(sex);

                        } else {
                            tvUserSex.setText("男");
                        }

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtil.showToastInThread("取消");
                    }
                })
                .create()
                .show(); }

    //返回键的调用
    @Event(value = R.id.iv_me_detail_back)
    private void GoBackToMeActivityClick(View view) {

        if(TextUtils.isEmpty(tvUserAge.getText().toString())){
            newUser.setAge(Integer.valueOf(tvUserAge.getText().toString()));
        }
        //保存数据
        final MyUser bmobUser = BmobUser.getCurrentUser(this, MyUser.class);
        newUser.setSex(tvUserSex.getText().toString());
//        newUser.setAge(Integer.valueOf(tvUserAge.getText().toString()));
        newUser.setHobby(tvUserHobby.getText().toString());
        newUser.setSignature(tvUserSDF.getText().toString());
        newUser.setHeight(tvHeight.getText().toString());
        newUser.setWeight(tvWeight.getText().toString());
        newUser.update(this, bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                ToastUtil.showToastInThread("更新用户信息成功");
                EventBus.getDefault().post(new SendToEvent.sendUserName(bmobUser.getUsername()));
            }

            @Override
            public void onFailure(int code, String msg) {
            }
        });
        finish();
    }

    @Override
    public void finish() {
        super.finish();
    }
}
