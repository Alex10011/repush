package com.alex10011.repush.startup;

import com.alex10011.repush.service.QueueService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Auther: yl
 * @Date: 2018/6/21 13:38
 * @Description:
 */
//在项目服务启动的时候就去加载一些数据或做一些事情
@Component
@Order(value = 1)
public class InitQueueStartup implements CommandLineRunner {
    private static final Log logger = LogFactory.getLog(InitQueueStartup.class);

    public InitQueueStartup(QueueService queueService) {
        this.queueService = queueService;
    }

    private QueueService queueService;

    @Override
    public void run(String... args) throws Exception {
        //TODO
        //后期增加启动指令对应生成对应客户端参数  java -jar --businessName=test
//        logger.info(">>>>>>>>>>>>>>>服务启动生成队列<<<<<<<<<<<<<");
//        logger.info(">>>>>>>>>>>>>>>消费队列客户端执行完成<<<<<<<<<<<<<");
    }

}
