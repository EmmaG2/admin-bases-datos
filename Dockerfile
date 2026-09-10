FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B package

FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app
COPY --from=build /app/target/admin-bases-datos-1.0.0-SNAPSHOT.jar app.jar
COPY --from=build /app/target/lib ./lib
USER 10001:10001
ENTRYPOINT ["java", "-jar", "app.jar"]
