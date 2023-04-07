package com.itjy.reggie.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
@Data
public class R<T> {
    private Integer code; //1成功 0失败
    private String msg;
    private T data;
    private Map map = new HashMap();

    public R() {
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public static <T> R<T> success(T object) {
        R r = new R();
        r.data = object;
        r.code = 1;
        return r;
    }


}
