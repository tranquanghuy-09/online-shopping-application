FROM eclipse-temurin:17-jre

WORKDIR /api-gateway

CMD ["./gradlew", "clean", "bootJar"]

COPY build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
