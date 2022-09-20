package com.hsbc.task.entity.base;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseBean<T>{

    private int code;

    private String msg;

    private T data;

}
