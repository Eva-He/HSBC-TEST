package com.hsbc.task.handler.role;

import com.hsbc.task.cache.RoleTable;
import com.hsbc.task.cache.UserTable;
import com.hsbc.task.entity.Role;
import com.hsbc.task.utils.RespUtil;
import io.muserver.MuRequest;
import io.muserver.MuResponse;
import io.muserver.RouteHandler;

import java.util.List;
import java.util.Map;

/**
 * @author Minhua He
 * @date 2022/9/20 1:43
 * >>>description
 */
public class DeleteRoleHandler implements RouteHandler {
    @Override
    public void handle(MuRequest muRequest, MuResponse muResponse, Map<String, String> map) throws Exception {
        String roleName = muRequest.query().get("roleName");
        List<Role> roleList = RoleTable.roleList;
        Role deleteRole = roleList.stream().filter(role -> roleName.equals(role.getRoleName())).findFirst().orElse(null);
        if (deleteRole == null) {
            muResponse.write(RespUtil.error("role not exits"));
        }
        roleList.remove(deleteRole);

        //remove the role in user
        UserTable.userList.stream().forEach(user -> {
            List<Role> userRoleList = user.getRoleList();
            if (userRoleList != null && userRoleList.size() > 0) {
                userRoleList.remove(deleteRole);
            }
        });

        muResponse.write(RespUtil.success("success"));
    }
}
