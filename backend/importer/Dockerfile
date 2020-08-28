FROM openjdk:14-alpine
COPY build/libs/importer-*-all.jar importer.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "importer.jar"]