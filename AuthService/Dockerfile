FROM eclipse-temurin:17-jre

WORKDIR /authentication-service

CMD ["./gradlew", "clean", "bootJar"]

COPY build/libs/*.jar app.jar

EXPOSE 8181

ENTRYPOINT ["java", "-jar", "app.jar"]
