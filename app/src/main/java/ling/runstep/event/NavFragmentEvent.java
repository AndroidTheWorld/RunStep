package ling.runstep.event;

import android.os.Bundle;

import ling.runstep.base.BaseFragment;


/**
 * Created by Jalyn on 2016/2/18.
 */
public class NavFragmentEvent {
    public BaseFragment fragment;
    public Bundle bundle;

    public NavFragmentEvent(BaseFragment fragment) {
        this.fragment = fragment;
    }

    public NavFragmentEvent(BaseFragment fragment, Bundle bundle) {
        this.fragment = fragment;
        this.bundle = bundle;
    }
}
