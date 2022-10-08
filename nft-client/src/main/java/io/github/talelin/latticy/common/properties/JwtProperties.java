package io.github.talelin.latticy.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("lin.cms")
public class JwtProperties {
    private static final String[] DEFAULT_EXCLUDE_METHODS = new String[]{"OPTIONS"};
    private String tokenSecret = "";
    private String[] excludeMethods;
    private Long tokenAccessExpire;
    private Long tokenRefreshExpire;
    private boolean loggerEnabled;

    public JwtProperties() {
        this.excludeMethods = DEFAULT_EXCLUDE_METHODS;
        this.tokenAccessExpire = 3600L;
        this.tokenRefreshExpire = 2592000L;
        this.loggerEnabled = true;
    }

    public boolean isLoggerEnabled() {
        return this.loggerEnabled;
    }

    public void setLoggerEnabled(boolean loggerEnabled) {
        this.loggerEnabled = loggerEnabled;
    }

    public String getTokenSecret() {
        return this.tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public Long getTokenAccessExpire() {
        return this.tokenAccessExpire;
    }

    public void setTokenAccessExpire(Long tokenAccessExpire) {
        this.tokenAccessExpire = tokenAccessExpire;
    }

    public Long getTokenRefreshExpire() {
        return this.tokenRefreshExpire;
    }

    public void setTokenRefreshExpire(Long tokenRefreshExpire) {
        this.tokenRefreshExpire = tokenRefreshExpire;
    }

    public String[] getExcludeMethods() {
        return this.excludeMethods;
    }

    public void setExcludeMethods(String[] excludeMethods) {
        this.excludeMethods = excludeMethods;
    }
}

