FROM java:openjdk-8-jdk

ADD /target/dailymenu-services-thorntail.jar /opt/dailymenu-services-thorntail.jar

ARG ZOMATO_API_KEY
ENV ZOMATO_API_KEY $ZOMATO_API_KEY

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/dailymenu-services-thorntail.jar"]