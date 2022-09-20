package com.hsbc.task.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hsbc.task.entity.base.ResponseBean;

public class RespUtil {

    public static <T> String success(T data) {

        ResponseBean<T> responseBean = new ResponseBean<>(200, "SUCC", data);

        String jsonString = JSON.toJSONString(responseBean, SerializerFeature.DisableCircularReferenceDetect);

        return jsonString;
    }

    public static <T> String error(T data) {

        ResponseBean<T> responseBean = new ResponseBean<>(500, "ERROR", data);

        String jsonString = JSON.toJSONString(responseBean);

        return jsonString;
    }

    public static <T> String response(T data, int code, String msg) {

        ResponseBean<T> responseBean = new ResponseBean<>(code, msg, data);

        String jsonString = JSON.toJSONString(responseBean);

        return jsonString;
    }
}
