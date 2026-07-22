FROM maven:3.9-eclipse-temurin-8 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline
COPY src src
RUN mvn -q -DskipTests package
FROM eclipse-temurin:8-jre
WORKDIR /app
RUN addgroup --system app && adduser --system --ingroup app app
COPY --from=build /app/target/samlong-api-1.0.0.jar app.jar
RUN mkdir -p /app/uploads && chown app:app /app/uploads
USER app
EXPOSE 8080
ENTRYPOINT ["java","-XX:+ExitOnOutOfMemoryError","-jar","app.jar"]
