package com.keepthinker.wavemessaging.client;

/**
 * Created by keepthinker on 2017/1/30.
 */
public class RegisterResponse {
    private String errorCode;
    private String errorMsg;
    private Long data;

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

    public Long getData() {
        return data;
    }

    public void setData(Long data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RegisterResponse{" +
                "errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
