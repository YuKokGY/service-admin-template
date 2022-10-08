package io.github.talelin.latticy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author pedro@TaleLin
 */
@SpringBootApplication
@MapperScan(basePackages = {"io.github.talelin.latticy.mapper"})
public class NftClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(NftClientApplication.class, args);
    }
}
