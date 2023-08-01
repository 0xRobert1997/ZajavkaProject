FROM eclipse-temurin:17
COPY build/libs/*.jar app.jar
CMD ["java","-jar","/app.jar"]
EXPOSE 8080