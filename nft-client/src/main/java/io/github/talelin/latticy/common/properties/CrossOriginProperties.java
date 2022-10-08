package io.github.talelin.latticy.common.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("lin-cross")
public class CrossOriginProperties {

    private Boolean crossOrigin;
    private String allowHeaders;
}
