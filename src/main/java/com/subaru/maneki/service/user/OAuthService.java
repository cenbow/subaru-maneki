package com.subaru.maneki.service.user;

import com.subaru.maneki.vo.FacebookOauthInfoVO;

/**
 * @author zhangchaojie
 * @since 2016-08-12
 */
public interface OAuthService {

    public String getAccountName(String accessToken);

    public String getOAuthUid(String accessToken);

    public FacebookOauthInfoVO getOAuthInfo(String accessToken);
}
