package com.subaru.maneki.manager.imp;

import java.io.IOException;

import javax.annotation.Resource;
import javax.net.ssl.SSLHandshakeException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.subaru.maneki.dao.CountryDao;
import com.subaru.maneki.manager.CommonManager;
import com.subaru.maneki.model.Country;

@Service("commonService")
public class CommonManagerImp implements CommonManager{
	
	@Resource
	private CountryDao countryDao;
	
	public String getCookieValue(HttpServletRequest request, String cookieName){
		Cookie[] cookies = request.getCookies();
		for (Cookie c: cookies){
			if (c.getName().equals(cookieName)){
				return c.getValue();
			}
		}
		return null;
	}
	
	public String getCountryCodeFromCookie(HttpServletRequest request){
		Cookie[] cookies = request.getCookies();
		for (Cookie c: cookies){
			if (c.getName().equals("country_code")){
				return c.getValue().toUpperCase();
			}
		}
		return "US";
	}
	
	public String getCurrency(HttpServletRequest request){
		String abbr = getCountryCodeFromCookie(request);
		
		Country country = countryDao.selectByAbbr(abbr);
		if (country == null){
			return null;
		}
		
		return country.getCurrencyUnit();
	}
	
	public double calculateCountryPrice(double dollarPrice, double rate){
		return dollarPrice * rate;
	}
	
	public double calculateCountryPrice(double dollarPrice, int countryId){
		
		if (countryId <= 0){
			return 0.0;
		}
		Country country = countryDao.select(countryId);
		return dollarPrice * country.getRate();
	}
	
	public double calculateCountryPrice(double dollarPrice, String abbr){
		if (abbr == null){
			return 0.0;
		}
		
		Country country = countryDao.selectByAbbr(abbr);
		return dollarPrice * country.getRate();
	}
	
	public int computeSaving(double platformPrice, double price){
		
		if(Double.compare(platformPrice, price) < 0){
			System.err.println("ERROR: platform price could not be less than our price");
		}
		return (int)(((platformPrice - price)/platformPrice) * 100);
	}
	
	private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
        // 自定义的恢复策略  
        @Override
        public boolean retryRequest(IOException exception,
                                    int executionCount,
                                    HttpContext context) {
            // 设置恢复策略，在发生异常时候将自动重试3次  
            if (executionCount >= 3) {
                // 如果连接次数超过了最大值则停止重试  
                return false;
            }
            if (exception instanceof NoHttpResponseException) {
                // 如果服务器连接失败重试  
                return true;
            }
            if (exception instanceof SSLHandshakeException) {
                // 不要重试ssl连接异常  
                return false;
            }
            HttpRequest request = (HttpRequest) context
                .getAttribute(
                    ExecutionContext.HTTP_REQUEST);
            boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
            if (!idempotent) {
                // 重试，如果请求是考虑幂等  
                return true;
            }
            return false;
        }
    };

	
	@SuppressWarnings("deprecation")
	public JSONObject jsonRequest(String url, JSONObject json){
		BasicHeader header = new BasicHeader("accept", "application/json");
		//BasicHeader header = null;
		
		DefaultHttpClient client = new DefaultHttpClient();
		client.setHttpRequestRetryHandler(requestRetryHandler);
		
		HttpPost post = new HttpPost(url);
		post.addHeader(HTTP.CONTENT_TYPE, "application/json");
		JSONObject response = null;
		try{
			StringEntity s = new StringEntity(JSON.toJSONString(json), HTTP.UTF_8);
			post.setEntity(s);
			
			if (header != null) {
                post.setHeader(header);
            }
			
			HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity entity = res.getEntity();
				String result = EntityUtils.toString(res.getEntity());
				response = (JSONObject)JSONObject.parse(result);
			}
		}catch(Exception e){
			System.out.println(e);
			throw new RuntimeException(e);
		}
		
		return response;
	}
}
