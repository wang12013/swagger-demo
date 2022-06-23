package com.example.swaggerdemo.service;

import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author wangzy
 * @date 2022/6/20 16:45
 */
@Service
public class VideoService {

    /**
     * 获取目录下最近的mp4文件
     * @param dirPath 文件夹目录
     * @return
     */
    public File getLatestMap4(String dirPath){

        File file = new File(dirPath);
        String[] list = file.list((dir,name)->{
            return name.endsWith(".mp4");
        });

        //筛选多个文件后缀名
        /*String[] list2 = file.list((dir,name)->{
           return  name.endsWith(".jpg")|| name.endsWith("txt");
        });*/
    //简化写法
        String[] list2 = file.list(((dir, name) -> name.endsWith(".jpg")|| name.endsWith("txt")));


        File lastFile = null;
        long lastTime = 0;

        for (int i=0 ; i< list.length;i++){
            File file1 = new File(dirPath + File.separator + list[i]);
            System.out.println("文件名：" + list[i] + "，最近更新时间---》" + file1.lastModified());

            //最近更新的文件
            if (file1.lastModified() > lastTime){
                lastFile = file1;
                lastTime = file1.lastModified();
            }
        }

        return lastFile;
    }

    /**
     * 筛选出路径下指定类型文件的，最新的文件
     * @param dirPath 文件路径
     * @param fileType 文件类型(后缀名，如 ".mp4" )
     * @return
     */
    public File getLatestFile(String dirPath,String fileType){

        //文件夹路径
        File file = new File(dirPath);

        //按照文件类型筛选出需要的文件
        String[] list = file.list((dir,name)-> name.endsWith(fileType));

        File lastFile = null;
        long lastTime = 0L;

        for (int i=0; i< list.length ; i++){
            File file1 = new File(dirPath+File.separator + list[i]);

            //最近跟新的文件
            if (file1.lastModified() > lastTime){
                lastFile = file1;
                lastTime = file1.lastModified();
            }
        }

        return lastFile;

    }
}
