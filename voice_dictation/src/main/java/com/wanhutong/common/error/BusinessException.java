package com.wanhutong.common.error;

/**
 * (C) Copyright 2017-now
 * All rights reserved.
 * <p>
 * 业务异常
 *
 * @author DreamerCK
 * @date 2019-01-08 12:21
 **/
public class BusinessException extends Exception implements CommonError {

    private CommonError commonError;

    /**
     * 直接接收BusinessErrorEnum的传参用于构造业务异常
     */
    public BusinessException(CommonError commonError) {
        super();
        this.commonError = commonError;
    }

    /**
     * 接收自定义errMsg的方式构造业务异常
     */
    public BusinessException(CommonError commonError, String errMsg) {
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }

    @Override
    public int getErrorCode() {
        return this.commonError.getErrorCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public void setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
    }
}
