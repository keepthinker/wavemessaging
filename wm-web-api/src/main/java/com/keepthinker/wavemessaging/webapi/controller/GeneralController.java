package com.keepthinker.wavemessaging.webapi.controller;

import com.keepthinker.wavemessaging.core.Constants;
import com.keepthinker.wavemessaging.core.ResponseData;
import com.keepthinker.wavemessaging.webapi.model.RegisterInfo;
import com.keepthinker.wavemessaging.webapi.model.RegisterResult;
import com.keepthinker.wavemessaging.webapi.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(produces = Constants.MIME_TYPE_APPLICATION_JSON)
public class GeneralController {

    @Autowired
    private GeneralService generalService;

    /**
     * https
     *
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = Constants.MIME_TYPE_APPLICATION_JSON)
    public
    @ResponseBody
    ResponseData register(@RequestBody RegisterInfo registerInfo) {
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
