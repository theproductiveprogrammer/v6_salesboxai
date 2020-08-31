FROM openjdk:14-alpine
COPY build/libs/event-server-*-all.jar event-server.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "event-server.jar"]