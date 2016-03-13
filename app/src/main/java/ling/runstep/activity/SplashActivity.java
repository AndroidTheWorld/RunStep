package ling.runstep.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import ling.runstep.R;
import ling.runstep.base.BaseActivity;

/**
 * Created by Jalyn on 2016/3/7.
 */
@ContentView(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {
    @ViewInject(R.id.iv_splash)
    private ImageView splashIv;

    @Event(value = R.id.iv_splash)
    private void splashClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
    }

    @Override
    protected void init() {


    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }
}
