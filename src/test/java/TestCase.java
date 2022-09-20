import com.alibaba.fastjson.JSON;
import com.hsbc.task.StartUp;
import com.hsbc.task.cache.MiddleWareSimulator;
import com.hsbc.task.cache.RoleTable;
import com.hsbc.task.cache.UserTable;
import com.hsbc.task.entity.Role;
import com.hsbc.task.entity.User;
import com.hsbc.task.entity.base.ResponseBean;
import com.hsbc.task.utils.CommonUtils;
import com.hsbc.task.utils.MyEncryptUtil;
import okhttp3.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Minhua He
 * @date 2022/9/19 23:02
 * >>>description
 */

public class TestCase {

    private static String url = "http://localhost:9002";

    public static final MediaType HTTPJSON  = MediaType.get("application/json; charset=utf-8");

    public static String sessionId;

    public static Random random = new Random();

    @BeforeClass
    /**
     * 1. start up service
     * 2. login with admin
     *      - login only when token valid (2h) and with authenticated account
     *      - if not login, will not able to access the services
     * 3. admin get all authorizations to the APIs
     * 4. other user can only access the APIs related to the role assigned
     */
    public static void init() throws Exception {
        StartUp.main(null);

//        Thread.sleep(500);

        OkHttpClient client = new OkHttpClient();
        User user = new User(null,"admin","1234",null);
        String userJson = JSON.toJSONString(user);
        RequestBody body = RequestBody.create(userJson, HTTPJSON);
        Request request = new Request.Builder()
                .url(url+"/login")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        String string = response.body().string();
        ResponseBean responseBean = JSON.parseObject(string, ResponseBean.class);
        sessionId = responseBean.getMsg();
        System.out.println(responseBean.getMsg());

    }


    /**
     * find all users in userTable
     * @throws Exception
     */

    @Test
    public void testFindUser() throws Exception {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url+"/findUser").addHeader("Cookie","JSESSION="+ sessionId)
                .build();
        Response response = client.newCall(request).execute();
        String string = response.body().string();
        System.out.println(string);
        ResponseBean responseBean = JSON.parseObject(string, ResponseBean.class);
        int code = responseBean.getCode();
        Assert.assertEquals(200,code);
    }


    /**
     * create only if user not exists, no duplicated name allowed
     * @throws IOException
     */

    @Test
    public void testCreateUser() throws IOException {

        List<User> previousUserList = new ArrayList<>(UserTable.userList);

        MyEncryptUtil myEncryptUtil = new MyEncryptUtil();
        String pwd = myEncryptUtil.getEncryofStr("123");
        ArrayList<Role> roleList = new ArrayList<>();
        roleList.add(new Role("4", "read"));

        User user = new User();
        user.setId(CommonUtils.getUUID());
        user.setUsername("aname");
        //failed as the username duplicated
        //user.setUsername("admin");
        user.setPassword(pwd);
        user.setRoleList(roleList);
        String userJson = JSON.toJSONString(user);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(userJson, HTTPJSON);

        Request request = new Request.Builder()
                .url(url+"/addUser").addHeader("Cookie","JSESSION="+ sessionId)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        String string = response.body().string();
        System.out.println(string);
        System.out.println(UserTable.userList);
//        ResponseBean responseBean = JSON.parseObject(string, ResponseBean.class);
//        int code = responseBean.getCode();
        Assert.assertEquals(true,previousUserList.size()<UserTable.userList.size());
    }




    @Test
    public void testDeleteUser() throws IOException {
        User userForDelete = new User(CommonUtils.getUUID(), "user4", "123", null);
        UserTable.userList.add(userForDelete);
        ArrayList<Role> updateRoleList = new ArrayList<>();
        updateRoleList.add(new Role("1", "create"));
        updateRoleList.add(new Role("3", "edit"));
        updateRoleList.add(new Role("4", "read"));
        userForDelete.setRoleList(updateRoleList);

        List<User> previousUserList = new ArrayList<>(UserTable.userList);
        System.out.println(previousUserList);
        String userJson = JSON.toJSONString(userForDelete);
        RequestBody body = RequestBody.create(userJson, HTTPJSON);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url+"/deleteUser").addHeader("Cookie","JSESSION="+ sessionId)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        String string = response.body().string();
        System.out.println(string);
        System.out.println(UserTable.userList);
        Assert.assertEquals(false,previousUserList.size()>UserTable.userList.size());
    }


    /**
     * delete failed if user not exists
     * @throws IOException
     */

    @Test
    public void testDeleteUserFailed() throws Exception {

        List<User> previousUserList = new ArrayList<>(UserTable.userList);

        User user = new User("123", "bname", "123", null);
        String userJson = JSON.toJSONString(user);
        RequestBody body = RequestBody.create(userJson, HTTPJSON);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url+"/deleteUser").addHeader("Cookie","JSESSION="+ sessionId)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        String string = response.body().string();
        System.out.println(string);
        Assert.assertEquals(true,previousUserList.size()==UserTable.userList.size());

    }




    @Test
    public void testFindRole() throws Exception {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url+"/getRoles").addHeader("Cookie","JSESSION="+ sessionId)
                .build();
        Response response = client.newCall(request).execute();
        String string = response.body().string();
        System.out.println(string);
        ResponseBean responseBean = JSON.parseObject(string, ResponseBean.class);
        int code = responseBean.getCode();
        Assert.assertEquals(200,code);

    }
