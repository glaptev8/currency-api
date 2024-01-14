FROM openjdk:21-jdk-slim-buster
WORKDIR /currency

COPY build/libs/currency-api-1.0-SNAPSHOT.jar /currency/build/

WORKDIR /currency/build

EXPOSE 8080

ENTRYPOINT java -jar currency-api-1.0-SNAPSHOT.jar