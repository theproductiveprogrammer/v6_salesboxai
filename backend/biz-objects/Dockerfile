FROM openjdk:14-alpine
COPY build/libs/biz-objects-*-all.jar biz-objects.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "biz-objects.jar"]