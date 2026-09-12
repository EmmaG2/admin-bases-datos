FROM nixos/nix:2.35.2 AS build
WORKDIR /app
COPY shell.nix .
RUN nix-shell --run 'java -version && mvn --version'
COPY pom.xml .
COPY src ./src
RUN nix-shell --run 'mvn -B -Dmaven.test.skip=true package'

FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app
COPY --from=build /app/target/admin-bases-datos-1.0.0-SNAPSHOT.jar app.jar
COPY --from=build /app/target/lib ./lib
USER 10001:10001
ENTRYPOINT ["java", "-jar", "app.jar"]
