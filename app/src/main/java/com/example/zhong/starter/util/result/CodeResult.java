package com.example.zhong.starter.util.result;

public class CodeResult implements PetResult {
    private int rstCode;//resultCode
    private String msg;

    public CodeResult(int rstCode, String msg) {
        this.rstCode= rstCode;
        this.msg = msg;
    }

    public int getRstCode() {
        return rstCode;
    }

    public void setRstCode(int rstCode) {
        this.rstCode = rstCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
