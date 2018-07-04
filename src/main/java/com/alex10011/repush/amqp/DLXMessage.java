package com.alex10011.repush.amqp;

import java.io.Serializable;

/**
 * 死信消息载体
 *
 * @author "yl"
 */
public class DLXMessage implements Serializable {

    private static final long serialVersionUID = -6760551478638875492L;

    public DLXMessage() {
        super();
    }

    public DLXMessage(String queueName, String content) {
        super();
        this.queueName = queueName;
        this.content = content;
    }

    public DLXMessage(String queueName, String content, long times) {
        super();
        this.queueName = queueName;
        this.content = content;
        this.times = times;
    }

    public DLXMessage(String exchange, String queueName, String content, long times) {
        super();
        this.exchange = exchange;
        this.queueName = queueName;
        this.content = content;
        this.times = times;
    }

    private String exchange;

    private String queueName;

    private String content;

    private long times;


    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimes() {
        return times;
    }

    public void setTimes(long times) {
        this.times = times;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    @Override
    public String toString() {
        return "DLXMessage{" +
                "exchange='" + exchange + '\'' +
                ", queueName='" + queueName + '\'' +
                ", content='" + content + '\'' +
                ", times=" + times +
                '}';
    }
}
