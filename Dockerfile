FROM openjdk:14-jdk-alpine as build
##FROM adoptopenjdk:11-jdk-hotspot
#
#WORKDIR /app
#
#COPY mvnw .
#COPY .mvn .mvn
#COPY pom.xml .
#
#RUN chmod +x ./mvnw
#
## download the dependency if needed or if the pom file is changed
#RUN ./mvnw dependency:go-offline -B
#
#COPY src src
#
#RUN ./mvnw package -DskipTests
#RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)
#
## Production Stage for Spring boot application image
#FROM openjdk:14-jdk-alpine as production
#ARG DEPENDENCY=/app/target/dependency
#
## Copy the dependency application file from build stage artifact
#COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
#COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
#COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
#
#ENTRYPOINT ["java", "-cp", "app:app/lib/*","package com.example.demo.DemoApplication"]

EXPOSE 8080

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
