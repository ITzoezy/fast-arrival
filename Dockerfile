#版本为java8
FROM java:8
#将所有jar包复制
COPY *.jar /app.jar
#服务端口号
CMD ["--server.port=8080"]
#向外面暴露的端口
EXPOSE 8080
#容器启动的时候运行的命令
ENTRYPOINT ["java","-jar","/app.jar"]
