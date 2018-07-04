package com.alex10011.repush.amqp;


public class AmqpRetryBean implements Cloneable {

    private String name;

    private Integer time;

    private AmqpRetryBean next;

    public AmqpRetryBean() {

    }

    public AmqpRetryBean(String name, int time, AmqpRetryBean next) {
        this.name = name;
        this.time = time;
        this.next = next;
    }

    public AmqpRetryBean next() {
        return next;
    }

    public String getName() {
        return name;
    }

    public Integer getTime() {
        return time;
    }

    @Override
    public AmqpRetryBean clone() throws CloneNotSupportedException {
        AmqpRetryBean amqpRetryBean = null;
        if (amqpRetryBean == null) {
            amqpRetryBean = (AmqpRetryBean) super.clone();
        }
        return amqpRetryBean;
    }

}