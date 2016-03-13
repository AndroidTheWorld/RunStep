package ling.runstep.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;
import ling.runstep.R;
import ling.runstep.base.BaseActivity;
import ling.runstep.bean.MyUser;
import ling.runstep.utils.ToastUtil;

/**
 * Created by Jalyn on 2016/3/7.
 */
@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {

    @ViewInject(R.id.edt_register_phone)
    private EditText phoneRegisterEdt;

    @ViewInject(R.id.edt_register_pw)
    private EditText pwRegisterEdt;

    @ViewInject(R.id.edt_register_sms_code)
    private EditText smsCodeRegisterEdt;

    @ViewInject(R.id.btn_register_get_sms_code)
    private Button getSmsCodeBtn;

    @ViewInject(R.id.btn_register_sure)
    private Button sureRegisterBtn;

    @ViewInject(R.id.btn_register_cancel)
    private  Button cancelRegisterBtn;

    private Intent intent;
    private String phoneRegisterStr,pwRegisterStr,smsCodeRegisterStr;
    private MyUser myUser;

    //获取短信验证码
    @Event(value = R.id.btn_register_get_sms_code)
    private void getSmsCodeClick(View view){
        phoneRegisterStr = phoneRegisterEdt.getText().toString().trim();
        if (!TextUtils.isEmpty(phoneRegisterStr)) {
            BmobSMS.requestSMSCode(this, phoneRegisterStr, "注册模板", new RequestSMSCodeListener() {

                @Override
                public void done(Integer smsId, BmobException ex) {
                    // TODO Auto-generated method stub
                    if (ex == null) {//验证码发送成功
                        ToastUtil.showToastInThread("验证码发送成功，短信id：" + smsId);//用于查询本次短信发送详情
                    } else {
                        ToastUtil.showToastInThread("errorCode = " + ex.getErrorCode() + ",errorMsg = " + ex.getLocalizedMessage());
                    }
                }
            });
        } else {
            ToastUtil.showToastInThread("请输入手机号码");
        }
    }

//注册
    @Event(value = R.id.btn_register_sure)
    private void sureRegisterClick(View view){
        phoneRegisterStr = phoneRegisterEdt.getText().toString().trim();
        pwRegisterStr = pwRegisterEdt.getText().toString().trim();
        smsCodeRegisterStr = smsCodeRegisterEdt.getText().toString().trim();
        if (!TextUtils.isEmpty(phoneRegisterStr) && !TextUtils.isEmpty(smsCodeRegisterStr)) {
            BmobSMS.verifySmsCode(this, phoneRegisterStr, smsCodeRegisterStr, new VerifySMSCodeListener() {
                @Override
                public void done(BmobException ex) {
                    // TODO Auto-generated method stub
                    if (ex == null) {//短信验证码已验证成功
                        ToastUtil.showToastInThread("验证通过");
                        if (!TextUtils.isEmpty(pwRegisterStr)) {
                            myUser.setUsername(phoneRegisterStr);
                            myUser.setPassword(pwRegisterStr);
                            myUser.setMobilePhoneNumber(phoneRegisterStr);
                            myUser.signUp(getApplicationContext(), new SaveListener() {

                                @Override
                                public void onSuccess() {
                                    ToastUtil.showToastInThread("注册成功");
                                    intent = new Intent(getApplicationContext(),LoginActivity.class);
                                    intent.putExtra("username",phoneRegisterStr);
                                    intent.putExtra("password",pwRegisterStr);
                                    startActivity(intent);
                                    RegisterActivity.this.finish();
                                }
                                @Override
                                public void onFailure(int i, String s) {
                                    ToastUtil.showToastInThread("注册失败");
                                }
                            });
                        }
                    } else {
                        ToastUtil.showToastInThread("验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                    }
                }
            });
        } else {
            ToastUtil.showToastInThread("请输入手机号和验证码");
        }
    }

    @Override
    protected void init() {

        myUser = new MyUser();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }
}
