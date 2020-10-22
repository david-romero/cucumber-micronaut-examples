FROM openjdk:14-alpine
COPY shared-payments-backend-runner/target/shared-payments-*.jar shared-payments.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "shared-payments.jar"]