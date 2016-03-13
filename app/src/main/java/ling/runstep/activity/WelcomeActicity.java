package ling.runstep.activity;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import ling.runstep.R;
import ling.runstep.adapter.WelcomPagerAdapter;
import ling.runstep.base.BaseActivity;
import ling.runstep.base.BaseView;
import ling.runstep.utils.DisplayUtils;
import ling.runstep.view.ViewGuideFour;
import ling.runstep.view.ViewGuideOne;
import ling.runstep.view.ViewGuideThree;
import ling.runstep.view.ViewGuideTwo;

/**
 * Created by Jalyn on 2016/3/6.
 */
@ContentView(R.layout.activity_guide)
public class WelcomeActicity extends BaseActivity {


    public Class[] VIEWS = {ViewGuideOne.class, ViewGuideTwo.class, ViewGuideThree.class, ViewGuideFour.class};

    @ViewInject(R.id.mViewPager)
    private ViewPager mViewPager;
    @ViewInject(R.id.iv_dot_one)
    private ImageView dotImageOne;
    @ViewInject(R.id.iv_dot_two)
    private ImageView dotImageTwo;
    @ViewInject(R.id.iv_dot_three)
    private ImageView dotImageThree;
    @ViewInject(R.id.iv_dot_four)
    private ImageView dotImageFour;
    @ViewInject(R.id.layout_notice_index)
    private LinearLayout layoutNoticeIndex;
    @ViewInject(R.id.tv_go_app)
    private TextView tvGoApp;
    private List<BaseView> viewContainer = new ArrayList<BaseView>();
    private List<ImageView> dotContainer;

    @Override
    protected void init() {
        // 初始化4个welcome的view
        initWelComeView();
        // 初始化4个点
        initDotContainer();
        // viewPager 设置adapter
        mViewPager.setAdapter(new WelcomPagerAdapter(getApplicationContext(), viewContainer));
        // 注册eventBus 的事件
//        EventBus.getDefault().register(this);
    }


    private void initDotContainer() {
        dotContainer = new ArrayList<ImageView>();
        dotContainer.add(dotImageOne);
        dotContainer.add(dotImageTwo);
        dotContainer.add(dotImageThree);
        dotContainer.add(dotImageFour);
    }

    private void initWelComeView() {
        for (int i = 0; i < VIEWS.length; i++) {
            try {
                Constructor constructor = VIEWS[i].getConstructor(Context.class);
                BaseView baseView = (BaseView) constructor.newInstance(getApplicationContext());
                viewContainer.add(baseView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void initListener() {

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BaseView baseView = viewContainer.get(position);//美女  n1=;
                ImageView imageView = dotContainer.get(position);
                changeState(imageView);
                // 控制所有点的状态和直接跳转的状态
                if (baseView instanceof ViewGuideFour) {
                    layoutNoticeIndex.setVisibility(View.GONE);
                    tvGoApp.setVisibility(View.GONE);
                } else {
                    layoutNoticeIndex.setVisibility(View.VISIBLE);
                    tvGoApp.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        tvGoApp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                goToSplashFragment();
//                Intent intent = new Intent(WelcomeActicity.class,MainActivity.class);
//
//            }
//        });


    }

//    @Event(value =  R.id.btn_start)
//    private void startClick(View view){
//        Intent intent = new Intent(WelcomeActicity.class,MainActivity.class);
//
//    }

//    public void onEventMainThread(GoHomeEvent event){
//        goToSplashFragment();
//    }
//
//    // 跳转到SplashFragmnet
//    private void goToSplashFragment() {
//        SplashFragment splashFragment=new SplashFragment();
////        MainActivity mainActivity= (MainActivity) getActivity();
////        mainActivity.startFragment(splashFragment, null);
//        EventBus.getDefault().post(new NavFragmentEvent(splashFragment));
//    }

    public void changeState(ImageView imageView) {
        for (int i = 0; i < dotContainer.size(); i++) {//px-----dp:100dp--->Npx
            dotContainer.get(i).setLayoutParams(new LinearLayout.LayoutParams(DisplayUtils.dip2px(getApplicationContext(), 8), DisplayUtils.dip2px(getApplicationContext(), 8)));
        }
        imageView.setLayoutParams(new LinearLayout.LayoutParams(DisplayUtils.dip2px(getApplicationContext(), 12), DisplayUtils.dip2px(getApplicationContext(), 12)));
    }

    @Override
    protected void initData() {

    }

//    @Override
//    public boolean finish() {
//        return true;
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }
}
