package com.alex10011.repush.service.impl;

import com.google.gson.Gson;
import com.alex10011.repush.amqp.AmqpConstant;
import com.alex10011.repush.amqp.DLXMessage;
import com.alex10011.repush.amqp.dynamic.ConsumerSimpleMessageListenerContainer;
import com.alex10011.repush.service.AmqpService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AmqpServiceImpl implements AmqpService {
    private static final Log logger = LogFactory.getLog(AmqpServiceImpl.class);

    @Autowired
    public AmqpServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    private RabbitTemplate rabbitTemplate;

    /**
     * 创建生产者定义延迟队列，exchange
     */
    public boolean createProducer(String queueName, RabbitAdmin admin, String normalQueue) {
        Queue queue = retryLetterQueue(queueName, normalQueue);
        admin.declareQueue(queue);
        return true;
    }

    /**
     * 创建消费者内容
     */
    public boolean createCustomer(String queueName,Object object) throws Exception {
        ConsumerSimpleMessageListenerContainer container = new ConsumerSimpleMessageListenerContainer();
        container.setConnectionFactory(rabbitTemplate.getConnectionFactory());
        container.setQueueNames(queueName);
        container.setConcurrentConsumers(1);
        container.setMessageListener(new MessageListenerAdapter(object, "handleMessage"));
        container.startConsumers();
        return true;
    }

    /**
     * 生成对应的延迟队列
     *
     * @param queueName
     * @return
     */
    private Queue retryLetterQueue(String queueName, String normalQueue) {
        Map<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("x-dead-letter-exchange", AmqpConstant.DEFAULT_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", normalQueue);
        return new Queue(queueName, true, false, false, arguments);
    }

    /**
     * 发送延迟队列消息
     *
     * @param queueName
     * @param msg
     * @param times
     * @return
     */
    public void sendDelay(String queueName, String msg, final long times) {
        DLXMessage dlxMessage = new DLXMessage(queueName, msg, times);
        MessagePostProcessor processor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration(String.valueOf(times));
                return message;
            }
        };
        dlxMessage.setExchange(AmqpConstant.DEFAULT_EXCHANGE);
        rabbitTemplate.convertAndSend(AmqpConstant.DEFAULT_EXCHANGE, queueName, new Gson().toJson(dlxMessage), processor);
    }

    /**
     * 发送队列消息
     *
     * @param queueName
     * @param msg
     * @return
     */
    public void send(String queueName, String msg) {
        rabbitTemplate.convertAndSend(AmqpConstant.DEFAULT_EXCHANGE, queueName, msg);
    }

}
