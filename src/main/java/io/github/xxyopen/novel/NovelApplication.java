package io.github.xxyopen.novel;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Map;

@SpringBootApplication
/**
 * @MapperScan注解扫描指定包下的MyBatis Mapper接口。
 * 指定了包路径io.github.xxyopen.novel.dao.mapper
 * 用于扫描并注册这些Mapper接口，使其可以被MyBatis框架自动识别并生成实现类。
 */
@MapperScan("io.github.xxyopen.novel.dao.mapper")
//用于启用缓存功能
@EnableCaching
@EnableScheduling
@Slf4j
public class NovelApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovelApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext context) {
        return args -> {
            Map<String, CacheManager> beans = context.getBeansOfType(CacheManager.class);
            log.info("加载了如下缓存管理器：");
            beans.forEach((k, v) -> {
                log.info("{}:{}", k, v.getClass().getName());
                log.info("缓存：{}", v.getCacheNames());
            });

        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .securityMatcher(EndpointRequest.toAnyEndpoint())
            .authorizeHttpRequests(requests -> requests.anyRequest().hasRole("ENDPOINT_ADMIN"));
        http.httpBasic();
        return http.build();
    }

}
