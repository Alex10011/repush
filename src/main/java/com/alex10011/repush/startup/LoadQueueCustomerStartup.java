package com.alex10011.repush.startup;

import com.alex10011.repush.amqp.AmqpConstant;
import com.alex10011.repush.amqp.AmqpRetryBean;
import com.alex10011.repush.constant.RepushConstant;
import com.alex10011.repush.service.QueueService;
import com.alex10011.repush.service.RepushServcie;
import com.alex10011.repush.util.CommonFileUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @Auther: yl
 * @Date: 2018/6/21 13:12
 * @Description:
 */
//在项目服务启动的时候就去加载一些数据或做一些事情
@Component
@Order(value = 2)
public class LoadQueueCustomerStartup implements CommandLineRunner {
    private static final Log logger = LogFactory.getLog(LoadQueueCustomerStartup.class);

    public LoadQueueCustomerStartup(QueueService queueService,RepushServcie repushServcie) {
        this.queueService = queueService;
        this.repushServcie=repushServcie;
    }

    private QueueService queueService;
    private RepushServcie repushServcie;

    @Override
    public void run(String... args) throws Exception {
        logger.info(">>>>>>>>>>>>>>>服务启动执行，执行生成消费队列客户端<<<<<<<<<<<<<");
        if (!CommonFileUtil.isExistFile(RepushConstant.RE_PUSH_FILE_PID_NAME)) {
            logger.info("pid文件不存在，不需加载消费队列客户端 file：" + RepushConstant.RE_PUSH_FILE_PID_NAME);
            return;
        }
        //获取已经生成的业务名称，并且生成对应的消费队列客户端
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(RepushConstant.RE_PUSH_FILE_PID_NAME));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                //读取出每个行并且针对每行进行生成Normal客户端
                queueService.createNormalCustomerQueue(tempString + AmqpConstant.QUEUE_NORMAL_NAME_SUFFIX);
                logger.info("-----------------------------------------------------------");
                logger.info("--------------------" + tempString + " normal生成完毕--------------------");
                //读取出每个行并且针对每行进行生成Fail客户端
                queueService.createFailCustomerQueue(tempString + AmqpConstant.QUEUE_FAIL_NAME_SUFFIX);
                logger.info("--------------------" + tempString + " fail生成完毕--------------------");
                //生成对应的AmqpConstant.AmqpBeans
                AmqpRetryBean lastBean = repushServcie.createListByBusinessName(tempString);
                while (lastBean != null) {
                    AmqpConstant.AmqpBeans.put(lastBean.getName(), lastBean);
                    lastBean = lastBean.next();
                }
            }
            reader.close();
        } catch (IOException e) {
            logger.error("执行生成消费队列客户端错误！", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    logger.error("执行生成消费队列客户端关闭reader错误！", e1);
                }
            }
        }
        logger.info(">>>>>>>>>>>>>>>消费队列客户端执行完成<<<<<<<<<<<<<");
    }

}