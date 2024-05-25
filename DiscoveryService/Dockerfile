FROM eclipse-temurin:17-jre

WORKDIR /discovery-service

# Chạy lệnh Gradle để build ứng dụng
CMD ["./gradlew", "clean", "bootJar"]

# Copy file .jar vào thư mục /discovery-service/app.jar
COPY build/libs/*.jar /discovery-service/app.jar

# Expose cổng 8761
EXPOSE 8761

# Khởi động ứng dụng
ENTRYPOINT ["java", "-jar", "/discovery-service/app.jar"]
