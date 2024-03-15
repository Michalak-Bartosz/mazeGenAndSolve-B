FROM openjdk:16-jdk-alpine
EXPOSE 7979
ARG JAR_FILE=target/mazeGenAndSolve-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar","-web -webAllowOthers -tcp -tcpAllowOthers -browser"]