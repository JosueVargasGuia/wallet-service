FROM openjdk:11
EXPOSE  9080
WORKDIR /app
ADD   ./target/*.jar /app/wallet-service.jar
ENTRYPOINT ["java","-jar","/app/wallet-service.jar"] 