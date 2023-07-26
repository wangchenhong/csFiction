package io.github.xxyopen.novel.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 跨域配置属性
 *
 * @author xiongxiaoyang
 * @date 2022/5/17
 * 设置了前缀为novel.cors，表示该类中的属性与配置文件中以novel.cors为前缀的属性进行映射。
 * 通过使用@ConfigurationProperties注解和记录类，Spring Boot可以自动将配置文件中的属性值绑定到CorsProperties对象的对应属性上。
 */
@ConfigurationProperties(prefix = "novel.cors")
public record CorsProperties(List<String> allowOrigins) {

}
