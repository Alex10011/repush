package com.alex10011.repush.amqp.dynamic;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

public class ConsumerSimpleMessageListenerContainer extends SimpleMessageListenerContainer {

    public void startConsumers() throws Exception {
        super.doStart();
    }

}  
