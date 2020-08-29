FROM openjdk:14-alpine
COPY build/libs/workflow-engine-*-all.jar workflow-engine.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "workflow-engine.jar"]