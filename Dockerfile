FROM java:8
MAINTAINER author-info
#cypher.jar 与上面的pom对应
ADD target/cypher.jar app.jar
EXPOSE 9999 #暴露端口 与配置文件对应
#激活名为 appliction-docker.yml 或 appliction-docker.properties 的配置
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=docker"]

# 可能需要一个Nexus，然后deploy就把代码打包到nexus，然后用Portainer去nexus拉去我的包，拉取后就能运行了