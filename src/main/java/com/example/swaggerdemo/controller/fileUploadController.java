package com.example.swaggerdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @author wangzy
 * @date 2022/6/24 13:52
 */
@Controller
public class fileUploadController {

    private static final String savePath = "C:\\Users\\王志勇\\Desktop\\test";

    @PostMapping("uploadFile")
    public void upload(@RequestParam("mfid") String mfid,@RequestParam("file") MultipartFile file) throws Exception {
        byte[] bytes = file.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        String name= file.getName();

        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(savePath + File.separator + name));

        byte[] buff = new byte[1024];

        int len=0;

        while ((len = inputStream.read(buff)) != -1){
            outputStream.write(buff,0,len);
        }

        inputStream.close();
        outputStream.close();


    }
}
