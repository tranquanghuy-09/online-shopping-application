FROM eclipse-temurin:17-jre

WORKDIR /api-gateway

CMD ["./gradlew", "clean", "bootJar"]

COPY build/libs/*.jar /api-gateway/app/

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/api-gateway/app/app.jar"]
