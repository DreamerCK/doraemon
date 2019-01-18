package com.wanhutong.common;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * (C) Copyright 2017-now
 * All rights reserved.
 * <p>
 * 语音识别处理视图对象
 *
 * @author DreamerCK
 * @date 2019-01-18 10:33
 **/
@Data
@Builder
public class VoiceResultVo {

    private String resultStr;

    private List<String> segWords;

}
