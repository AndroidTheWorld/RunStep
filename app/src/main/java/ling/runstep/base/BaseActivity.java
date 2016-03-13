package ling.runstep.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.xutils.x;

/**
 * Created by Jalyn on 2016/1/19.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        init();
        initListener();
        initData();
    }

    protected abstract void init();

    protected abstract void initListener();

    protected abstract void initData();


}
