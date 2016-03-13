package ling.runstep.base;

import android.content.Context;
import android.widget.LinearLayout;

import org.xutils.x;

/**
 * Created by Jalyn on 2016/1/19.
 */
public abstract class BaseView extends LinearLayout {
    public BaseView(Context context) {
        super(context);
        initView();
        x.view().inject(this);
        initListener();
    }


    protected abstract void initView();

    protected abstract void initListener();
}
