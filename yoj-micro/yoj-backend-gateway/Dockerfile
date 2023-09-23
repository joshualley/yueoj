FROM openjdk:8-alpine
LABEL authors="joshua"

WORKDIR /app

ADD /yoj-backend-gateway/target/yoj-backend-gateway-1.0.0.jar yoj-gateway.jar

EXPOSE 8101

CMD java -jar yoj-gateway.jar