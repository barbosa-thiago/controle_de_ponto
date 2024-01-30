FROM maven:3-openjdk-11-slim AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:11
COPY --from=build target/controle-de-ponto-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]