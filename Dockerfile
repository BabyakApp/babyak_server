FROM adoptopenjdk/openjdk11
ARG JAR_FILE=./build/libs/babyak-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
