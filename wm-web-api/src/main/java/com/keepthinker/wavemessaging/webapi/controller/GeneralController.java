package com.keepthinker.wavemessaging.webapi.controller;

import com.keepthinker.wavemessaging.core.utils.Constants;
import com.keepthinker.wavemessaging.core.model.ResponseData;
import com.keepthinker.wavemessaging.webapi.model.LoginInfo;
import com.keepthinker.wavemessaging.webapi.model.LoginResult;
import com.keepthinker.wavemessaging.webapi.model.RegisterInfo;
import com.keepthinker.wavemessaging.webapi.model.RegisterResult;
import com.keepthinker.wavemessaging.webapi.service.GeneralService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(produces = Constants.MIME_TYPE_APPLICATION_JSON)
public class GeneralController {
    private final static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private GeneralService generalService;

    /**
     * https
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = Constants.MIME_TYPE_APPLICATION_JSON)
    public @ResponseBody ResponseData register(@RequestBody RegisterInfo registerInfo) {
        LOGGER.info("register: " + registerInfo);
        try {
            RegisterResult result = generalService.register(registerInfo);
            if (result.isSuccess()) {
                return ResponseData.newSuccess(result.getClientId());
            } else {
                ResponseData responseData = new ResponseData();
                responseData.setErrorCode(ResponseData.ERROR_CODE_PARAM_INVALID);
                responseData.setErrorMsg("该账号已存在，请换其他账号");
                return responseData;
            }
        } catch (Exception e) {
            LOGGER.error("register error", e);
            return ResponseData.RESPONSE_SERVER_ERROR;
        }
    }

    /**
     * https
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = Constants.MIME_TYPE_APPLICATION_JSON)
    public @ResponseBody ResponseData login(@RequestBody LoginInfo loginInfo){
        LOGGER.info("login: " + loginInfo);
        try {
            LoginResult result = generalService.login(loginInfo);
            return ResponseData.newSuccess(result.getToken());
        } catch (Exception e) {
            LOGGER.error("login error", e);
            return ResponseData.RESPONSE_SERVER_ERROR;
        }
    }

    @RequestMapping(value = "/ip", method = RequestMethod.POST, consumes = Constants.MIME_TYPE_APPLICATION_JSON)
    public
    @ResponseBody
    ResponseData getIp() {

        return ResponseData.newSuccess("ip");
    }

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public
    @ResponseBody
    String ping() {
        return "pong";
    }
}
