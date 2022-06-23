package com.example.swaggerdemo.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;

import java.nio.charset.Charset;

/*
设置tomcat的参数,没什么卵用
 */
//@SpringBootConfiguration
public class WebServerConfiguration {

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.setPort(8082);
        factory.setUriEncoding(Charset.forName("utf-8"));//编码
        factory.addConnectorCustomizers(new MyTomcatConnectorCustomizer());
        return factory;
    }

    class MyTomcatConnectorCustomizer implements TomcatConnectorCustomizer {
        @Override
        public void customize(Connector connector) {
            Http11NioProtocol handler = (Http11NioProtocol)connector.getProtocolHandler();

            handler.setAcceptCount(1000);// 排队数

            handler.setMaxConnections(1000);// 最大连接数

            handler.setMaxThreads(500);// 线程池的最大线程数

            handler.setMinSpareThreads(50);// 最小线程数

            handler.setConnectionTimeout(20000);// 超时时间 20S

        }
    }

}