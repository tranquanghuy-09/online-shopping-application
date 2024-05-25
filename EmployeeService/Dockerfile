FROM eclipse-temurin:17-jre

WORKDIR /employee-service

# Chạy lệnh Gradle để build ứng dụng
CMD ["./gradlew", "clean", "bootJar"]

# Copy file .jar vào thư mục /employee-service/app.jar
COPY build/libs/*.jar /employee-service/app/

# Expose cổng 8085
EXPOSE 8085

# Khởi động ứng dụng
ENTRYPOINT ["java", "-jar", "/employee-service/app/app.jar"]
