FROM openjdk:14-alpine
COPY build/libs/activities-*-all.jar activities.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "activities.jar"]