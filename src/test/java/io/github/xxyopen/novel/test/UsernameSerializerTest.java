package io.github.xxyopen.novel.test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.xxyopen.novel.core.json.serializer.UsernameSerializer;
import io.github.xxyopen.novel.dto.resp.BookCommentRespDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

/**
 * @ClassName UsernameSerializerTest
 * @Description 用户名序列化测试
 * @Author Ducking
 * @DATE 2023/7/31 10:37
 * @Version 1.0
 */
@SpringBootTest
@RequiredArgsConstructor
public class UsernameSerializerTest {

    @Test
    public void serializeTest() throws IOException {
        // 原用户名
        String username = "123456789";
        System.out.println("old username: " + username);

        // 生成对象
        BookCommentRespDto.CommentInfo commentInfo =
            BookCommentRespDto.CommentInfo.builder().
                commentUser(username).
                build();
        // 序列化
        String s = new ObjectMapper().writeValueAsString(commentInfo);
        System.out.println(s);
    }
}
