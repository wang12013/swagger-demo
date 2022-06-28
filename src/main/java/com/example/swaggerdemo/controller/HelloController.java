package com.example.swaggerdemo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author wangzy
 * @date 2022/4/17 15:04
 *
 * 常用注解
 * @Api
 * 是类上注解 控制了整个类生成接口信息的内容，属性tags 类的名字 description描述
 * @ApiOperation 写在方法上的注解，对方法进行描述， 属性value 方法描述 notes 提示信息
 * @ApiParam 写在方法参数中的注解，用于对参数进行描述，说明一下是否是必填项，属性有 name 参数名字 value参数描述 required是否是必须
 * @ApiModel是类上注解，主要应用在实体类上，属性value 类名称，description 是描述
 * @ApiModelproperty可以应用在方法上或是属性上，用于方法参数是应用类型时进行定义描述
 * @Apilgnore 用于类上方法上参数上，表示被忽视，
 */

@RestController
public class HelloController {

    @ApiOperation("hello控制器的hello方法")//这个注解只能放在方法上
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @ApiOperation("hello控制器的name方法")//这个注解只能放在方法上
    @GetMapping("/hello2")
    public String hello2(@ApiParam("用户名") String name){
        return name;
    }

    //用List来接受路径变量
    @GetMapping("/{mfids}/get")
    public String listRequest(@PathVariable("mfids")List<String> mfids){
        System.out.println(mfids);
        return mfids.toString();

        //请求路径： localhost:8082/12,34,56/get  ----> 能成功访问
        //路径变量是以,隔开的一个长字符串，他能自动解析成一个list
    }


    //定时任务
    @Scheduled(fixedRate = 5000)//每隔5秒执行
    public void timeTask(){
        System.out.println("定时任务执行了");
    }

    /**
     * 获取视频流
     * @param response
     * @param videoId 视频存放信息索引
     * @return
     * @author xWang
     * @Date 2020-05-20

    @RequestMapping("/getVideo/{videoId}")
    public void getVideo(HttpServletRequest request,HttpServletResponse response,@PathVariable Integer videoId)
    {
        //视频资源存储信息
        VideoSource videoSource = videoSourceService.selectById(videoId);
        response.reset();
        //获取从那个字节开始读取文件
        String rangeString = request.getHeader("Range");

        try {
            //获取响应的输出流
            OutputStream outputStream = response.getOutputStream();
            File file = new File(videoSource.getFileAddress());
            if(file.exists()){
                RandomAccessFile targetFile = new RandomAccessFile(file, "r");
                long fileLength = targetFile.length();
                //播放
                if(rangeString != null){

                    long range = Long.valueOf(rangeString.substring(rangeString.indexOf("=") + 1, rangeString.indexOf("-")));
                    //设置内容类型
                    response.setHeader("Content-Type", "video/mp4");
                    //设置此次相应返回的数据长度
                    response.setHeader("Content-Length", String.valueOf(fileLength - range));
                    //设置此次相应返回的数据范围
                    response.setHeader("Content-Range", "bytes "+range+"-"+(fileLength-1)+"/"+fileLength);
                    //返回码需要为206，而不是200
                    response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                    //设定文件读取开始位置（以字节为单位）
                    targetFile.seek(range);
                }else {//下载

                    //设置响应头，把文件名字设置好
                    response.setHeader("Content-Disposition", "attachment; filename="+videoSource.getFileName() );
                    //设置文件长度
                    response.setHeader("Content-Length", String.valueOf(fileLength));
                    //解决编码问题
                    response.setHeader("Content-Type","application/octet-stream");
                }


                byte[] cache = new byte[1024 * 300];
                int flag;
                while ((flag = targetFile.read(cache))!=-1){
                    outputStream.write(cache, 0, flag);
                }
            }else {
                String message = "file:"+videoSource.getFileName()+" not exists";
                //解决编码问题
                response.setHeader("Content-Type","application/json");
                outputStream.write(message.getBytes(StandardCharsets.UTF_8));
            }

            outputStream.flush();
            outputStream.close();

        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }*/



}