// SHA256:p2QAMXNIC1TJYWeIOttrVc98/R1BUFWu3/LiyKgUfQM


    @Test
    public void testCreateRole() throws Exception {

        List<Role> previousRoleList = new ArrayList<>(RoleTable.roleList);

        Role role = new Role();
        role.setRoleId(CommonUtils.getUUID());
        role.setRoleName("arole");
        //failed as role name duplicated
        //role.setRoleName("create");
        String roleJson = JSON.toJSONString(role);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(roleJson, HTTPJSON);

        Request request = new Request.Builder()
                .url(url+"/addRole").addHeader("Cookie","JSESSION="+ sessionId)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        String string = response.body().string();
        System.out.println(string);
        System.out.println(RoleTable.roleList);
        Assert.assertEquals(true,previousRoleList.size()<RoleTable.roleList.size());

    }



    /**
     * delete role in roleTable and also delete the role associated with the users
     * @throws Exception
     */
    @Test
    public void testDeleteRole() throws Exception {

        List<Role> previousRoleList = new ArrayList<>(RoleTable.roleList);
        List<User> previousUserList = new ArrayList<>(UserTable.userList);
        int previousUserRoleSize = previousUserList.get(0).getRoleList().size();
        System.out.println(RoleTable.roleList);

//        String[] roles = MiddleWareSimulator.config.getProperty("base.authorize").split(",");
//        String roleName = roles[random.nextInt(4)];
        String roleName = "read";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url+"/deleteRole?roleName="+roleName).addHeader("Cookie","JSESSION="+ sessionId)
                .build();
        Response response = client.newCall(request).execute();
        String string = response.body().string();
        System.out.println(string);
        System.out.println(RoleTable.roleList);
        Assert.assertEquals(false,previousRoleList.size() > RoleTable.roleList.size() && previousUserRoleSize > UserTable.userList.get(0).getRoleList().size());
    }





    @Test
    public void testDeleteRoleFailed() throws Exception {

        List<Role> previousRoleList = new ArrayList<>(RoleTable.roleList);
        List<User> previousUserList = new ArrayList<>(UserTable.userList);
        int previousUserRoleSize = previousUserList.get(0).getRoleList().size();
        System.out.println(RoleTable.roleList);

        //role name not exists
        String roleName = "failedrole";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url+"/deleteRole?roleName="+roleName).addHeader("Cookie","JSESSION="+ sessionId)
                .build();
        Response response = client.newCall(request).execute();
        String string = response.body().string();
        System.out.println(string);
        System.out.println(RoleTable.roleList);
        Assert.assertEquals(true,previousRoleList.size() == RoleTable.roleList.size() && previousUserRoleSize == UserTable.userList.get(0).getRoleList().size());

    }





    @Test
    public void testUpdateRoleToUser() throws IOException {
        //User userForUpdate = new User(CommonUtils.getUUID(), "user5", "123", null);
        //UserTable.userList.add(userForUpdate);
        User userForUpdate = UserTable.userList.get(0);
        ArrayList<Role> updateRoleList = new ArrayList<>();
        updateRoleList.add(new Role("1", "create"));
        updateRoleList.add(new Role("3", "edit"));
        updateRoleList.add(new Role("4", "read"));
        userForUpdate.setRoleList(updateRoleList);


        String userJson = JSON.toJSONString(userForUpdate);
        RequestBody body = RequestBody.create(userJson, HTTPJSON);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url+"/updateRoleToUser").addHeader("Cookie","JSESSION="+ sessionId)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        String string = response.body().string();
        System.out.println(string);
        System.out.println(UserTable.userList.get(0).toString());
        ResponseBean responseBean = JSON.parseObject(string, ResponseBean.class);
        int code = responseBean.getCode();
        Assert.assertEquals(200,code);
    }



    /**
     * user only can access the APIs related to the role assigned
     */

    @Test
    public void testAuthorization() throws Exception {
        Thread.sleep(1000);

        //update role to user: read, create, edit, without the role of delete, cannot access the delete**/remove** APIs
        testUpdateRoleToUser();
        User user = UserTable.userList.get(0);
        String userJson = JSON.toJSONString(user);
        RequestBody body = RequestBody.create(userJson, HTTPJSON);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url+"/deleteUser").addHeader("Cookie","JSESSION="+ sessionId)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        String string = response.body().string();
        System.out.println(string);
        System.out.println(UserTable.userList);
        ResponseBean responseBean = JSON.parseObject(string, ResponseBean.class);
        String msg = (String) responseBean.getData();
        Assert.assertEquals("no authorization", msg);

    }

}

