package com.wanhutong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * (C) Copyright 2017-now
 * All rights reserved.
 * <p>
 * 服务中心启动类
 *
 * @author DreamerCK
 * @date 2018-11-29 10:52
 **/
@EnableEurekaServer
@SpringBootApplication
public class ServerCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerCenterApplication.class, args);
    }

}
