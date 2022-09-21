package com.hsbc.task;

import com.hsbc.task.cache.MiddleWareSimulator;
import com.hsbc.task.cache.RoleTable;
import com.hsbc.task.cache.UserTable;
import com.hsbc.task.entity.Role;
import com.hsbc.task.entity.User;
import com.hsbc.task.handler.role.AddRoleHandler;
import com.hsbc.task.handler.role.DeleteRoleHandler;
import com.hsbc.task.handler.role.FindRolesHandler;
import com.hsbc.task.handler.user.*;
import com.hsbc.task.utils.MyEncryptUtil;
import com.hsbc.task.utils.RespUtil;
import com.hsbc.task.config.AuthenticationHandler;
import com.hsbc.task.config.AuthorizationHandler;
import io.muserver.Method;
import io.muserver.MuServer;
import io.muserver.MuServerBuilder;
import io.muserver.handlers.ResourceHandlerBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Minhua He
 * @date 2022/9/18 23:50
 * >>>description
 */
public class StartUp {

    private static int port;

    public static void main(String[] args) throws Exception {
         init();
        MuServer server = MuServerBuilder.httpServer().withHttpPort(port)
                .addHandler(new AuthenticationHandler())
               .addHandler(new AuthorizationHandler())
                .addHandler(Method.GET, "/", (request, response, pathParams) -> {
                    response.write(RespUtil.success(UserTable.userList));
                })
                //user
                .addHandler(Method.POST, "/login", new LoginHandler())
                .addHandler(Method.POST, "/addUser", new AddUserHandler())
                .addHandler(Method.GET, "/findUser", new FindUserHandler())
                .addHandler(Method.POST, "/deleteUser", new DeleteUserHandler())
                .addHandler(Method.GET, "/findUserById", new FindUserByIdHandler())
                .addHandler(Method.POST, "/updateRoleToUser", new UpdateRoleToUserHandler())
                .addHandler(Method.POST,"/checkLogin",new CheckLoginHandler())

                //role
                .addHandler(Method.GET, "/getRoles", new FindRolesHandler())
                .addHandler(Method.POST, "/addRole", new AddRoleHandler())
                .addHandler(Method.GET, "/deleteRole", new DeleteRoleHandler())

                .addHandler(ResourceHandlerBuilder.fileOrClasspath("src/main/resources/static", "/static"))
                .start();
        System.out.println("Started server at " + server.uri());

    }

    private static void init() throws IOException {
        InputStream systemResourceAsStream = ClassLoader.getSystemResourceAsStream("config.properties");

        Properties config = MiddleWareSimulator.config;
        config.load(systemResourceAsStream);
        port = Integer.valueOf(config.getProperty("port"));


        String[] roleNameList = config.getProperty("base.authorize").split(",");
        for (int i = 0; i < roleNameList.length; i++) {
            RoleTable.roleList.add(new Role(String.valueOf(i + 1), roleNameList[i]));
        }

        MyEncryptUtil myEncryptUtil = new MyEncryptUtil();
        List<User> userList = UserTable.userList;

        ArrayList<Role> roles = new ArrayList<>( RoleTable.roleList);
        User admin = new User("1", config.getProperty("admin.username"), myEncryptUtil.getEncryofStr(config.getProperty("admin.password")), roles);
        userList.add(admin);

    }
}
