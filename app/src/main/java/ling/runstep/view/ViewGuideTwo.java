package ling.runstep.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import org.xutils.view.annotation.ViewInject;

import ling.runstep.R;
import ling.runstep.base.BaseView;
import ling.runstep.utils.AppUtil;


/**
 * Created by Asstea on 2015/12/10 15:17
 */
public class ViewGuideTwo extends BaseView {
    @ViewInject(R.id.iv_intro_two)
    private ImageView ivIntroTwo;

    public ViewGuideTwo(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        View.inflate(AppUtil.getApplicationContext(), R.layout.view_guide_two, this);
    }

    @Override
    protected void initListener() {

    }
}
