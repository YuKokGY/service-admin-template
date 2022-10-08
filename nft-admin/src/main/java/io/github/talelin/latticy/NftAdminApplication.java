package io.github.talelin.latticy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pedro@TaleLin
 */
@SpringBootApplication(scanBasePackages = {"io.github.talelin.latticy"})
@MapperScan(basePackages = {"io.github.talelin.latticy.mapper"})
@RestController
public class NftAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(NftAdminApplication.class, args);
    }
}
