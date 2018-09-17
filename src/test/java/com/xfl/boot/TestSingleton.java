package com.xfl.boot;

/**
 * Created by XFL
 * time on 2018/8/28 0:13
 * description:
 */
public class TestSingleton {

    private volatile static TestSingleton single = null;

    public static TestSingleton getInstance() { //1
        if (single == null) {                  //2
            synchronized (TestSingleton.class) {//3
                if (single == null) { //4
                    single = new TestSingleton();//5
                }
            }
        }
        return single;//6
    }
}
