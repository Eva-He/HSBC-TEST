package com.hsbc.task.handler.user;

import com.alibaba.fastjson.JSON;
import com.hsbc.task.cache.RoleTable;
import com.hsbc.task.cache.UserTable;
import com.hsbc.task.entity.Role;
import com.hsbc.task.entity.User;
import com.hsbc.task.utils.RespUtil;
import io.muserver.MuRequest;
import io.muserver.MuResponse;
import io.muserver.RouteHandler;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Minhua He
 * @date 2022/9/20 3:52
 * >>>description
 */
public class UpdateRoleToUserHandler implements RouteHandler {
    @Override
    public void handle(MuRequest muRequest, MuResponse muResponse, Map<String, String> map) throws Exception {

        String requestBody = muRequest.readBodyAsString();
        User inputUser = JSON.parseObject(requestBody, User.class);

        //get user by id
        User findUser = UserTable.userList.stream().filter(user -> inputUser.getId().equals(user.getId())).findFirst().get();

        List<Role> userRoleList = inputUser.getRoleList();

        if (userRoleList!=null && userRoleList.size()>0) {

            List<Role> resultRoleList = RoleTable.roleList.stream().map(role -> {
                boolean anyMatch = userRoleList.stream().anyMatch(inpRole -> inpRole.getRoleName().equals(role.getRoleName()));
                if (anyMatch) {
                    return role;
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            findUser.setRoleList(resultRoleList);
        }else {
            findUser.setRoleList(new ArrayList<>());
        }

        muResponse.write(RespUtil.success("success"));


    }
}
