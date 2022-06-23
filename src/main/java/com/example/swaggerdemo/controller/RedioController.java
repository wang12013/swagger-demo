package com.example.swaggerdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.OutputStream;

@RequestMapping(value = "redio")
@Controller
public class RedioController {

    //可以拖动播放，播放大视频播放不了
    @RequestMapping(value = "/getVido", method = RequestMethod.GET)
    @ResponseBody
    public void getVido(HttpServletRequest request, HttpServletResponse response ,String fileName) {
        // 视频路径
        String file = "G:\\videoTest\\" + fileName;
        try {
            //	获得视频文件的输入流
            FileInputStream inputStream = new FileInputStream(file);
            // 创建字节数组，数组大小为视频文件大小
            byte[] data = new byte[inputStream.available()];
            // 将视频文件读入到字节数组中
            inputStream.read(data);
            String diskfilename = "final.mp4";
            // 设置返回数据格式
            response.setContentType("video/mp4");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + diskfilename + "\"");
            System.out.println("data.length " + data.length);
            response.setContentLength(data.length);
            response.setHeader("Content-Range", "" + Integer.valueOf(data.length - 1));
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Etag", "W/\"9767057-1323779115364\"");
            // 获得 response 的字节流
            OutputStream os = response.getOutputStream();
            // 将视频文件的字节数组写入 response 中
            os.write(data);
            //先声明的流后关掉！
            os.flush();
            os.close();
            inputStream.close();

        } catch (Exception e) {

        }
    }
}