FROM eclipse-temurin:17-jre

WORKDIR /order-service

# Chạy lệnh Gradle để build ứng dụng
CMD ["./gradlew", "clean", "bootJar"]

# Copy file .jar vào thư mục /order-service/app.jar
COPY build/libs/*.jar /order-service/app/

# Expose cổng 8081
EXPOSE 8081

# Khởi động ứng dụng
ENTRYPOINT ["java", "-jar", "/order-service/app/app.jar"]
