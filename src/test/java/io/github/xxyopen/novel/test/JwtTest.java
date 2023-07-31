package io.github.xxyopen.novel.test;

import io.github.xxyopen.novel.core.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @ClassName JwtTest
 * @Description JWT生成与解析测试
 * @Author Ducking
 * @DATE 2023/7/31 10:14
 * @Version 1.0
 */
@SpringBootTest
public class JwtTest {

    private final Long uid = 1212121212L;
    private final String systemKey = "front";

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    public void tokenTest() {
        // 生成token
        String jwt = jwtUtils.generateToken(uid, systemKey);
        assertThat(jwt).isNotNull();;

        // 解析token
        Long new_uid = jwtUtils.parseToken(jwt, systemKey);
        assertThat(new_uid).isEqualTo(uid);
    }
}
