package com.bookstore.scraper.Enum;

import lombok.Getter;

@Getter
public enum ResultEnum {
    NotUnique(0,"The result is not unique"),
    Empty(1, "The result is empty"),
    DOC_EMPTY (2, "The doc is empty"),
    Invalid_Double(3, "The input is not a valid double"),
    Invalid_Integer(4, "The input is not a valid integer"),
    Date_Invalid(5, "The input is not a valid date format");
    ;

    private int code;
    private String msg;

    ResultEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
