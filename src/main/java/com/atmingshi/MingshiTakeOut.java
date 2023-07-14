package com.atmingshi;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author yang
 * @create 2023-07-11 15:51
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "com.atmingshi")
@MapperScan("com.atmingshi.mapper")
@ServletComponentScan//原生 servlet 注入
public class MingshiTakeOut {
    public static void main(String[] args) {
        SpringApplication.run(MingshiTakeOut.class,args);
        log.info("启动成功");
    }
}
