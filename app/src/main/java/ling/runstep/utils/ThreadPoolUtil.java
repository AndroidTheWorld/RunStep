package ling.runstep.utils;

import android.os.Handler;

/**
 * Created by Jalyn on 2016/1/19.
 */
public class ThreadPoolUtil {

    //UI的handler
    private static Handler handler = new Handler();

    public static void runTaskOnThread(Runnable runnable) {
        ThreadPoolFactory.getCommonThreadPool().execute(runnable);
    }

    public static void runTaskOnUIThread(Runnable runnable) {
        handler.post(runnable);
    }
}
