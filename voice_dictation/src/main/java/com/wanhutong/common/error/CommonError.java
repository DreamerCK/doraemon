package com.wanhutong.common.error;

/**
 * (C) Copyright 2017-now
 * All rights reserved.
 * <p>
 * 通用错误模型接口
 *
 * @author DreamerCK
 * @date 2019-01-08 12:11
 **/
public interface CommonError {

    public int getErrorCode();

    public String getErrMsg();

    public void setErrMsg(String errMsg);

}
