package ling.runstep.constant;

/**
 * Created by Jalyn on 2016/1/20.
 */
public interface Constant {
    interface appFinal {
        long EXIT_TIME_GAP = 3000;//返回键退出间隔时间
        long JSON_PAST_TIME_GAP = 7200000;// json有效时长：初定为两小时
        int WELCOME_DELAYED = 1000;//welcome跳转时间
        String PRODUCT_ID = "productId";
        String USER_ID = "userid";
        String GOTO = "GoActivity";
    }
}
