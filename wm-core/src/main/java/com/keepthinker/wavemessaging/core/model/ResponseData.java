package com.keepthinker.wavemessaging.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by keepthinker on 2017/1/30.
 */
public class ResponseData {
    public static final ResponseData RESPONSE_PARAM_ERROR;
    public static final ResponseData RESPONSE_SERVER_ERROR;
    public static String ERROR_CODE_PARAM_INVALID = "400";
    public static String ERROR_CODE_SERVER_ERROR = "500";
    public static String ERROR_MESSAGE_PARAM = "paramError";
    public static String ERROR_MESSAGE_SERVER = "ServerError";

    static {
        ResponseData paramErrorData = new ResponseData();
        paramErrorData.setErrorCode(ERROR_CODE_PARAM_INVALID);
        paramErrorData.setErrorMsg(ERROR_MESSAGE_PARAM);
        RESPONSE_PARAM_ERROR = paramErrorData;
        ResponseData serverErrorData = new ResponseData();
        serverErrorData.setErrorCode(ERROR_CODE_SERVER_ERROR);
        serverErrorData.setErrorMsg(ERROR_MESSAGE_SERVER);
        RESPONSE_SERVER_ERROR = serverErrorData;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String errorCode;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String errorMsg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    public ResponseData() {
    }

    public ResponseData(Object data) {
        this.data = data;
    }

    public static ResponseData newSuccess(Object data) {
        return new ResponseData(data);
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}