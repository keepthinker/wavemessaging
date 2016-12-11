package com.keepthinker.wavemessaging.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ResponseData {
	
	public static String ERROR_CODE_PARAM_INVALID = "400";
	public static String ERROR_CODE_SERVER_ERROR = "500";

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
	
	
}
