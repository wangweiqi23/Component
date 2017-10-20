package com.weiqi.modulebase.model.dog;

import java.io.Serializable;

/**
 * Created by alexwangweiqi on 17/9/20.
 */

public class DogObject implements Serializable {

    private String name;
    private int age;

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
