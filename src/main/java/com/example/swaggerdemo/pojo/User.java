package com.example.swaggerdemo.pojo;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangzy
 * @date 2022/4/17 17:03
 *
 * swagger中是可以扫描工程中的实体类的
 *      只要接口中返回值存在实体类，他就能扫描到这个实体类的信息显示到swagger中
 *
 *      加了ApiModel和ApiModelProperty这些注释就能在swagger中能看到更明白
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户实体类")//给实体类在swagger中加了文档注释
public class User {

    @ApiModelProperty("用户名")//给属性加注释
    public String name;
    @ApiModelProperty("用户昵称")
    public String nickname;


}
