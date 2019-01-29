package com.keepthinker.wavemessaging.client.controller;

import com.keepthinker.wavemessaging.client.service.SnsService;
import com.keepthinker.wavemessaging.client.utils.Constants;
import com.keepthinker.wavemessaging.core.model.ResponseData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by keepthinker on 2017/7/8.
 */
@Controller
@RequestMapping(produces = Constants.MIME_TYPE_APPLICATION_JSON)
public class SnsController {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private SnsService snsService;

    @RequestMapping(value = "sayToMyself", method = RequestMethod.POST)
    public @ResponseBody ResponseData sayToMyself(String sentence){
        try {
            snsService.sayToMyself(sentence);
            return ResponseData.newSuccess("you've send \"" + sentence + "\"");
        }catch(Exception e) {
            LOGGER.error("unexpected error", e);
            return ResponseData.RESPONSE_SERVER_ERROR;
        }
    }

    @RequestMapping(value = "sayToGroup", method = RequestMethod.POST)
    public @ResponseBody ResponseData sayToGroup(@RequestParam("groupName") String groupName,
                                                 @RequestParam("sentence") String sentence){
        try {
            snsService.sayToGroup(groupName, sentence);
            return ResponseData.newSuccess("you've send \"" + groupName + "\"");
        }catch(Exception e) {
            LOGGER.error("unexpected error", e);
            return ResponseData.RESPONSE_SERVER_ERROR;
        }
    }


    @RequestMapping(value = "createTalkGroup", method = RequestMethod.POST)
    public @ResponseBody ResponseData createTalkGroup(String groupName){
        try {
            snsService.createTalkGroup(groupName);
            return ResponseData.newSuccess("you've try to create \"" + groupName + "\" talk group");
        }catch(Exception e) {
            LOGGER.error("unexpected error", e);
            return ResponseData.RESPONSE_SERVER_ERROR;
        }
    }

    @RequestMapping(value = "deleteTalkGroup", method = RequestMethod.POST)
    public @ResponseBody ResponseData deleteTalkGroup(String groupName){
        try {
            snsService.deleteTalkGroup(groupName);
            return ResponseData.newSuccess("you've try to delete \"" + groupName + "\" talk group");
        }catch(Exception e) {
            LOGGER.error("unexpected error", e);
            return ResponseData.RESPONSE_SERVER_ERROR;
        }
    }
}
