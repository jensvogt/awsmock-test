FROM openjdk:21-jdk-slim

ARG VERSION

RUN mkdir /root/.aws
COPY awsmock-test-${VERSION}-SNAPSHOT.jar awsmock-test.jar
COPY config /root/.aws/config
COPY credentials /root/.aws/credentials

ENV AWS_PROFILE=awsmock
EXPOSE 10100 10100

ENTRYPOINT ["java","-jar","/awsmock-test.jar"]
