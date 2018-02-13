FROM openjdk:8u151-jdk-alpine
COPY target/uberjar/auth.proxy-0.1.0-SNAPSHOT-standalone.jar /usr/src/app.jar
WORKDIR /usr/src
CMD ["java", "-jar", "app.jar"]
