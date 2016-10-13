package com.subaru.maneki.service.user.imp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;
import com.subaru.maneki.service.user.OAuthService;
import com.subaru.common.http.HttpClientUtil;
import com.subaru.maneki.vo.FacebookOauthInfoVO;

import org.apache.http.HttpHost;

/**
 * @author zhangchaojie
 * @since 2016-08-12
 */
@Service("facebookOAuthService")
public class FacebookOAuthServiceImp implements OAuthService {

    private static String GET_ACCOUNTNAME_URL = "https://graph.facebook.com/me?fields=name,email";

    private static String GET_OAUTHUID_URL    = "";
    
    private String facebookRequest(String accessToken){
    	List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
    	
        paramsList.add(new BasicNameValuePair("access_token", accessToken));
        paramsList.add(new BasicNameValuePair("fields", "name,email"));
        String result = null;
        
//        String httphost = "salmon.3.14159.in";
//        int port = 17089;
//        HttpHost proxy = new HttpHost(httphost, port);
        HttpHost proxy = null;
        result = HttpClientUtil.get(GET_ACCOUNTNAME_URL, paramsList, proxy);
        result = HttpClientUtil.httpRequest(GET_ACCOUNTNAME_URL, paramsList, "GET", proxy, null, null);
//        System.out.println(result);
        
        return result;
    }

    @Override
    public String getAccountName(String accessToken) {

        if (StringUtils.isBlank(accessToken)) {
            return null;
        }

//        HttpBuilder builder = new HttpBuilder(GET_ACCOUNTNAME_URL);
//        builder.addParam("access_token", accessToken);
//        String result = builder.get();
        
        String result = this.facebookRequest(accessToken);

        return parseAccountName(result);

    }

    @Override
    public String getOAuthUid(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            return null;
        }

        String result = this.facebookRequest(accessToken);

        return parseOAuthUid(result);
    }
    
    @Override
    public FacebookOauthInfoVO getOAuthInfo(String accessToken){
    	if (StringUtils.isBlank(accessToken)) {
            return null;
        }

        String result = this.facebookRequest(accessToken);
        
        return parseOAuthInfo(result);
    }

    private String parseAccountName(String jsonString) {
    	JSONObject jsonObject = JSON.parseObject(jsonString);
    	if (jsonObject.containsKey("error")){
    		return null;
    	}
    	
    	String uid = jsonObject.getString("id");
    	if (uid == null){
    		return null;
    	}
    	String email = null;
    	try{
    		email = jsonObject.getString("email");
    	}catch(Exception e){
    		email = "user_" + uid;
    	}
    	String name = null;
    	try{
    		name = jsonObject.getString("name");
    	}catch(Exception e){
    		name = email;
    	}
    	
    	return name;
    }

    private String parseOAuthUid(String jsonString) {
    	JSONObject jsonObject = JSON.parseObject(jsonString);
    	if (jsonObject.containsKey("error")){
    		return null;
    	}
    	
    	String uid = jsonObject.getString("id");
    	if (uid == null){
    		return null;
    	}
    	
    	return uid;
    }
    
    private FacebookOauthInfoVO parseOAuthInfo(String jsonString){
    	
    	JSONObject jsonObject = JSON.parseObject(jsonString);
    	if (jsonObject.containsKey("error")){
    		return null;
    	}
    	
    	String uid = jsonObject.getString("id");
    	if (uid == null){
    		return null;
    	}
    	String email = jsonObject.getString("email");
    	if (email == null){
    		email = "user_" + uid;
    	}
    	
    	String name = jsonObject.getString("name");
    	if (name == null){
    		name = email;
    	}
    	
    	FacebookOauthInfoVO facebookOauthInfoVO=  new FacebookOauthInfoVO();
    	facebookOauthInfoVO.setEmail(email);
    	facebookOauthInfoVO.setName(name);
    	facebookOauthInfoVO.setUid(uid);
    	
    	return facebookOauthInfoVO;
    }

}
