package com.hsbc.task.handler.user;

import com.alibaba.fastjson.JSON;
import com.hsbc.task.cache.MiddleWareSimulator;
import com.hsbc.task.cache.UserTable;
import com.hsbc.task.entity.User;
import com.hsbc.task.utils.CommonUtils;
import com.hsbc.task.utils.MyEncryptUtil;
import com.hsbc.task.utils.RespUtil;
import io.muserver.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Minhua He
 * @date 2022/9/19 1:17
 * >>>description
 */
public class LoginHandler implements RouteHandler {

    @Override
    public void handle(MuRequest request, MuResponse response, Map<String, String> pathParams) throws IOException {

        // Returns null if there is no parameter with that value
        String requestBody = request.readBodyAsString();
        User loginUser = JSON.parseObject(requestBody, User.class);
        //System.out.println(loginUser);
        String username = loginUser.getUsername();
        String password = loginUser.getPassword();

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            response.write(RespUtil.error(null));
            return;
        }

        MyEncryptUtil myEncryptUtil = new MyEncryptUtil();
        String encrypPwd = myEncryptUtil.getEncryofStr(password);
        List<User> userList = UserTable.userList;

        boolean result = userList.stream().anyMatch(user -> user.getUsername().equals(username) && user.getPassword().equals(encrypPwd));

        if (result) {
            String token = myEncryptUtil.getEncryofStr(loginUser.toString());
            //find user
            User findUser = userList.stream().filter(user -> loginUser.getUsername().equals(user.getUsername())).findFirst().get();
            MiddleWareSimulator.userMap.put(token, findUser);
            String uuid = CommonUtils.getUUID();
            Cookie session = CookieBuilder.newCookie()
                    .withName("JSESSION")
                    .withValue(uuid)
                    .withMaxAgeInSeconds(60*60*2)
                    .httpOnly(true)
                    .secure(false)
                    .withSameSite("Lax")
                    .withPath("/")
                    .build();
            response.addCookie(session);

            //put the token into session
            MiddleWareSimulator.sessionMap.put(uuid,token);
            response.write(RespUtil.response(findUser,200,uuid));
        } else {
            response.write(RespUtil.error(null));
        }

    }
}
