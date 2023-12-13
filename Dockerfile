FROM openjdk:17.0.2

COPY ./target/hello-0.0.1-SNAPSHOT.jar .

EXPOSE 8081

CMD ["java", "-jar", "hello-0.0.1-SNAPSHOT.jar"]
