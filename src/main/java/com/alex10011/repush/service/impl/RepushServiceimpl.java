package com.alex10011.repush.service.impl;

import com.alex10011.repush.amqp.AmqpConstant;
import com.alex10011.repush.amqp.AmqpRetryBean;
import com.alex10011.repush.exception.RepushConfigException;
import com.alex10011.repush.service.RepushServcie;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RepushServiceimpl implements RepushServcie {
    private static final Log logger = LogFactory.getLog(RepushServiceimpl.class);

    @Value("${repush.queue.template.name}")
    String templateNames;

    @Value("${repush.queue.template.time}")
    String templateTimes;

    /**
     * 根据模板内容创建对应的队列对象
     *
     * @param businessName
     * @return
     * @throws RepushConfigException
     */
    public AmqpRetryBean createListByBusinessName(String businessName) throws RepushConfigException {
        return createList(businessName, templateNames, templateTimes);
    }

    public AmqpRetryBean createList(String businessName, String nameStr, String timeStr) throws RepushConfigException {
        String[] nameArray = nameStr.split(",");
        String[] timeArray = timeStr.split(",");
        if (nameArray.length != timeArray.length) {
            //配置文件配置内容数目有误
            throw new RepushConfigException("创建队列对象出错：配置文件配置内容数目有误，请检查配置文件 ");
        }
        AmqpRetryBean nextOne = new AmqpRetryBean(businessName + AmqpConstant.QUEUE_FAIL_NAME_SUFFIX, 900000, null);
        for (int i = nameArray.length - 1; i >= 0; i--) {
            String name = businessName + "." + nameArray[i];
            int time = 0;
            try {
                time = Integer.valueOf(timeArray[i].trim());
            } catch (NumberFormatException e) {
                throw new RepushConfigException("创建队列对象出错：timeStr:" + timeStr, e);
            }
            AmqpRetryBean arb = new AmqpRetryBean(name, time, nextOne);
            nextOne = arb;
        }
        return new AmqpRetryBean(businessName + AmqpConstant.QUEUE_NORMAL_NAME_SUFFIX, 1, nextOne);
    }

}
