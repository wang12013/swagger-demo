FROM java:8
MAINTAINER author-info
#cypher.jar 与上面的pom对应
ADD target/cypher.jar app.jar
EXPOSE 9999 #暴露端口 与配置文件对应
#激活名为 appliction-docker.yml 或 appliction-docker.properties 的配置
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=docker"]