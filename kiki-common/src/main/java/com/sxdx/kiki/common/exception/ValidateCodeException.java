package com.sxdx.kiki.common.exception;

import java.io.Serializable;

/*
 * 验证码类型异常
 */
public class ValidateCodeException extends Exception implements Serializable {

    private static final long serialVersionUID = 2442897438854140377L;

    public ValidateCodeException(String message){
        super(message);
    }
}