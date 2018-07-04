package com.alex10011.repush;

import com.alex10011.repush.util.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.alex10011.repush.*")//包名
public class RetryApplication {

    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(RetryApplication.class, args);
        SpringUtil.setApplicationContext(app);
        System.out.println("ヾ(◍°∇°◍)ﾉﾞv over~~");
    }
}
