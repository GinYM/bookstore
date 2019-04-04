package com.bookstore.Exception;

import com.bookstore.enums.ResultEnum;
import lombok.Getter;

@Getter
public class BookException extends Exception {
    private Integer code;
    public BookException(ResultEnum resultEnum){
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }
}
