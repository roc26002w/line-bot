package tw.com.urad.web.rest;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tw.com.urad.web.rest.service.impl.LineRestServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Rocko on 2016/12/19.
 */
@RestController
@RequestMapping("/api")
@Data
@Slf4j
public class HandleRequestResource {

    @Autowired
    LineRestServiceImpl lineRestService;

    private String replyToken;

    @RequestMapping(value = "/line-bot",
            method = RequestMethod.GET)
    public void getRequest() {

        log.debug("Request : ========");
        log.info("Request Info : ========");

    }


    @RequestMapping(value = "/get-message",
            method = RequestMethod.POST)
    public void postRequest(
            @RequestBody Map<String, Object> body) throws IOException {

        log.info("Request requestParams : ======== : {}" ,body);
        ArrayList<LinkedHashMap> list = (ArrayList)body.get("events");

        list.forEach(item -> {
            replyToken = (String) item.get("replyToken");
        });

        log.info("Request LinkedHashMap : ======== : {}" ,list);
        log.info("Request replyToken : ======== : {}" ,replyToken);

        try {
            lineRestService.replyMessage(replyToken);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
