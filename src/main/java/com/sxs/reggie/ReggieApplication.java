package com.sxs.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author sxs
 * @create 2022-08-19 12:10
 */
@Slf4j //TODO lombok他提供的注解 方便在代码中加日志 然后在控制台输出
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class, args);
        log.info("启动成功");
    }
}
