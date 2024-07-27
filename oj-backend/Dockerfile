FROM openjdk:8-alpine

COPY ./target/oj-backend-0.0.1-SNAPSHOT.jar /app/target/

CMD ["java","-jar","/app/target/oj-backend-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]