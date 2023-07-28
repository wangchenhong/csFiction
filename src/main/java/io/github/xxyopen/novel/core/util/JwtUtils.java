package io.github.xxyopen.novel.core.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * JWT 工具类
 *
 * @author ChenXi
 * @date 2022/5/17
 */
@ConditionalOnProperty("novel.jwt.secret")
/**
 * 用于标记一个类为 Spring 管理的组件，使得它可以通过自动扫描等方式被 Spring 自动加载和管理。
 */
@Component
@Slf4j
public class JwtUtils {

    /**
     * 注入JWT加密密钥
     */
    @Value("${novel.jwt.secret}")
    private String secret;

    /**
     * 定义系统标识头常量
     */
    private static final String HEADER_SYSTEM_KEY = "systemKeyHeader";

    /**
     * 根据用户ID生成JWT
     *
     * @param uid       用户ID
     * @param systemKey 系统标识
     * @return JWT
     */
    public String generateToken(Long uid, String systemKey) {
        /**
         * setHeaderParam()：设置 JWT 的 Header 参数，其中 HEADER_SYSTEM_KEY 是一个常量，表示 Header 中自定义的系统密钥信息。
         * setSubject()：设置 JWT 的 Payload 中的 Subject（主题），将用户的 ID 转换为字符串作为 Subject 的值。
         * Payload 部分通常包含有关用户或其他主题的信息。
         * signWith()：使用指定的密钥对 JWT 进行签名。
         * Keys.hmacShaKeyFor() 方法根据提供的字节数组生成一个 HMAC 密钥，用于对 JWT 进行签名。
         * secret 是一个代表密钥的字符串，通过 getBytes() 方法转换为字节数组。
         * compact()：生成最终的 JWT 令牌。
         */
        return Jwts.builder()
            .setHeaderParam(HEADER_SYSTEM_KEY, systemKey)
            .setSubject(uid.toString())
            .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
            .compact();
    }

    /**
     * 解析JWT返回用户ID
     *
     * @param token     JWT
     * @param systemKey 系统标识
     * @return 用户ID
     */
    public Long parseToken(String token, String systemKey) {
        Jws<Claims> claimsJws;
        try {
            /**
             * 首先使用给定的密钥 secret创建一个签名验证器，并设置到 JWT 解析器中。然后，它使用解析器构建器的 build() 方法构建解析器对象。
             * 接下来，它调用 parseClaimsJws(token)方法来解析JWT令牌并获取其中的声明（claims）。
             * 解析后的结果将存储在 claimsJws 对象中。
             */
            claimsJws = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token);
            // OK, we can trust this JWT
            // 判断该 JWT 是否属于指定系统
            /**
             * 如果解析成功，程序会继续判断该 JWT 是否属于指定的系统。
             * 它通过比较 JWT 头部中的自定义系统密钥信息 HEADER_SYSTEM_KEY 和传入的 systemKey 是否相等来进行判断。
             * 如果相等，说明该 JWT 是属于指定系统的。
             */
            if (Objects.equals(claimsJws.getHeader().get(HEADER_SYSTEM_KEY), systemKey)) {
//                如果以上验证都通过，方法将返回 JWT 载荷中的 Subject（主题）字段，并将其解析为 Long 类型返回。
                return Long.parseLong(claimsJws.getBody().getSubject());
            }
        } catch (JwtException e) {
            log.warn("JWT解析失败:{}", token);
            // don't trust the JWT!
        }
        return null;
    }

}
