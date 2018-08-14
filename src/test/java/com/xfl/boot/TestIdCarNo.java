package com.xfl.boot;

import com.xfl.boot.common.utils.IdCardVerification;

import java.text.ParseException;

/**
 * Created by XFL
 * time on 2018/8/14 23:53
 * description:
 */
public class TestIdCarNo {
    public static void main(String[] args) {
        try {
            System.out.println(IdCardVerification.IDCardValidate("662662663256326"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
