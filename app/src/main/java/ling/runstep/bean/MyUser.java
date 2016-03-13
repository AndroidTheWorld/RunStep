package ling.runstep.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by Jalyn on 2016/3/8.
 */
public class MyUser extends BmobUser {
    private String sex;
    private String hobby;
    private String signature;
    private String height;
    private String weight;
    private Integer age;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }


    @Override
    public String toString() {
        return "MyUser{" +
                "sex='" + sex + '\'' +
                ", hobby='" + hobby + '\'' +
                ", signature='" + signature + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", age=" + age +
                '}';
    }
}
