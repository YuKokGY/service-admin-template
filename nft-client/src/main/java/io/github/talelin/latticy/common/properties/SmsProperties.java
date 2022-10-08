/**
 * @作者 YuKok
 * @创建时间 2020/11/7 18:26
 */
package io.github.talelin.latticy.common.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("sms")
public class SmsProperties {

    public String accessKeyId;

    public String accessKeySecret;

    public String product;

    public String domain;

    public String signName;

    public String templateCode;
}
