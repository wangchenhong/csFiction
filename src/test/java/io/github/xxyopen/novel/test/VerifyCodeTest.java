package io.github.xxyopen.novel.test;

import io.github.xxyopen.novel.core.common.util.ImgVerifyCodeUtils;
import io.github.xxyopen.novel.manager.redis.VerifyCodeManager;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.AfterTestClass;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Scanner;

/**
 * @ClassName VerifyCodeTest
 * @Description 验证码测试
 * @Author Ducking
 * @DATE 2023/7/31 9:27
 * @Version 1.0
 */
@SpringBootTest
@RequiredArgsConstructor
public class VerifyCodeTest {

    private final String imagePath = "D:\\.workspace\\code\\novel\\csFiction\\src\\test\\resources\\image\\verifyCodeImage.png"; // 保存图片的路径
    private final String verifyCode = "4354";

    @Test
    public void genVerifyCodeTest() throws IOException {
        String imageString = ImgVerifyCodeUtils.genVerifyCodeImg(verifyCode);
        byte[] imageBytes = Base64.getDecoder().decode(imageString);

        // 将解码后的图片保存到文件
        FileOutputStream fos = new FileOutputStream(imagePath);
        fos.write(imageBytes);
        fos.close();
        System.out.println("Image saved to " + imagePath);
    }
}
