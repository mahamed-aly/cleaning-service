FROM openjdk:17-alpine
WORKDIR /app
COPY target/cleaning-service-0.0.1-SNAPSHOT.jar  cleaning-service.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","cleaning-service.jar"]