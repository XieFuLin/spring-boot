package com.xfl.boot.common.constant;

/**
 * Created by XFL
 * time on 2017/6/20 21:59
 * description:
 */
public class MsgConstant {
    private Integer status;
    private String message;

    public MsgConstant(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
