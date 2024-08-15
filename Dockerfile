# syntax=docker/dockerfile:1

FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /opt/library-service
COPY pom.xml ./
RUN mvn dependency:resolve
COPY ./src ./src
RUN mvn clean package -Dmaven.test.skip=true

FROM maven:3.9.6-eclipse-temurin-17-alpine AS develop
WORKDIR /opt/library-service
COPY --from=build /opt/library-service/target/*.jar ./app.jar
ENTRYPOINT [ "sh","-c","java -jar app.jar --spring.profiles.active=dev" ]
