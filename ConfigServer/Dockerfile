FROM eclipse-temurin:17-jre

WORKDIR /config-server

CMD ["./gradlew", "clean", "bootJar"]

COPY build/libs/*.jar app.jar

EXPOSE 9999

ENTRYPOINT ["java", "-jar", "app.jar"]
