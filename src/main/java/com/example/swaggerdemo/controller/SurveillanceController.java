package com.example.swaggerdemo.controller;

import cn.hutool.core.util.StrUtil;
import com.example.swaggerdemo.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 * @author wangzy
 * @date 2022/6/20 16:56
 */
@Controller
@Slf4j
@RequestMapping("surveillance")
public class SurveillanceController {

    private String dirPath = "G:\\videoTest";
    @Autowired
    private VideoService videoService;

    /**
     * 播放最近的mp4-----每次点进度条就相当于重新请求了controller
     *
     * todo 点进度条都会重新请求一次controller，这造成报错的原因
     * @param request
     * @param response
     */
    @GetMapping("video")
    public void play(HttpServletRequest request, HttpServletResponse response) {
        RandomAccessFile randomAccessFile = null;
        OutputStream outputStream = null;
        try {
            response.reset();
            File latestMap4 = videoService.getLatestMap4(dirPath);
            long fileLength = latestMap4.length();
            // 随机读文件
            randomAccessFile = new RandomAccessFile(latestMap4, "r");

            //获取从那个字节开始读取文件
            String rangeString = request.getHeader("Range");
            long range=0;
            if (StrUtil.isNotBlank(rangeString)) {
                range = Long.valueOf(rangeString.substring(rangeString.indexOf("=") + 1, rangeString.indexOf("-")));
            }
            //获取响应的输出流
            outputStream = response.getOutputStream();
            //设置内容类型
            response.setHeader("Content-Type", "video/mp4");
            //返回码需要为206，代表只处理了部分请求，响应了部分数据
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

            // 移动访问指针到指定位置
            randomAccessFile.seek(range);
            // 每次请求只返回1MB的视频流不会报错，但是返回2MB就会报错了
            byte[] bytes = new byte[1024 * 1024];
            int len = randomAccessFile.read(bytes);
            //设置此次相应返回的数据长度
            response.setContentLength(len);

            //设置此次相应返回的数据范围
            response.setHeader("Content-Range", "bytes "+range+"-"+(fileLength-1)+"/"+fileLength);
            // 将这1MB的视频流响应给客户端，写出流的时候，超过1MB就会报错
            outputStream.write(bytes, 0, len);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            log.info(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }finally {

            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                log.info(e.getMessage());
            }
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
                log.info(e.getMessage());
            }

        }


        //System.out.println("返回数据区间:【"+range+"-"+(range+len)+"】");
    }

}
