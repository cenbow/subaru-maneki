package com.subaru.maneki.service.user.imp;

import com.subaru.maneki.service.user.OAuthService;
import com.subaru.maneki.vo.FacebookOauthInfoVO;
import org.springframework.stereotype.Service;

/**
 * @author zhangchaojie
 * @since 2016-08-12
 */
@Service("twitterOAuthService")
public class TwitterOAuthServiceImp implements OAuthService {

    @Override
    public String getAccountName(String accessToken) {
        return null;
    }

    @Override
    public String getOAuthUid(String accessToken) {
        return null;
    }

    @Override
    public FacebookOauthInfoVO getOAuthInfo(String accessToken) {
        return null;
    }

}
