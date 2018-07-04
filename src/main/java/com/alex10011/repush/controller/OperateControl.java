package com.alex10011.repush.controller;

import com.alex10011.repush.amqp.AmqpConstant;
import com.alex10011.repush.amqp.HttpContent;
import com.alex10011.repush.service.AmqpService;
import com.alex10011.repush.service.QueueService;
import com.alex10011.repush.util.CommonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

@RestController
public class OperateControl {
    private static final Log logger = LogFactory.getLog(OperateControl.class);
    public OperateControl(QueueService queueService, AmqpService amqpService) {
        this.queueService = queueService;
        this.amqpService = amqpService;
    }

    private QueueService queueService;
    private AmqpService amqpService;

    @RequestMapping(value = "/manage/init", method = RequestMethod.GET)
    public String init(@RequestParam("businessName")String businessName) {
        logger.debug("Operate.init businessName:"+businessName);
        try{
            queueService.createQueue(businessName);
        }catch (Exception e){
            logger.error("Error in Operate.init --- businessName:"+businessName,e);
            return "false";
        }
        return "success";
    }

    @RequestMapping(value = "/sendtest", method = RequestMethod.GET)
    public String sendtest() throws Exception {
        System.out.println("Send Message --------------");
        HttpContent httpContent = new HttpContent();
        httpContent.setUrl("http://10.19.106.113:7001");
        httpContent.setType(HttpMethod.GET.name());
        httpContent.setParamsType("json");
        httpContent.setParams("{'id':123,'name':test}");
        amqpService.sendDelay("test.normal", CommonUtil.javaToStr(httpContent), 1000);
//		amqpService.send("test.normal", "lalala");
        return "success";
    }

//    @RequestMapping(value = "/send")
//    @ResponseBody
//    public void send(@RequestBody HttpContent httpContent) {
//        System.out.println("Send Message --------------");
//        amqpService.sendDelay(httpContent.getBusinessName(), CommonUtil.javaToStr(httpContent), 1);
//    }

    @RequestMapping(value = "send", method = RequestMethod.POST)
    @ResponseBody
    // @AccessSign4Outer(responseType = 1)
    public String send(@RequestBody HttpContent httpContent) {
        System.out.println("Send Message --------------");
        amqpService.sendDelay(httpContent.getBusinessName()+AmqpConstant.QUEUE_NORMAL_NAME_SUFFIX, CommonUtil.javaToStr(httpContent), 1);
        return "true";
    }
}
