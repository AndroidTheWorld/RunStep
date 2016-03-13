package ling.runstep.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.x;

/**
 * Created by Jalyn on 2016/1/19.
 */
public abstract class BaseFragment extends Fragment {
    protected View view;
    //是否首次出现
    protected boolean isFirst = true;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = x.view().inject(this, inflater, container);
        init();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initMState();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isFirst) {
            isFirst = false;
            lazyData();
        } else {
        }
    }

    /**
     * 返回Fragment的View
     *
     * @return
     */
    public View getRootView() {
        return view;
    }

    /**
     * onCreateView 时调用
     *
     * @return
     */
    protected abstract void init();

    /**
     * onViewCreated 时调用
     */
    protected abstract void initMState();

    /**
     * onActivityCreated 时调用
     */
    protected abstract void initData();

    /**
     * fragment懒加载
     */
    protected void lazyData() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isFirst = true;
    }
}
