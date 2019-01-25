package com.wanhutong.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * (C) Copyright 2017-now
 * All rights reserved.
 * <p>
 * 分词工具配置
 *
 * @author DreamerCK
 * @date 2019-01-16 17:12
 **/
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "word")
public class WordSegmentationConfig {

    private String dicPath;
    private String stopWordsPath;
    private String bigRamPath;
    private String testSegWord;
    private String voiceTemp;
    private String wavSuffixName;
    private String mp3SuffixName;

}
