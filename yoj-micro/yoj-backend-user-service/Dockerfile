FROM openjdk:8-alpine
LABEL authors="joshua"

WORKDIR /app

ADD /yoj-backend-common/target/yoj-backend-common-1.0.0.jar yoj-common.jar
ADD /yoj-backend-model/target/yoj-backend-model-1.0.0.jar yoj-model.jar
ADD /yoj-backend-service-client/target/yoj-backend-service-client-1.0.0.jar yoj-service-client.jar

ADD /yoj-backend-user-service/target/yoj-backend-user-service-1.0.0.jar yoj-user-service.jar

EXPOSE 8102

CMD java -jar yoj-user-service.jar