# Stage 1: Build the application
FROM gradle:7.6.1-jdk17 AS build

WORKDIR /home/gradle/src

COPY . .

RUN gradle clean build --no-daemon

# Stage 2: Run the application
FROM eclipse-temurin:17

WORKDIR /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/neodohae_spring_boot.jar

ENTRYPOINT ["java", "-jar", "neodohae_spring_boot.jar"]