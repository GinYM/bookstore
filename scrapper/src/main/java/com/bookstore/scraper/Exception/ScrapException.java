package com.bookstore.scraper.Exception;

import com.bookstore.scraper.Enum.ResultEnum;
import lombok.Getter;

@Getter
public class ScrapException extends Exception {
    private int code;
    private String msg;

    public ScrapException(ResultEnum resultEnum){
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
    }

}
