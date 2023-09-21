FROM openjdk:8-alpine
LABEL authors="joshua"

WORKDIR /app

ADD /yoj-backend-common/target/yoj-backend-common-1.0.0.jar yoj-common.jar
ADD /yoj-backend-model/target/yoj-backend-model-1.0.0.jar yoj-model.jar
ADD /yoj-backend-service-client/target/yoj-backend-service-client-1.0.0.jar yoj-service-client.jar

ADD /yoj-backend-user-service/target/yoj-backend-user-service-1.0.0.jar yoj-user-service.jar
ADD /yoj-backend-question-service/target/yoj-backend-question-service-1.0.0.jar yoj-question-service.jar
ADD /yoj-backend-judge-service/target/yoj-backend-judge-service-1.0.0.jar yoj-judge-service.jar
ADD /yoj-backend-code-sandbox/target/yoj-backend-code-sandbox-1.0.0.jar yoj-code-sandbox.jar
ADD /yoj-backend-gateway/target/yoj-backend-gateway-1.0.0.jar yoj-gateway.jar

EXPOSE 8101

CMD java -jar yoj-user-service.jar
CMD java -jar yoj-question-service.jar
CMD java -jar yoj-judge-service.jar
CMD java -jar yoj-code-sandbox.jar
CMD java -jar yoj-gateway.jar
