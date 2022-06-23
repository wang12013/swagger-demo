package com.example.swaggerdemo.file;

import com.example.swaggerdemo.utils.VideoEncodeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author wangzy
 * @date 2022/6/14 14:42
 */
@SpringBootTest
public class TestEncode {
    @Test
    public void test() throws Exception {
        String s1 = "G:\\videoTest\\1.mp4";
        String s2 = "G:\\videoTest\\11.mp4";

        VideoEncodeUtil.encode(s1,s2);
    }
}
