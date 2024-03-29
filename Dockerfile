FROM gradle:7.3.1-jdk17 AS builder
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test -x asciidoctor --stacktrace

FROM openjdk:17-slim
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=embedded
ENV SPRING_BATCH_JOB_ENABLED=true
ENV SPRING_BATCH_JOB_NAMES=allRetrievalsJob
COPY --from=builder /home/gradle/src/build/*.jar /app/
WORKDIR /app
CMD ["java", "-jar", "league-troll-build.jar"]
