package com.hsbc.task.handler.user;

import com.hsbc.task.cache.UserTable;
import com.hsbc.task.entity.User;
import com.hsbc.task.utils.RespUtil;
import io.muserver.MuRequest;
import io.muserver.MuResponse;
import io.muserver.RouteHandler;

import java.util.List;
import java.util.Map;

/**
 * @author Minhua He
 * @date 2022/9/19 22:50
 * >>>description
 */
public class FindUserHandler implements RouteHandler {

    @Override
    public void handle(MuRequest muRequest, MuResponse muResponse, Map<String, String> map) throws Exception {
        List<User> userList = UserTable.userList;
        muResponse.write(RespUtil.success(userList));
    }
}
