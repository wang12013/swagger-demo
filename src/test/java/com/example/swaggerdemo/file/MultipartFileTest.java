package com.example.swaggerdemo.file;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author wangzy
 * @date 2022/6/28 11:13
 */
@SpringBootTest
public class MultipartFileTest {

    //当使用的方法参数是MultipartFile 时，比如网页上文件上传上来一个文件时就用MultipartFile来接受

    //但是我要远程调用别的服务，他的方法参数是MultipartFile ,我怎么使用本地文件创建一个MultipartFile 传参过去
    @Test
    public void test() throws Exception {
        //生成file文件
        File file = new File("G:\\智慧调解-总体设计说明书1.2.doc");
        //File文件转为MultipartFile
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("name", file.getName(), "multipart/from-data", IOUtils.toByteArray(inputStream));

        //multipart 的name,getOriginalFilename才是真正的文件名


    }
}
