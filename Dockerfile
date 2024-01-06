FROM maven:3.8.7-eclipse-temurin-19-alpine
ENV JAVA_VERSION=19
MAINTAINER fghost.net

RUN apk add tar which gzip

ADD . /usr/src/fghost
WORKDIR /usr/src/fghost
EXPOSE 5001
ENTRYPOINT ["mvn", "clean", "package", "install", "-Dmaven.test.skip=true", "exec:java"]