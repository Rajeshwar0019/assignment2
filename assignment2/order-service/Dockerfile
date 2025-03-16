FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
COPY target/order-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 7002
ENTRYPOINT ["java", "-jar", "/app.jar"]
