package com.hsbc.task.handler.user;

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
 * @date 2022/9/19 22:56
 * >>>description
 */
public class FindUserByIdHandler implements RouteHandler {
    @Override
    public void handle(MuRequest muRequest, MuResponse muResponse, Map<String, String> map) throws Exception {

        String userId = muRequest.query().get("id");

        if (StringUtils.isEmpty(userId)) {
            String errorResponse = RespUtil.response(null, 500, "user id can not be null");
            muResponse.write(errorResponse);
            return;
        }

        User selectedUser = UserTable.userList.stream().filter(user -> userId.equals(user.getId())).findAny().get();

        muResponse.write(RespUtil.success(selectedUser));

    }
}
