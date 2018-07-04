package com.alex10011.repush.amqp;

import java.util.HashMap;
import java.util.Map;

public class AmqpConstant {
    public static Map<String, AmqpRetryBean> AmqpBeans = new HashMap<String, AmqpRetryBean>();

    public static final String DEFAULT_EXCHANGE = "default.exchange";

    public static final String QUEUE_NORMAL_NAME_SUFFIX=".normal";

    public static final String QUEUE_FAIL_NAME_SUFFIX=".fail";
}
