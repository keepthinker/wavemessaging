package com.keepthinker.wavemessaging.webapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.keepthinker.wavemessaging.common.Constants;
import com.keepthinker.wavemessaging.common.ResponseData;
import com.keepthinker.wavemessaging.webapi.model.RegisterInfo;

@Controller
@RequestMapping(produces = Constants.MIME_TYPE_APPLICATION_JSON)
public class GeneralController {
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/register", consumes = Constants.MIME_TYPE_APPLICATION_JSON)
	public ResponseData register(@RequestBody RegisterInfo RegisterInfo){
		
		
		return ResponseData.newSuccess("hello");
	}
	
	@RequestMapping(value = "/ip", consumes = Constants.MIME_TYPE_APPLICATION_JSON)
	public ResponseData getIp(){

		return ResponseData.newSuccess("ip");
	}

	@RequestMapping(value = "/ping")
	public @ResponseBody String ping(){
		return "pong";
	}
}
