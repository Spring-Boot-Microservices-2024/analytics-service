package org.naukma.analytics.authentication;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Getter
@Service
public class AuthenticationService {
    @Value("${auth.header:X-API-KEY}")
    private String authHeader;
    @Value("${auth.key:booking}")
    private String authKey;

    @PostConstruct
    public void init() {
        log.info("AuthenticationService initialized with header: {} and key: {}", authHeader, authKey);
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(authHeader);
        if (apiKey == null || !apiKey.equals(authKey))
            throw new BadCredentialsException("Invalid API Key");

        return new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);
    }
}
