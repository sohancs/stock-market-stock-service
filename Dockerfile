FROM openjdk:8
ADD target/stock-market-stock-service-1.0.0.jar stock-market-stock-service-1.0.0.jar
EXPOSE 9000
ENTRYPOINT ["java","-jar","stock-market-stock-service-1.0.0.jar"]