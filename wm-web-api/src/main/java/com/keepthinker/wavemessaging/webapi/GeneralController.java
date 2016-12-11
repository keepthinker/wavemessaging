package com.keepthinker.wavemessaging.webapi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.keepthinker.wavemessaging.common.Constants;
import com.keepthinker.wavemessaging.common.ResponseData;

@Controller
@RequestMapping(produces = Constants.MIME_TYPE_APPLICATION_JSON)
public class GeneralController {
	
	@RequestMapping(value = "/register", consumes = Constants.MIME_TYPE_APPLICATION_JSON)
	public ResponseData register(){
		
		return ResponseData.newSuccess("hello");
	}

	@RequestMapping(value = "/ping")
	public @ResponseBody String ping(){
		return "pong";
	}
}
