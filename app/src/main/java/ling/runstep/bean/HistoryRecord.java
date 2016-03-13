package ling.runstep.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Jalyn on 2016/3/6.
 */
public class HistoryRecord extends BmobObject {
    public MyUser uid;
    private String distance;
    private String totalTime;
    private String speed;
    private String calorie;

    public MyUser getUid() {
        return uid;
    }

    public void setUid(MyUser uid) {
        this.uid = uid;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }
}
