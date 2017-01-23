package com.keepthinker.wavemessaging.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ResponseData {
	
	public static String ERROR_CODE_PARAM_INVALID = "400";
	public static String ERROR_CODE_SERVER_ERROR = "500";
	public static String ERROR_MESSAGE_PARAM = "参数错误";
	public static String ERROR_MESSAGE_SERVER = "服务器内部错误";

	@JsonInclude(Include.NON_EMPTY)
	private String errorCode;
	@JsonInclude(Include.NON_EMPTY)
	private String errorMsg;
	@JsonInclude(Include.NON_NULL)
	private Object data;

	public ResponseData() {
	}
	
	public ResponseData(Object data) {
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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public static ResponseData newSuccess(Object data){
		return new ResponseData(data);
	}

	public static final ResponseData RESPONSE_PARAM_ERROR;
	public static final ResponseData RESPONSE_SERVER_ERROR;
	
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
}
