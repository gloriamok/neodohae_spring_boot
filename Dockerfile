FROM eclipse-temurin:17

WORKDIR /app

COPY build/libs/neodohae_spring_boot-0.0.1-SNAPSHOT.jar /app/neodohae_spring_boot.jar

ENTRYPOINT ["java", "-jar", "neodohae_spring_boot.jar"]