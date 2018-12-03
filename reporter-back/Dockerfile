FROM openjdk:8-jdk-alpine

ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.simon.wa.ReporterBackApplication"]

EXPOSE 8080

RUN apk update
RUN apk add haveged
RUN apk add curl

HEALTHCHECK --interval=30s --timeout=5s --start-period=300s \
  CMD curl -sS http://127.0.0.1:8080/login || exit 1
