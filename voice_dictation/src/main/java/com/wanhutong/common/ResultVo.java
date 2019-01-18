package com.wanhutong.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wanhutong.common.error.BusinessErrorEnum;
import lombok.Data;

/**
 * (C) Copyright 2017-now
 * All rights reserved.
 * <p>
 * 统一结果返回模型
 *
 * @author DreamerCK
 * @date 2019-01-08 11:49
 **/
@Data
public class ResultVo<T> {

    private Byte status;

    private T data;

    private Integer errorCode;

    private String msg;

    private static final Byte SUCCESS_STATUS = 1;

    private static final Byte ERROR_STATUS = 0;

    private ResultVo() {
    }

    private ResultVo(Byte status) {
        this.status = status;
    }

    private ResultVo(Byte status, T data) {
        this.status = status;
        this.data = data;
    }

    private ResultVo(Byte status, Integer errorCode, String msg) {
        this.status = status;
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public ResultVo createBySuccess() {
        return new ResultVo(SUCCESS_STATUS);
    }

    @JsonIgnore
    public boolean isSuccess() {
        return SUCCESS_STATUS.equals(this.status);
    }

    @SuppressWarnings("unchecked")
    public static <T> ResultVo<T> createBySuccessData(T data) {
        return new ResultVo(SUCCESS_STATUS, data);
    }

    public static ResultVo createByError(Integer errorCode, String msg) {
        return new ResultVo(ERROR_STATUS, errorCode, msg);
    }

    public static ResultVo createByErrorEnum(BusinessErrorEnum resultEnum) {
        return new ResultVo(ERROR_STATUS, resultEnum.getErrorCode(), resultEnum.getErrMsg());
    }

}
