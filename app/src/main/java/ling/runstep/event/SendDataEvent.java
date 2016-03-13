package ling.runstep.event;

import android.os.Bundle;

/**
 * Created by Jalyn on 2016/1/20.
 */
public class SendDataEvent {
    private Class<?> activity;
    private Bundle bundle;
    public SendDataEvent(){

    }
    public SendDataEvent(Class<?> activity, Bundle bundle) {
        this.activity = activity;
        this.bundle = bundle;
    }

    public Class<?> getActivity() {
        return activity;
    }

    public void setActivity(Class<?> activity) {
        this.activity = activity;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
