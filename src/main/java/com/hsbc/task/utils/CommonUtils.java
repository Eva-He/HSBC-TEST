package com.hsbc.task.utils;

import com.hsbc.task.cache.MiddleWareSimulator;
import com.hsbc.task.entity.User;
import io.muserver.MuRequest;

import java.util.UUID;

/**
 * @author Minhua He
 * @date 2022/9/19 19:17
 * >>>description
 */
public class CommonUtils {


    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getToken(MuRequest request) {

        String sessionId = request.cookie("JSESSION").orElse("");
        String token = MiddleWareSimulator.sessionMap.get(sessionId);
        return token;
    }

    public static User getUserByToken(MuRequest request) {

        String token = getToken(request);
        User user = MiddleWareSimulator.userMap.get(token);
        return user;
    }

}
