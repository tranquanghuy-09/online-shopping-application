FROM eclipse-temurin:17-jre

WORKDIR /inventory-service

# Chạy lệnh Gradle để build ứng dụng
CMD ["./gradlew", "clean", "bootJar"]

# Copy file .jar vào thư mục /inventory-service/app.jar
COPY build/libs/*.jar /inventory-service/app.jar

# Expose cổng 8082
EXPOSE 8082

# Khởi động ứng dụng
ENTRYPOINT ["java", "-jar", "/inventory-service/app.jar"]
