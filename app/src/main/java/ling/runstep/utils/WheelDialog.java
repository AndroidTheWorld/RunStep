package ling.runstep.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import ling.runstep.R;
import ling.runstep.trackutils.NumericWheelAdapter;
import ling.runstep.trackutils.WheelView;

/**
 * Created by Jalyn on 2016/1/30.
 */
public class WheelDialog extends Dialog implements View.OnClickListener{


    private Button btnSure,btnCancel;
    private Context context;
    private int width;
    private int height;
    private SelecctListener selecctListener;
    private LinearLayout selectLayout;
    private CallBack callBack;
    private int currentNum;
    private String title;
    private int flag = 0;
    private WheelView selectView = null;
    private NumericWheelAdapter selectAdapter = null;

    public WheelDialog(Context context, SelecctListener listener,
                       CallBack callBack, int currentNum,
                       int width, int height, String title, int flag) {
        super(context);
        this.context = context;
        this.selecctListener = listener;
        this.callBack = callBack;
        this.currentNum = currentNum;
        this.width = width;
        this.height = height;
        this.title = title;
        this.flag = flag;
    }

    public WheelDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected WheelDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sure:
                selecctListener.refreshSelectUi(selectAdapter.getValues(),
                        callBack);
                this.dismiss();
                break;
            case  R.id.btn_cancel:
                this.dismiss();
                break;
        }
    }

    public interface CallBack{
        public void execute();
    }
    public interface SelecctListener{
        public void refreshSelectUi(String num, CallBack back);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_age);
        selectLayout = (LinearLayout) findViewById(R.id.layout_age);
        selectView = (WheelView) findViewById(R.id.wheel_view);
        btnSure = (Button) findViewById(R.id.btn_sure);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height / 3 + 10);
        selectLayout.setLayoutParams(params);
        selectAdapter = new NumericWheelAdapter(0, 300);
        selectView.setAdapter(selectAdapter);
        int cc = 20;
        selectView.setCurrentItem(cc);
        selectView.setVisibleItems(5);
        if (flag != 1) {
            btnSure.setText("下一步");
        } else {
            btnSure.setText("确定");
        }
        btnCancel.setOnClickListener(this);
        btnSure.setOnClickListener(this);
    }
    @Override
    public void dismiss() {
        super.dismiss();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
}
