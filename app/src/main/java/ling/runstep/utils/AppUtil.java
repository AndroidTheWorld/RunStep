package ling.runstep.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.WindowManager;
import android.widget.ImageView;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * Created by Jalyn on 2016/1/19.
 */
public class AppUtil {

    private static final String IS_APPVERSION = "isAppversion";
    private static final String IS_LOGIN = "isLogin";
    private static final String USER_PHONE = "userPhone";
    private static final String OBJECTID = "ObjectId";

    public static Context getApplicationContext() {
        return x.app().getApplicationContext();
    }


    public static boolean isFirstVisit() {
        return SharedPreferencesUtil.getInt(getApplicationContext(), IS_APPVERSION) != getAppversion(getApplicationContext());
    }

    public static void setFirstVisit() {
        SharedPreferencesUtil.putInt(getApplicationContext(), IS_APPVERSION, getAppversion(getApplicationContext()));
    }

    //图片设置
    public static ImageOptions getImageOptions(int wDb, int hDb, int drawableId) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(wDb), DensityUtil.dip2px(hDb))
                .setCrop(true)
                        // 加载中或错误图片的ScaleType
                .setPlaceholderScaleType(ImageView.ScaleType.CENTER_CROP)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(drawableId)
                .build();
        return imageOptions;
    }

    public static ImageOptions getImageOptionsByPx(int wPx, int hPx, int drawableId) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setSize(wPx, hPx)
                .setCrop(true)
                        // 加载中或错误图片的ScaleType
                .setPlaceholderScaleType(ImageView.ScaleType.CENTER_CROP)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(drawableId)
                .build();
        return imageOptions;
    }


    public static int getWeightPx() {
        WindowManager wm = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return width;
    }

    /**
     * 判读是否登陆
     *
     * @return
     */
    public static boolean isLogin() {
        return SharedPreferencesUtil.getBoolean(getApplicationContext(), IS_LOGIN, false);
    }

    /**
     * 登陆设置
     */
    public static void setLogin() {
        SharedPreferencesUtil.putBoolean(getApplicationContext(), IS_LOGIN, true);
    }

    /**
     * 登出设置
     */
    public static void setLogout() {
        SharedPreferencesUtil.putBoolean(getApplicationContext(), IS_LOGIN, false);
    }

    /**
     * 获取详情的objectId
     *
     * @return
     */
    public static String getObjectId() {
        return SharedPreferencesUtil.getString(getApplicationContext(), OBJECTID);
    }

    /**
     * 设置详情的objectId
     *
     * @param objectId
     */
    public static void setObjectId(String objectId) {
        SharedPreferencesUtil.putString(getApplicationContext(), OBJECTID, objectId);
    }

    /**
     * 获取账号
     *
     * @return
     */
    public static String getUserPhone() {
        return SharedPreferencesUtil.getString(getApplicationContext(), USER_PHONE);
    }

    /**
     * 设置账号
     *
     * @param userPhone
     */
    public static void setUserPhone(String userPhone) {
        SharedPreferencesUtil.putString(getApplicationContext(), USER_PHONE, userPhone);
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static int getAppversion(Context context) {
        if (context == null) return 0;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 加载图片
     *
     * @param wDp               图片宽dp
     * @param hDp               图片高dp
     * @param defaultDrawableId 默认图片
     * @param iv                ImageView控件
     * @param imaUrl            图片url
     */
    public static void loadImage(int wDp, int hDp, int defaultDrawableId, ImageView iv, String imaUrl) {
        ImageOptions options = getImageOptions(wDp, hDp, defaultDrawableId);
        x.image().bind(iv, imaUrl, options);
    }

    /**
     * 加载图片
     *
     * @param wPx               图片宽dp
     * @param hPx               图片高dp
     * @param defaultDrawableId 默认图片
     * @param iv                ImageView控件
     * @param imaUrl            图片url
     */
    public static void loadImageByPx(int wPx, int hPx, int defaultDrawableId, ImageView iv, String imaUrl) {
        ImageOptions options = getImageOptionsByPx(wPx, hPx, defaultDrawableId);
        x.image().bind(iv, imaUrl, options);
    }
}
