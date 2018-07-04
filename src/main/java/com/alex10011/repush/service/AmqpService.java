package com.alex10011.repush.service;


import org.springframework.amqp.rabbit.core.RabbitAdmin;

public interface AmqpService {

    /**
     * 延迟发送消息到队列
     *
     * @param queue   队列名称
     * @param message 消息内容
     * @param times   延迟时间 单位毫秒
     */
    public void sendDelay(String queueName, String msg, final long times);

    /**
     * 延迟发送消息到队列
     *
     * @param queue   队列名称
     * @param message 消息内容
     * @param times   延迟时间 单位毫秒
     */
    public void send(String queueName, String msg);

    /**
     * 创建生产者队列
     *
     * @param queueName
     * @return
     */
    public boolean createProducer(String queueName, RabbitAdmin admin, String normalQueue);

    /**
     * 创建成功消费者
     *
     * @param queueName
     * @return
     */
    public boolean createCustomer(String queueName,Object object) throws Exception;

}
