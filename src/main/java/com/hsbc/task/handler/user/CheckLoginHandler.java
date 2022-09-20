package com.hsbc.task.handler.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hsbc.task.cache.MiddleWareSimulator;
import com.hsbc.task.utils.CommonUtils;
import com.hsbc.task.utils.RespUtil;
import io.muserver.MuRequest;
import io.muserver.MuResponse;
import io.muserver.RouteHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class CheckLoginHandler implements RouteHandler {

    @Override
    public void handle(MuRequest muRequest, MuResponse muResponse, Map<String, String> map) throws Exception {
        String requestBody = muRequest.readBodyAsString();
        Map<String, String> params = JSONObject.parseObject(requestBody, new TypeReference<Map<String, String>>(){});
        String uuid = params.get("id");

        String token = MiddleWareSimulator.sessionMap.get(uuid);
        if (StringUtils.isEmpty(token)) {
            muResponse.write(RespUtil.error(null));
            return;
        }
        muResponse.write(RespUtil.success(null));

    }
}
