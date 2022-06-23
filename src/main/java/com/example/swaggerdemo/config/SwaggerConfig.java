package com.example.swaggerdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * @author wangzy
 * @date 2022/4/17 15:10
 *
 * swagger的配置
 *
 * 有了这个配置类后就可以通过 http://ip:port/swagger-ui.html 访问swagger了
 * 注意如果是spring-boot最新版2.6.6等较新的版本启动会报错，换成2.4.0版本就没问题了
 *
 * 报错Unable to infer base url. This is common when using dynamic servlet registration or when the API is
 * 就在工程配置文件中 修i该配置spring.profiles.active=dev 这是开发环境
 * 因为接口测试只适用于开发环境嘛
 *
 * 配置swagger
 *      Swagger配置的实例 docket
 *
 * 1、通过enable()方法配置是否启用swagger，如果是false，swagger将不能在浏览器中访问了
 *
 *   // 设置要显示swagger的环境
 *    Profiles of = Profiles.of("dev", "test");
 *    // 判断当前是否处于该环境
 *    // 通过 enable() 接收此参数判断是否要显示
 *    boolean b = environment.acceptsProfiles(of);
 *
 *    docket.enable(b);
 *
 *
 */
@Configuration
@EnableSwagger2//开启swagger
public class SwaggerConfig {

    //配置了swagger的Docket实例
    @Bean
    public Docket createRestApi(){
        DocumentationType swagger2 = DocumentationType.SWAGGER_2;//new DocumentationType("swagger", "2.0");
        DocumentationType mySwagger = new DocumentationType("MySwagger", "1.0");
       // new Docket(DocumentationType.SWAGGER_2);
        Docket docket = new Docket(mySwagger);
        docket.apiInfo(swaggerInfo());

        //这样就是指定了扫描这个包下的接口 basePackage指定要扫描的包 ,any()扫描全部，none()不扫描
        //withClassAnnotation 扫描类上的注解，withMethodAnnotation 扫描方法上的注解---需要的参数是一个注解的class
        //RequestHandlerSelectors.withMethodAnnotation(GetMapping.class);
        //RequestHandlerSelectors.withClassAnnotation(RestController.class);
        docket.select().apis(RequestHandlerSelectors.basePackage("com.example.swaggerdemo.controller"))
               .paths(PathSelectors.any()) //路径，any任何路径
                .build();

        //可以设置为false就是不能访问swagger
        docket.enable(true);

        docket.groupName("wang's team");//文档的分组组名
        //可以返回多个docket,然后通过在每个不同的docket里面用不同的接口包扫描或者路径过滤啥的
        // 每个docket里面就有不同的，一个docket就是一个接口的分组，然后设置groupName，就能分组管理接口

        return docket;
    }

    @Bean
    public Docket restApi2(){
        return new Docket(DocumentationType.SWAGGER_2).groupName("分组二");
    }
    @Bean
    public Docket restApi3(){
        return new Docket(DocumentationType.SWAGGER_2).groupName("分组三");
    }


    //配置Swagger信息=apiInfo
    private ApiInfo swaggerInfo(){

        //作者信息
        Contact contact = new Contact("wangzy", "www.wangzy.com", "wangzy.@xxx.com");

        return new ApiInfo(
                "My Api Document", //swagger-ui的title
                "This is my first Api Documentation", //api文档的描述
                "1.0" , //版本
                "urn:tos", //开发团队的地址
                contact, //作者信息
                "Apache 2.0",
                "http://www.apache.llll",
                new ArrayList<>());
    }

}
