package com.example.swaggerdemo.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.Watcher;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * @author wangzy
 * @date 2022/6/13 9:06
 *
 * https://www.hutool.cn/docs/#/core/IO/%E6%96%87%E4%BB%B6%E7%9B%91%E5%90%AC-WatchMonitor?id=%e4%bd%bf%e7%94%a8
 */
@Component
public class FileMonitor {
    //监听指定的文件目录
    //String dirPath ="C:\\Users\\王志勇\\Desktop\\test\\test.txt";
    String dirPath ="C:\\Users\\王志勇\\Desktop\\test";



    @PostConstruct
    public void fileCreateMonitor(){


        //给的dirPath是一个文件那就是监听文件，给的是一个文件夹就是监听文件夹里面
        File file = FileUtil.file(dirPath);
        if (ObjectUtil.isNull(file) && !file.exists()){
            Console.log("文件或目录不存在：{}",dirPath);
        }
        //这里只监听文件或目录的修改事件  用WatchMonitor.EVENTS_指定监控所有事件
        WatchMonitor watchMonitor = WatchMonitor.create(file, WatchMonitor.EVENTS_ALL);
        watchMonitor.setWatcher(new Watcher(){
            @Override
            public void onCreate(WatchEvent<?> event, Path currentPath) {
                Object obj = event.context();
                Console.log("创建：{}-> {}", currentPath, obj);





            }

            @Override
            public void onModify(WatchEvent<?> event, Path currentPath) {
                Object obj = event.context();
                Console.log("修改：{}-> {}", currentPath, obj);
            }

            @Override
            public void onDelete(WatchEvent<?> event, Path currentPath) {
                Object obj = event.context();
                Console.log("删除：{}-> {}", currentPath, obj);
            }

            @Override
            public void onOverflow(WatchEvent<?> event, Path currentPath) {
                Object obj = event.context();
                Console.log("Overflow：{}-> {}", currentPath, obj);
            }
        });

//设置监听目录的最大深入，目录层级大于制定层级的变更将不被监听，默认只监听当前层级目录
        watchMonitor.setMaxDepth(3);
//启动监听
        watchMonitor.start();
    }

}


