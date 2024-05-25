FROM eclipse-temurin:17-jre

WORKDIR /authentication-service

# Chạy lệnh Gradle để build ứng dụng
CMD ["./gradlew", "clean", "bootJar"]

# Copy file .jar vào thư mục /authentication-service/app/
COPY build/libs/*.jar /authentication-service/app/

# Expose cổng 8181
EXPOSE 8181

# Khởi động ứng dụng
ENTRYPOINT ["java", "-jar", "/authentication-service/app/app.jar"]
