package com.alex10011.repush.common;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

//辅助解决返回结果嵌套对象的获取 
//前提是服务提供方采用我们通用的包裹类RspBean的时候可使用，否则自行拆解返回包
public class RestTemplateUtil {
	static Logger log = LoggerFactory.getLogger(RestTemplateUtil.class);

	public static String restTemplatePost(RestTemplate restTemplate, String url, Map<String,String> param,
			MediaType media) {
		log.info("发送参数" + param);

		HttpHeaders headers = new HttpHeaders();
		// 表单 MediaType.APPLICATION_FORM_URLENCODED
		// body中传输寄送 MediaType.APPLICATION_JSON
		headers.setContentType(media);

		HttpEntity requestEntity = null;
		if (media == MediaType.APPLICATION_JSON || media == MediaType.APPLICATION_JSON_UTF8) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			String requestJson = "";
			try {
				requestJson = mapper.writeValueAsString(param);
			} catch (JsonProcessingException e) {
				log.error(e.getMessage(), e);
			}
			requestEntity = new HttpEntity<String>(requestJson, headers);
		} else {
			// 封装参数，不要替换为Map与HashMap，否则参数无法传递
			MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
			if (param != null && param.size() > 0) {
				Set<Entry<String, String>> set = param.entrySet();
				for (Entry<String, String> entry : set) {
					params.add(entry.getKey(), entry.getValue());
				}
			}
			requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		}

		ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
		if (results.getBody() != null) {
			try {
				return results.getBody();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return null;
	}
	
	public static String restTemplatePost(RestTemplate restTemplate, String url, String json) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		HttpHeaders headers = new HttpHeaders();
		// 表单 MediaType.APPLICATION_FORM_URLENCODED
		// body中传输寄送 MediaType.APPLICATION_JSON
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());

		HttpEntity requestEntity = new HttpEntity<String>(json, headers);

		ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
		if (results.getBody() != null) {
			try {
				return results.getBody();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return null;
	}
	
	public static String restTemplateGet(RestTemplate restTemplate, String url, String json) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		HttpHeaders headers = new HttpHeaders();
		// 表单 MediaType.APPLICATION_FORM_URLENCODED
		// body中传输寄送 MediaType.APPLICATION_JSON
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());

		HttpEntity requestEntity = new HttpEntity<String>(json, headers);

		ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
		if (results.getBody() != null) {
			try {
				return results.getBody();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return null;
	}
}
