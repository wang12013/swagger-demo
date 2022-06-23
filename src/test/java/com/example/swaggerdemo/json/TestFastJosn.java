package com.example.swaggerdemo.json;

import java.io.FileInputStream;
import java.io.IOException;
 import java.io.InputStream;
 import java.util.ArrayList;
 import java.util.Iterator;
 import java.util.List;
 
 import org.apache.commons.io.IOUtils;

 
 import com.alibaba.fastjson.JSON;
 import com.alibaba.fastjson.JSONArray;
 import com.alibaba.fastjson.JSONObject;
 import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.jupiter.api.Test;

/**
  * JSONArray : 相当于List </br>
  * JSONObject: 相当于Map<String,Object>
  * @author yang.han
 *
 * https://www.bbsmax.com/A/D854L6D65E/
  *
  */
 public class TestFastJosn {
 
     /**
      * java对象转 json字符串
      */
     @Test
     public void objectToJson() {
         // 简单java类转json字符串
         User user = new User("dmego", "123456");
         String UserJson = JSON.toJSONString(user);
         System.out.println("简单java类转json字符串:" + UserJson);
 
         // List<Object>转json字符串
         User user1 = new User("zhangsan", "123123");
         User user2 = new User("lisi", "321321");
         List<User> users = new ArrayList<User>();
         users.add(user1);
         users.add(user2);
         String ListUserJson = JSON.toJSONString(users);
         System.out.println("List<Object>转json字符串:" + ListUserJson);
 
         // 复杂java类转json字符串
         UserGroup userGroup = new UserGroup("userGroup", users);
         String userGroupJson = JSON.toJSONString(userGroup);
         System.out.println("复杂java类转json字符串:" + userGroupJson);
 
     }
 
     /**
      * json字符串转java对象 注：字符串中使用双引号需要转义 (" --> \"),这里使用的是单引号
      */
     @Test
     public void JsonToObject() {
         /*
          * json字符串转简单java对象 字符串：{"password":"123456","username":"dmego"}
          */
 
         String jsonStr1 = "{'password':'123456','username':'dmego'}";
         User user = JSON.parseObject(jsonStr1, User.class);
         System.out.println("json字符串转简单java对象:" + user.toString());
 
         /*
          * json字符串转List<Object>对象
          * 字符串：[{"password":"123123","username":"zhangsan"
          * },{"password":"321321","username":"lisi"}]
          */
         String jsonStr2 = "[{'password':'123123','username':'zhangsan'},{'password':'321321','username':'lisi'}]";
         List<User> users = JSON.parseArray(jsonStr2, User.class);
         System.out.println("json字符串转List<Object>对象:" + users.toString());
 
         /*
          * json字符串转复杂java对象
          * 字符串：{"name":"userGroup","users":[{"password":"123123"
          * ,"username":"zhangsan"},{"password":"321321","username":"lisi"}]}
          */
         String jsonStr3 = "{'name':'userGroup','users':[{'password':'123123','username':'zhangsan'},{'password':'321321','username':'lisi'}]}";
         UserGroup userGroup = JSON.parseObject(jsonStr3, UserGroup.class);
         System.out.println("json字符串转复杂java对象:" + userGroup);
     }

    /**
     * 从文件读取json数据
     * @throws IOException
     */
     @Test
     public void parserJsonTxt() throws IOException{
         //ClassLoader cl = this.getClass().getClassLoader();
         //InputStream inputStream = cl.getResourceAsStream("C:\\Users\\王志勇\\Desktop\\test\\date1.json");
         FileInputStream fileInputStream = new FileInputStream("C:\\Users\\王志勇\\Desktop\\test\\date1.json");
         String jsontext = IOUtils.toString(fileInputStream, "utf8");
 
         JSONObject obj=JSONObject.parseObject(jsontext);//获取jsonobject对象
         JSONObject obj1 = obj.getJSONObject("data");
         JSONArray jsonArray = obj1.getJSONArray("rows");
         System.out.println("jsonArray:"+jsonArray);
         JSONObject obj2 = jsonArray.getJSONObject(1);
         System.out.println("obj2:" +obj2);
         for(Iterator iterator = jsonArray.iterator(); iterator.hasNext();) {
             JSONObject jsonObject1 = (JSONObject) iterator.next();
             System.out.println(jsonObject1);
         }
 
         info_util iu = JSON.parseObject(jsontext, info_util.class);//取得第一层JSONObject
         info_data_util du = JSON.parseObject(iu.getData(), info_data_util.class);//取得第二层JSONObject
         List<info_array_Util> olist = JSON.parseArray(du.getRows(), info_array_Util.class);//取得第三层JSONArray
         System.out.println(iu);
         System.out.println(du);
         System.out.println(olist);
 
     }
 
 }