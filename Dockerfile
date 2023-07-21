# Stage 1: Build the application
FROM gradle:7.6.1-jdk17 AS build

WORKDIR /home/gradle

COPY . .

RUN gradle clean build -x test --no-daemon

# Stage 2: Run the application
FROM eclipse-temurin:17

WORKDIR /app

COPY /src/main/resources/env.properties /app/env.properties

COPY --from=build /home/gradle/build/libs/neodohae_spring_boot-0.0.1-SNAPSHOT.jar /app/neodohae_spring_boot.jar

ENTRYPOINT ["java", "-jar", "neodohae_spring_boot.jar"]