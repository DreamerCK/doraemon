package com.wanhutong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * (C) Copyright 2017-now
 * All rights reserved.
 * <p>
 * 音视频转换启动类
 *
 * @author DreamerCK
 * @date 2018-11-29 11:10
 **/
@EnableEurekaClient
@SpringBootApplication
public class VoiceDictationApplication {

    public static void main(String[] args) {
        SpringApplication.run(VoiceDictationApplication.class, args);
    }

}
