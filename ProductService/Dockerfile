FROM eclipse-temurin:17-jre

WORKDIR /product-service

# Chạy lệnh Gradle để build ứng dụng
CMD ["./gradlew", "clean", "bootJar"]

# Copy file .jar vào thư mục /product-service/app.jar
COPY build/libs/*.jar /product-service/app/

# Expose cổng 8087
EXPOSE 8087

# Khởi động ứng dụng
ENTRYPOINT ["java", "-jar", "/product-service/app/app.jar"]
