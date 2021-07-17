FROM openjdk:8
ADD target/stock-service-0.0.1-SNAPSHOT.jar stock-service-0.0.1-SNAPSHOT.jar
EXPOSE 9000
ENTRYPOINT ["java","-jar","stock-service-0.0.1-SNAPSHOT.jar"]