package com.bookstore.scraper.Enum;

import lombok.Getter;

@Getter
public enum FormatEnum {

    PAPERBACK(0, "Paperback"),
    HARDCOVER(1, "Hardcover"),
    KINDLE(2, "Kindle")
    ;

    private int code;
    private String msg;

    FormatEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
