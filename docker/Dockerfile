FROM openjdk:21-jdk-slim

RUN mkdir /root/.aws
COPY awsmock-spring-test-0.0.1-SNAPSHOT.jar awsmock-spring-test.jar
COPY config /root/.aws/config
COPY credentials /root/.aws/credentials

ENV AWS_PROFILE=awsmock
EXPOSE 10100 10100

ENTRYPOINT ["java","-jar","/awsmock-spring-test.jar"]
