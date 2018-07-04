package com.alex10011.repush.amqp;

import java.io.Serializable;

public class HttpContent implements Serializable {

    /**
     * 请求类型
     */
    private String type;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 参数内容, 请求参数为map格式，通过json转string
     */
    private String params;

    /**
     * 参数类型 默认为json
     */
    private String paramsType = "json";

    private String businessName;

    public HttpContent() {

    }

    public HttpContent(String url, String type, String paramsType, String params, String business) {
        this.url = url;
        this.type = type;
        this.paramsType = paramsType;
        this.params = params;
        this.businessName = business;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getParamsType() {
        return paramsType;
    }

    public void setParamsType(String paramsType) {
        this.paramsType = paramsType;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    @Override
    public String toString() {
        return "HttpContent{" +
                "type=" + type +
                ", url='" + url + '\'' +
                ", params='" + params + '\'' +
                ", paramsType='" + paramsType + '\'' +
                ", businessName='" + businessName + '\'' +
                '}';
    }
}
