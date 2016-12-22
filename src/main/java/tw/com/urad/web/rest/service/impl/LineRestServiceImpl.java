package tw.com.urad.web.rest.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tw.com.urad.web.rest.service.LineRESTService;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rocko on 2016/12/21.
 */
@Slf4j
@Service
public class LineRestServiceImpl implements LineRESTService {

    private RestTemplate restTemplate = new RestTemplate();

    private ObjectMapper mapper = new ObjectMapper();

    final String CHANNEL_ACCESS_TOKEN = "geuTwedOV73cHvBHeGiI1+j7XXadHlMUX6t2I2LIKLNPwMCiJWK8ZOiQJXEg8nZ3YPujAwvN8dR7PLfGW2lQAI/cvMmSvnop7KmzGRlUWWJCLDXTHlKQ36UcNVJ9ypE9nwFrFgfwvJTC9ZtK+iVsqgdB04t89/1O/w1cDnyilFU=";

    public ResponseEntity replyMessage(String replyToken) throws HttpClientErrorException ,Exception {

        Map<String, Object> bodyMap = new HashMap<>();
        ArrayList hashMessages = new ArrayList<>();
        Map<String,String> messageBody = new HashMap<>();
        messageBody.put("type","text");
        messageBody.put("text","hi");
        hashMessages.add(messageBody);

        bodyMap.put("replyToken", replyToken);
        bodyMap.put("messages", hashMessages);

        URI uri = this.createUri();

        log.debug("Line Uri :" , uri);

        HttpEntity postBodyAndHeader = setHeaderAndRequestBody(bodyMap);

        ResponseEntity<Map> response
                = restTemplateMakeCallExchange(uri, HttpMethod.POST, postBodyAndHeader, Map.class);

        return response;
    }

    private URI createUri() {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.line.me/v2/bot/message/reply")
                .build()
                .toUri();
    }


    private HttpEntity setHeaderAndRequestBody(Map<String, Object> body) {
        String jsonBody = "";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add(HttpHeaders.AUTHORIZATION,"Bearer " + CHANNEL_ACCESS_TOKEN);

        try {
            jsonBody = mapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        return new HttpEntity(jsonBody, headers);
    }


    private <T> ResponseEntity<T> restTemplateMakeCallExchange(
            URI uri, HttpMethod httpMethod , HttpEntity bodyAndHeader, Class<T> responseType) throws HttpClientErrorException, Exception {
        try {
            return restTemplate.exchange(uri, httpMethod, bodyAndHeader, responseType);
        } catch (HttpClientErrorException clientError) {
            log.warn("LINE request URI: {} with http method: {}", uri.toString(), httpMethod);
            log.warn("error code is {} ", clientError.getStatusCode());
            log.warn("and response with error header: {} ", clientError.getResponseHeaders());
            log.warn("and error body: {} ", clientError.getResponseBodyAsString());
            throw clientError;
        } catch (Exception apiEx) {
            apiEx.printStackTrace();
            log.warn("Unexpected exception was thrown: {}", apiEx.getMessage());
            throw apiEx;
        }
    }

}
