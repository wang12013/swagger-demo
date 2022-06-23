package com.example.swaggerdemo.file;

import cn.hutool.json.JSONStrFormater;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.swaggerdemo.config.JsonFormatTool;
import com.example.swaggerdemo.utils.MyJsonFormatter;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangzy
 * @date 2022/6/12 11:00
 */
public class JsonReadTest {



    /**
     * 生成.json格式文件
     */
    public static boolean createJsonFile(String jsonString, String filePath, String fileName) {
        // 标记文件生成是否成功
        boolean flag = true;

        // 拼接文件完整路径
        String fullPath = filePath + File.separator + fileName + ".json";

        // 生成json格式文件
        try {
            // 保证创建一个新文件
            File file = new File(fullPath);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();

            // 格式化json字符串
            jsonString = JsonFormatTool.formatJson(jsonString);

            //格式化json字符串
            JSONStrFormater.format(jsonString);

            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(jsonString);
            write.flush();
            write.close();
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }

        // 返回是否成功的标记
        return flag;
    }



    static class User {
        private String name;
        private String sex;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", sex='" + sex + '\'' +
                    '}';
        }
    }


    @Test
    public void testSaveFile(){
        ArrayList<taskInfo> taskInfos = new ArrayList<>();
        taskInfos.add(new taskInfo("1", "任务一"));
        taskInfos.add(new taskInfo("2", "任务二"));

        EquStatus equStatus = new EquStatus(1L, "设备1", "01", taskInfos);

        EquStatus equStatus2 = new EquStatus(2L, "设备2", "01", taskInfos);
        //就是存在问题的，这两个taskInfos是同一个

        HashMap<String, EquStatus> map = new HashMap<>();
        map.put("01",equStatus);
        map.put("02",equStatus2);

        //这地方有问题
        String s = JSONArray.toJSONString(map);//会有问题
        //todo 这个字符串有问题，fastjson bug？？？
//fastjson版本：1.2.37有这个问题，用JSON和JSONArray都会存在问题

        Gson gson = new Gson();
        String s1 = gson.toJson(map);
        String s2 = gson.toJson(equStatus);

        //方法里面做了对字符串的格式化，就传原字符串过去就行
        boolean isCreate = JsonReadTest.createJsonFile(s1, "G:\\videoTest", "equStatusMap");
        System.out.println(isCreate);


        boolean isCreat2 = JsonReadTest.createJsonFile(s2, "G:\\videoTest", "equStatus");

        System.out.println(isCreat2);




    }

    @Test
    public void testParse() throws IOException {
        File file = new File("G:\\videoTest\\equStatusMap.json");

        //读取字符串
        String jsonString = FileUtils.readFileToString(file);
        System.out.println(jsonString);

        //第一个参数是json字符串，第二个参数是解析成什么类型JSON.parseObject(jsonString, User.class);
        //fastjson，怎么解析Map
        Map<String,EquStatus> parse = (Map<String,EquStatus>) JSON.parse(jsonString); //写HashMap会报错,写Map就可以

        System.out.println(parse.get("01"));
        //{"taskList":[{"taskName":"任务一","taskId":"1"},{"taskName":"任务二","taskId":"2"}],"equName":"设备1","orgId":1,"status":"01"}
        System.out.println(parse.get("02"));

//使用Gson,通常就用两个方法，fromJson,和toJson两个方法
        Gson gson = new Gson();
        HashMap parse2 = (HashMap) gson.fromJson(jsonString, HashMap.class);

        System.out.println(parse2.get("01"));//拿到的是一个LinkedTreeMap，而不是我要的EquStatus
        //{orgId=1.0, equName=设备1, status=01, taskList=[{taskId=1, taskName=任务一}, {taskId=2, taskName=任务二}]}
        System.out.println(parse2.get("02"));

        //但是使用,下面这行就会报错，LinkedTreeMap cannot be cast to com.example.swaggerdemo.file.EquStatus
        LinkedTreeMap map2 =  (LinkedTreeMap) parse2.get("01");
        //从HashMap去拿到的是一个LinkedTreeMap，而不是我要的EquStatus，尽管指定了HashMap的泛型，也不起作用
        //所以我取消了上面HasMap的泛型，
        //里面那层还是LinkedTreeMap,也就是map2的类型就是一个LinkedTreeMap，
        //怎么把他转成我要的EquStatus,



    }
    @Test
    public void testParse2() throws IOException {
        File file = new File("G:\\videoTest\\equStatus.json");

        //读取字符串
        String jsonString = FileUtils.readFileToString(file);
        System.out.println(jsonString);

        //第一个参数是json字符串，第二个参数是解析成什么类型JSON.parseObject(jsonString, User.class);

        EquStatus parse = JSON.parseObject(jsonString, EquStatus.class);

        //对象里的属性是一个list的时候是可以解析出来的
        System.out.println(parse.getEquName());
        System.out.println(parse.getTaskList().get(1));
    }



    @Test
    public void test1(){


        User user = new User();
        user.setName("用户名");
        user.setSex("男");

        //将对象转为json字符串
        String s = JSONArray.toJSONString(user);

        System.out.println(s);

        //创建json文件
        JsonReadTest.createJsonFile(s,"G:\\videoTest","testJson");

    }

    //读取json文件
    @Test
    public void testUser() throws IOException {
       // ClassPathResource resource = new ClassPathResource("json/user.json");
        //File file = resource.getFile();
        File file = new File("G:\\videoTest\\testJson.json");
        //从文件中读取json字符串，commons-io的
        String jsonString = FileUtils.readFileToString(file);

        //将字符串转为对象
        User user = JSON.parseObject(jsonString, User.class);
        System.out.println(user.getSex());
    }

}
