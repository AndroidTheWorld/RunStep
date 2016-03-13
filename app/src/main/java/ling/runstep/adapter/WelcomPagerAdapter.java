package ling.runstep.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ling.runstep.base.BaseView;


/**
 * Created by Administrator on 2015/11/27.
 */
public class WelcomPagerAdapter extends PagerAdapter {
    private Context context;
    private List<BaseView> viewContainer;

    public WelcomPagerAdapter(Context context, List<BaseView> viewContainer) {
        this.context = context;
        this.viewContainer = viewContainer;
    }

    @Override
    public int getCount() {
        return viewContainer == null ? 0 : viewContainer.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;//true
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewContainer.get(position));
        return viewContainer.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewContainer.get(position));
    }
}
