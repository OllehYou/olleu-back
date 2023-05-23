FROM openjdk:17

COPY ./build/libs /app

WORKDIR /app

CMD ["java", "-jar", "olleu-back-0.0.1-SNAPSHOT.jar"]
