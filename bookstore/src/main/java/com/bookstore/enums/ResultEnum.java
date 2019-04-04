package com.bookstore.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
    LOCK_ERROR(1, "lock error")
    ;
    private int code;
    private String msg;

    private ResultEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
