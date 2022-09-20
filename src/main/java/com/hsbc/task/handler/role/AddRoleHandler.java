package com.hsbc.task.handler.role;

import com.alibaba.fastjson.JSON;
import com.hsbc.task.cache.RoleTable;
import com.hsbc.task.entity.Role;
import com.hsbc.task.entity.User;
import com.hsbc.task.utils.CommonUtils;
import com.hsbc.task.utils.RespUtil;
import io.muserver.MuRequest;
import io.muserver.MuResponse;
import io.muserver.RouteHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author Minhua He
 * @date 2022/9/20 1:21
 * >>>description
 */
@Slf4j
public class AddRoleHandler implements RouteHandler {
    @Override
    public void handle(MuRequest muRequest, MuResponse muResponse, Map<String, String> map) throws Exception {
        String requestBody = muRequest.readBodyAsString();
        Role inputRole = JSON.parseObject(requestBody, Role.class);

        String roleName = inputRole.getRoleName();
        if (StringUtils.isEmpty(roleName)) {
            String msg = "role name can not be empty";
            String error = RespUtil.response(null,500, msg);
            log.info(msg);
            muResponse.write(error);
            return;
        }

        boolean anyMatch = RoleTable.roleList.stream().anyMatch(role -> roleName.equals(role.getRoleName()));
        if (anyMatch) {
            String msg = "role name duplicated";
            String error = RespUtil.response(null,500, msg);
            log.info(msg);
            muResponse.write(error);
            return;
        }

        inputRole.setRoleId(CommonUtils.getUUID());
        RoleTable.roleList.add(inputRole);
        log.info("add role succeed");
        muResponse.write(RespUtil.success("success"));
    }
}
