FROM eclipse-temurin:17-jre

WORKDIR /customer-service

# Chạy lệnh Gradle để build ứng dụng
CMD ["./gradlew", "clean", "bootJar"]

# Copy file .jar vào thư mục /customer-service/app/
COPY build/libs/*.jar /customer-service/app/

# Expose cổng 8086
EXPOSE 8086

# Khởi động ứng dụng
ENTRYPOINT ["java", "-jar", "/customer-service/app/app.jar"]
