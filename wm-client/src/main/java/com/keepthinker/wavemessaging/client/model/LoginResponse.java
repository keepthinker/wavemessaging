package com.keepthinker.wavemessaging.client.model;

/**
 * Created by keepthinker on 2017/3/5.
 */
public class LoginResponse {
    private String errorCode;
    private String errorMsg;
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
