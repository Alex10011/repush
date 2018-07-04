package com.alex10011.repush.service;

import com.alex10011.repush.amqp.AmqpRetryBean;
import com.alex10011.repush.exception.RepushConfigException;

public interface RepushServcie {

    AmqpRetryBean createListByBusinessName(String businessName) throws RepushConfigException;

    AmqpRetryBean createList(String businessName, String nameStr, String timeStr) throws RepushConfigException;

}
