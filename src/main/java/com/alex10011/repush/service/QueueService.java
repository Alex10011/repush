package com.alex10011.repush.service;

public interface QueueService {

    void createQueue(String businessName) throws Exception;

    boolean createNormalCustomerQueue(String queueName) throws Exception;

    boolean createFailCustomerQueue(String queueName) throws Exception;

}
