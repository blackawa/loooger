FROM openjdk:8u151-jdk-alpine
COPY target/front.web-0.1.0-SNAPSHOT-standalone.jar /usr/src/app.jar
WORKDIR /usr/src
CMD ["java", "-jar", "app.jar"]
