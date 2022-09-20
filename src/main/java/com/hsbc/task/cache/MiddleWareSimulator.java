package com.hsbc.task.cache;

import com.hsbc.task.entity.User;

import java.util.HashMap;
import java.util.Properties;

/**
 * @author Minhua He
 * @date 2022/9/19 18:24
 * >>>description
 */
public class MiddleWareSimulator {

    public static HashMap<String, User> userMap = new HashMap<>();

    public static HashMap<String, String> sessionMap = new HashMap<>();

    public static Properties config = new Properties();


}
