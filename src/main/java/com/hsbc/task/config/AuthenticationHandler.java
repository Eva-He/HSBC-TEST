package com.hsbc.task.config;

import com.hsbc.task.cache.MiddleWareSimulator;
import com.hsbc.task.entity.User;
import com.hsbc.task.utils.CommonUtils;
import com.hsbc.task.utils.RespUtil;
import io.muserver.MuHandler;
import io.muserver.MuRequest;
import io.muserver.MuResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Slf4j
public class AuthenticationHandler implements MuHandler {

    @Override
    public boolean handle(MuRequest muRequest, MuResponse muResponse) throws Exception {

        //pass the static resource
        String path = muRequest.serverURI().getPath();
        String[] split = path.split("\\.");
        String suffix = split[split.length - 1];
        String[] unfilter = MiddleWareSimulator.config.getProperty("unfilter").split(",");
        if (Arrays.stream(unfilter).anyMatch(x->suffix.equals(x))) {
            return false;
        }

        String token = CommonUtils.getToken(muRequest);

        User loginUser = CommonUtils.getUserByToken(muRequest);
        if (StringUtils.isEmpty(token) || loginUser == null) {
            log.info("token not found.");
            muResponse.status(403);
            muResponse.write(RespUtil.response(null,403,"token not found"));
            return true;
        }

        return false;
    }
}
