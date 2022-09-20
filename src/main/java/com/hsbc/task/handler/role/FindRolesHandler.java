package com.hsbc.task.handler.role;

import com.hsbc.task.cache.RoleTable;
import com.hsbc.task.utils.RespUtil;
import io.muserver.MuRequest;
import io.muserver.MuResponse;
import io.muserver.RouteHandler;

import java.util.Map;

/**
 * @author Minhua He
 * @date 2022/9/20 1:10
 * >>>description
 */
public class FindRolesHandler implements RouteHandler {

    @Override
    public void handle(MuRequest muRequest, MuResponse muResponse, Map<String, String> map) throws Exception {
        String success = RespUtil.success(RoleTable.roleList);
        muResponse.write(success);
    }
}
