package com.keepthinker.wavemessaging.webapi.service;

import com.keepthinker.wavemessaging.webapi.model.RegisterInfo;
import com.keepthinker.wavemessaging.webapi.model.RegisterResult;

public interface GeneralService {

	/**
	 * generate a unique id for the username.<br/>
	 * save client info in redis
	 * @param registerInfo
	 */
	RegisterResult register(RegisterInfo registerInfo);
}
