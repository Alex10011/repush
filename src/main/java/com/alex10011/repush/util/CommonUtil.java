package com.alex10011.repush.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.alex10011.repush.amqp.HttpContent;

public class CommonUtil {
    private static final Log logger = LogFactory.getLog(CommonUtil.class);
    /**
     * String 转对象
     *
     * @param content
     * @return
     */
    public static HttpContent strToJava(String content) {
        return JSONObject.parseObject(content, HttpContent.class);
    }

    /**
     * String 转对象
     *
     * @param content
     * @return
     */
    public static String javaToStr(HttpContent httpContent) {
        return JSONObject.toJSONString(httpContent);
    }

    public static String mapToStr(Map<String, String> map) {
        return new Gson().toJson(map).toString();
    }

    public static Map<String, String> strToMap(String str) {
        Map<String, String> param = new HashMap<String, String>();
        return new Gson().fromJson(str, param.getClass());
    }

    /**
     * 获取利用反射获取类里面的值和名称
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, String> objectToMap(Object obj) {
        Map<String, String> map = new HashMap<String, String>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = null;
            try {
                value = field.get(obj);
            } catch (Exception e) {
				logger.error(e.getMessage(), e);
            }
            if (value != null && !"".equals(value.toString().trim()))
                map.put(fieldName, value.toString());
            // map.put(fieldName, value != null ? value.toString() : "");
        }
        return map;
    }

    /**
     * 以行的方式写文件；
     */
    public static void writeFile(List objs, File file, List<String> infos) {
        DataOutputStream outs = null;
        try {
            outs = new DataOutputStream(new FileOutputStream(file));
            for (String str : infos) {
                outs.write(str.getBytes("utf-8"));
                outs.write("\n".getBytes());
            }

            Iterator iter = objs.iterator();
            while (iter.hasNext()) {
                outs.write(iter.next().toString().getBytes("utf-8"));
                outs.write("\n".getBytes());
            }
        } catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
        } finally {
            if (outs != null) {
                try {
                    outs.close();
                } catch (IOException e) {
					logger.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 字符串改set（可实现去重效果）
     *
     * @param info
     * @param seed
     * @return
     */
    public static Set<String> string2Set(String info, String seed) {
        Set<String> set = new HashSet<String>();
        if (!StringUtils.isEmpty(info)) {
            String[] menus = info.split(seed);
            for (String str : menus) {
                set.add(str);
            }
        }
        return set;
    }
    public static void main(String[] args) throws Exception {
//		Vo_Outer_Zc_Req_Cancel_Bank vo = new Vo_Outer_Zc_Req_Cancel_Bank();
//		System.out.println(objectToMap(vo));
        // HttpContent httpContent=new HttpContent("http://127.0.0.1"
        // ,HttpMethod.POST,"json","lalalal");
        // String result=javaToStr(httpContent);
        // System.out.println(result);
        // HttpContent lala=strToJava(result);
        // System.out.println(lala.toString());
        //
        // Map<String,String> param=new HashMap<String,String>();
        // param.put("111", "111111");
        // param.put("222", "222222");
        // String reuString=mapToStr(param);
        // System.out.println(reuString);
        // Map<String,String> temp=strToMap(reuString);
        // System.out.println(temp.toString());

        System.out.println((int) (Math.random() * 10000));

    }
}
