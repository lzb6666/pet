package com.example.zhong.starter.util.result;

public class DataResult<T> {
    private String resultCode;
    private String msg;
    private T data;

    public DataResult(String resultCode, String msg) {
        this.resultCode = resultCode;
        this.msg = msg;
    }

    public DataResult(String resultCode, String msg, T data) {
        this.resultCode = resultCode;
        this.msg = msg;
        this.data = data;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
