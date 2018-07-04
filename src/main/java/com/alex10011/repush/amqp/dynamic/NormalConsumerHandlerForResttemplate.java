package com.alex10011.repush.amqp.dynamic;

import com.google.gson.Gson;
import com.alex10011.repush.amqp.AmqpConstant;
import com.alex10011.repush.amqp.AmqpRetryBean;
import com.alex10011.repush.amqp.DLXMessage;
import com.alex10011.repush.amqp.HttpContent;
import com.alex10011.repush.common.RestTemplateUtil;
import com.alex10011.repush.constant.RepushConstant;
import com.alex10011.repush.service.AmqpService;
import com.alex10011.repush.service.impl.AmqpServiceImpl;
import com.alex10011.repush.util.CommonFileUtil;
import com.alex10011.repush.util.CommonUtil;
import com.alex10011.repush.util.SpringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class NormalConsumerHandlerForResttemplate {
    private static final Log logger = LogFactory.getLog(AmqpServiceImpl.class);

    public void handleMessage(String msg) {
        DLXMessage dlxMessage = null;
        HttpContent httpContent = null;
        Map<String, String> param = null;
        try {
            //1.消费队列进行解析内容并且发送数据
            dlxMessage = new Gson().fromJson(msg, DLXMessage.class);
            //2.处理业务逻辑消息发送成功
            String content = dlxMessage.getContent();
            httpContent = CommonUtil.strToJava(content);
            param = CommonUtil.strToMap(httpContent.getParams());
        } catch (Exception e) {
            logger.error("AmqpReceive.processMessage4Message转换对象出错 msg is " + msg,e);
            CommonFileUtil.writeFileByFileWriter(RepushConstant.RE_PUSH_FAIL_FILE,dlxMessage.toString()+RepushConstant.RE_PUSH_NEW_LING);
        }
        try {
            logger.info("请求发送内容地址："+httpContent.getUrl());
            logger.info("请求发送内容："+param.toString());
            //3.调用存储服务消息推送内容服务
            RestTemplate restTemplate = (RestTemplate) SpringUtil.getBean(RestTemplate.class);
            String result = RestTemplateUtil.restTemplatePost(restTemplate, httpContent.getUrl(), param, MediaType.APPLICATION_JSON);
            logger.info("请求返回结果"+result);
        } catch (Exception ex) {
            logger.debug(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "----------------");
            //4.消息处理失败，进一步推送至下一个死信节点
            String queueName = dlxMessage.getQueueName();
            AmqpRetryBean next = AmqpConstant.AmqpBeans.get(queueName).next();
            dlxMessage.setQueueName(next.getName());
            AmqpService amqpService = (AmqpService) SpringUtil.getBean(AmqpService.class);
            amqpService.sendDelay(next.getName(), dlxMessage.getContent(), next.getTime() * 1000);
            logger.debug(Thread.currentThread().getName() + "处理请求失败推送消息至下一节点内容为：" + dlxMessage.toString());
        }
    }
}  