package ling.runstep.utils;

import android.widget.Toast;

import org.xutils.x;

/**
 * Created by Jalyn on 2016/1/19.
 */
public class ToastUtil {
    public static void showToastInUIThread(String content) {
        Toast.makeText(x.app(), content, Toast.LENGTH_SHORT).show();
    }

    public static void showToastInThread(final String content) {
        ThreadPoolUtil.runTaskOnUIThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(x.app(), content, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
