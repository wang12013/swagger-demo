package com.example.swaggerdemo.file;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * @author wangzy
 * @date 2022/6/13 9:33
 *
 * 返回当前目录下的所有文件的文件名
 */
public class FileFindTest {
    @Test
    public void test(){
        //文件夹目录
        String dirPath = "C:\\Users\\王志勇\\Desktop\\test";

        //File对象
        File file = new File(dirPath);
        //获取目录下的所有文件和文件夹
        //File[] files = file.listFiles();

        //获取当前目录下的所有文件的文件名和文件夹名
        //String[] list = file.list();

        File[] files = file.listFiles();
        for (File  f : files){
            //只选出文件，显示文件名，文件夹不要
            if (f.isFile()){
                System.out.println(f.getName());
            }
        }

        /*
         * File类提供了两个文件过滤器方法
         * public String[] list(FilenameFilter filter)
         * public File[] listFiles(FileFilter filter)
         */
        File[] files2 = file.listFiles((dir,name)->{
            //详细代码看下面
            return name.endsWith(".txt");//列出以.jpg结尾的文件
        });

        for (File  f : files2){
            //只选出文件，显示文件名，文件夹不要
            if (f.isFile()){
                System.out.println(f.getName());
            }
        }


    }

    //创建文件在指定的目录
    @Test
    public void test2() throws IOException {

        File file = new File("D:\\io\\io1\\hello.txt");
        //创建一个与file同目录下的另外一个文件，文件名为：haha.txt
        File destFile = new File(file.getParent(),"haha.txt");
        boolean newFile = destFile.createNewFile();
        if(newFile){
            System.out.println("创建成功！");
        }
    }

    /*
     * File类提供了两个文件过滤器方法
     * public String[] list(FilenameFilter filter)
     * public File[] listFiles(FileFilter filter)

     */
    @Test
    public void test3(){
        File srcFile = new File("d:\\code");

        File[] subFiles = srcFile.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jpg");
            }
        });

        for(File file : subFiles){
            System.out.println(file.getAbsolutePath());
        }
    }

}
