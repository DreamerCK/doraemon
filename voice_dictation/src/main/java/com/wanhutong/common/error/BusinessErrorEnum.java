package com.wanhutong.common.error;

/**
 * (C) Copyright 2017-now
 * All rights reserved.
 * <p>
 * 业务错误枚举
 *
 * @author DreamerCK
 * @date 2019-01-08 12:13
 **/
public enum BusinessErrorEnum implements CommonError {

    //1xxxx通用错误类型
    PARAMETER_VALIDATION_ERROR(10001, "参数不合法"),
    VOICE_CAN_NOT_UNDERSTAND(10002, "小通没有听清您说的什么，请在说一次吧~"),

    ;

    private int errCode;
    private String errMsg;

    private BusinessErrorEnum(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrorCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
