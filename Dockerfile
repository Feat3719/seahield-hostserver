FROM openjdk:17
COPY src/main/resources/keystore.p12 /keystore.p12
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
