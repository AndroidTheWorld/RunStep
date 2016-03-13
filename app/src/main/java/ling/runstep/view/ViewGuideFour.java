package ling.runstep.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import ling.runstep.R;
import ling.runstep.activity.MainActivity;
import ling.runstep.base.BaseView;
import ling.runstep.utils.AppUtil;
import ling.runstep.utils.ToastUtil;


/**
 * Created by Asstea on 2015/12/10 15:17
 */
public class ViewGuideFour extends BaseView {
    @ViewInject(R.id.iv_intro_three)
    private ImageView iVIntroThree;

    public ViewGuideFour(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        View.inflate(AppUtil.getApplicationContext(), R.layout.view_guide_four, this);
    }

    @Override
    protected void initListener() {

    }

    @Event(value = R.id.btn_start, type = View.OnClickListener.class)
    private void onClick(View v) {
//        EventBus.getDefault().post(new MainActivity());
        ToastUtil.showToastInThread("开启运动之旅");
        Intent intent = new Intent(getContext(), MainActivity.class);
//        startActivity(intent);
    }

}
