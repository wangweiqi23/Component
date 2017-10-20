package com.weiqi.modulebase.model.account;

/**
 * Created by alexwangweiqi on 17/9/27.
 */

public class Account {

    public static final int NOT_LOGIN = 0;
    public static final int IS_LOGIN = 1;

    private boolean isLogin;
    private String name;
    private int age;

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
