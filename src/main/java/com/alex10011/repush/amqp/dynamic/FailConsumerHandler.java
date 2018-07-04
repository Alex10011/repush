package com.alex10011.repush.amqp.dynamic;

/**
 * @Auther: yl
 * @Date: 2018/6/21 13:54
 * @Description:
 */

import com.google.gson.Gson;
import com.alex10011.repush.amqp.DLXMessage;
import com.alex10011.repush.amqp.HttpContent;
import com.alex10011.repush.constant.RepushConstant;
import com.alex10011.repush.util.CommonFileUtil;

import java.util.Map;

public class FailConsumerHandler {

    public FailConsumerHandler() {

    }

    public void handleMessage(String msg) {
        System.out.println("Fail--------------------------: " + msg);
        DLXMessage dlxMessage = null;
        HttpContent httpContent = null;
        Map<String, String> param = null;
        try {
            //1.消费队列进行解析内容
            dlxMessage = new Gson().fromJson(msg, DLXMessage.class);
            //2.将消息记录保存到数据库内(目前用文件存储)
            CommonFileUtil.writeFileByFileWriter(RepushConstant.RE_PUSH_FAIL_FILE,dlxMessage.toString()+RepushConstant.RE_PUSH_NEW_LING);
        } catch (Exception e) {
            System.out.println("AmqpReceive.processMessage4Message转换对象出错 msg is " + msg);
        }
    }
}  