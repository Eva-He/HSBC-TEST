package config;

import com.alibaba.fastjson.JSON;
import com.hsbc.task.cache.MiddleWareSimulator;
import com.hsbc.task.entity.Role;
import com.hsbc.task.entity.User;
import com.hsbc.task.utils.CommonUtils;
import com.hsbc.task.utils.RespUtil;
import io.muserver.MuHandler;
import io.muserver.MuRequest;
import io.muserver.MuResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author Minhua He
 * @date 2022/9/20 11:17
 * >>>description
 */
public class AuthorizationHandler implements MuHandler {
    @Override
    public boolean handle(MuRequest muRequest, MuResponse muResponse) throws Exception {
        Properties config = MiddleWareSimulator.config;

        String path = muRequest.serverURI().getPath();
        String[] split = path.split("\\.");
        String suffix = split[split.length - 1];

        String[] unfilter = MiddleWareSimulator.config.getProperty("unfilter").split(",");
        if (Arrays.stream(unfilter).anyMatch(x->suffix.equals(x))) {
            return false;
        }

        User loginUser = CommonUtils.getUserByToken(muRequest);
        if (loginUser == null) {
            muResponse.write(RespUtil.error("error"));
            return false;
        }

        List<Role> roleList = loginUser.getRoleList();
        if (roleList == null || roleList.size() == 0) {
            muResponse.status(403);
            muResponse.write(RespUtil.error("no authorization"));
            return true;
        }

        //get api and check related roles' authorization related to user

        //get the apis that user can access
        ArrayList<String> apis = new ArrayList<>();
        roleList.stream().forEach(role -> {
            String roleName = role.getRoleName();
            String property = config.getProperty("auth." + roleName);
            if (StringUtils.isNotEmpty(property)) {
                List<String> apiList = Arrays.stream(property.split(",")).collect(Collectors.toList());
                apis.addAll(apiList);
            }
        });

        String fixedSuffix = suffix.replace("/", "");
        boolean anyMatch = apis.stream().anyMatch(api -> fixedSuffix.startsWith(api));

        if (!anyMatch) {
            muResponse.status(403);
            muResponse.write(RespUtil.error("no authorization"));
            return true;
        }


        return false;
    }
}
