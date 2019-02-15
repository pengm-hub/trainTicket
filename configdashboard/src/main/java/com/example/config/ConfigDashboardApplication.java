package com.example.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * 配置中心
 * @ EnableConfigServer 启用配置服务
 * @ EnableDiscoveryClient 启用发现客户端服务
 * https://www.jianshu.com/p/276fda4f9cc4
 */
@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class ConfigDashboardApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigDashboardApplication.class, args);
    }
}
