FROM openjdk:14-alpine
COPY build/libs/lead-scoring-*-all.jar lead-scoring.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "lead-scoring.jar"]