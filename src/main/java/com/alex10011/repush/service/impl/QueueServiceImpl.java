package com.alex10011.repush.service.impl;

import com.alex10011.repush.amqp.AmqpConstant;
import com.alex10011.repush.amqp.AmqpRetryBean;
import com.alex10011.repush.amqp.dynamic.FailConsumerHandler;
import com.alex10011.repush.amqp.dynamic.NormalConsumerHandler;
import com.alex10011.repush.constant.RepushConstant;
import com.alex10011.repush.service.AmqpService;
import com.alex10011.repush.service.QueueService;
import com.alex10011.repush.service.RepushServcie;
import com.alex10011.repush.util.CommonFileUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class QueueServiceImpl implements QueueService {
    private static final Log logger = LogFactory.getLog(QueueServiceImpl.class);

    public QueueServiceImpl(RabbitTemplate rabbitTemplate, AmqpService amqpService, RepushServcie repushServcie) {
        this.rabbitTemplate = rabbitTemplate;
        this.amqpService = amqpService;
        this.repushServcie = repushServcie;
    }

    private RabbitTemplate rabbitTemplate;
    private AmqpService amqpService;
    private RepushServcie repushServcie;

    /**
     * 增加队列
     */
    public void createQueue(String businessName) throws Exception {
        String normalQueueName = businessName + AmqpConstant.QUEUE_NORMAL_NAME_SUFFIX;
        String failQueueName = businessName + AmqpConstant.QUEUE_FAIL_NAME_SUFFIX;
        //增加交换机
        RabbitAdmin admin = new RabbitAdmin(this.rabbitTemplate.getConnectionFactory());
        admin.declareExchange(new TopicExchange(AmqpConstant.DEFAULT_EXCHANGE));
        //根据businessName生成对应的延迟队列组
        AmqpRetryBean lastBean = repushServcie.createListByBusinessName(businessName);
        while (lastBean != null) {
            if (!amqpService.createProducer(lastBean.getName(), admin, normalQueueName)) {
                logger.error("Error in createProducer" + lastBean.getName());
            }
            admin.declareBinding(new Binding(lastBean.getName(), Binding.DestinationType.QUEUE, AmqpConstant.DEFAULT_EXCHANGE, lastBean.getName(), null));
            AmqpConstant.AmqpBeans.put(lastBean.getName(), lastBean);
            lastBean = lastBean.next();
        }
        //根据businessName生成对应的消费客户端
        if (!amqpService.createCustomer(normalQueueName, new NormalConsumerHandler()))
            logger.error("Error in createCustomer" + businessName);
        if (!amqpService.createCustomer(failQueueName, new FailConsumerHandler()))
            logger.error("Error in createCustomer" + businessName);

        //业务名称写入对应存储文件内
        if (!CommonFileUtil.isExistFile(RepushConstant.RE_PUSH_FILE_PID_NAME))
            CommonFileUtil.createFile(RepushConstant.RE_PUSH_FILE_PID_NAME);
        CommonFileUtil.writeFile(RepushConstant.RE_PUSH_FILE_PID_NAME, businessName + RepushConstant.RE_PUSH_NEW_LING);
    }

    public boolean createNormalCustomerQueue(String queueName) throws Exception {
        return amqpService.createCustomer(queueName, new NormalConsumerHandler());
    }

    public boolean createFailCustomerQueue(String queueName) throws Exception {
        return amqpService.createCustomer(queueName, new FailConsumerHandler());
    }

    /**
     * @param queueName
     * @return Queue
     */
    private Queue retryLetterQueue(String queueName) {
        Map<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("x-dead-letter-exchange", AmqpConstant.DEFAULT_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", queueName);
        return new Queue(queueName, true, false, false, arguments);
    }
}
