package com.hsbc.task.handler.user;

import com.alibaba.fastjson.JSON;
import com.hsbc.task.cache.UserTable;
import com.hsbc.task.entity.Role;
import com.hsbc.task.entity.User;
import com.hsbc.task.utils.CommonUtils;
import com.hsbc.task.utils.MyEncryptUtil;
import com.hsbc.task.utils.RespUtil;
import io.muserver.MuRequest;
import io.muserver.MuResponse;
import io.muserver.RouteHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author Minhua He
 * @date 2022/9/19 20:47
 * >>>description
 */
@Slf4j
public class AddUserHandler implements RouteHandler {
    @Override
    public void handle(MuRequest request, MuResponse response, Map<String, String> map) throws Exception {
        String requestBody = request.readBodyAsString();
        User inputUser = JSON.parseObject(requestBody, User.class);

        String username = inputUser.getUsername();
        if (StringUtils.isEmpty(username)) {
            String msg = "user name can not be empty";
            String error = RespUtil.response(null,500, msg);
            log.info(msg);
            response.write(error);
            return;
        }

        boolean anyMatch = UserTable.userList.stream().anyMatch(user -> user.getUsername().equals(username));
        if (anyMatch) {
            response.write(RespUtil.error("user exits"));
            return;
        }

        String password = inputUser.getPassword();
        MyEncryptUtil myEncryptUtil = new MyEncryptUtil();
        password = myEncryptUtil.getEncryofStr(password);
        List<Role> roleList = inputUser.getRoleList();

        User newUser = new User();
        newUser.setId(CommonUtils.getUUID());
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setRoleList(roleList);
        UserTable.userList.add(newUser);
        log.info("add user succeed");
        response.write(RespUtil.success("success"));
    }
}
