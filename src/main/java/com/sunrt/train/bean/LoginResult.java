package com.sunrt.train.bean;

public class LoginResult {

    private boolean isSuccess;
    private String msg;

    public LoginResult(){}

    public LoginResult(boolean isSuccess, String msg) {
        this.isSuccess = isSuccess;
        this.msg = msg;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "LoginResult{" +
                "isSuccess=" + isSuccess +
                ", msg='" + msg + '\'' +
                '}';
    }
}
