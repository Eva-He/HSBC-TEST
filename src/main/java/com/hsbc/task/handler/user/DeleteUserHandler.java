package com.hsbc.task.handler.user;

import com.alibaba.fastjson.JSON;
import com.hsbc.task.cache.UserTable;
import com.hsbc.task.entity.User;
import com.hsbc.task.utils.RespUtil;
import io.muserver.MuRequest;
import io.muserver.MuResponse;
import io.muserver.RouteHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author Minhua He
 * @date 2022/9/19 23:12
 * >>>description
 */
public class DeleteUserHandler implements RouteHandler {
    @Override
    public void handle(MuRequest request, MuResponse response, Map<String, String> map) throws Exception {
        // Returns null if there is no parameter with that value
        String requestBody = request.readBodyAsString();
        User selectedUser = JSON.parseObject(requestBody, User.class);
        String userId = selectedUser.getId();

        if (StringUtils.isBlank(userId)) {
            response.write(RespUtil.error(null));
            return;
        }
        User removingUser = UserTable.userList.stream().filter(user -> userId.equals(user.getId())).findFirst().orElse(null);

        if (removingUser != null) {
            UserTable.userList.remove(removingUser);
            response.write(RespUtil.success("success"));
        }else{
            response.write(RespUtil.success("user not exists"));
        }

    }
}
