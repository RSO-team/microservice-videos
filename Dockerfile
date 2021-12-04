FROM adoptopenjdk:15-jre-hotspot

RUN mkdir /app

WORKDIR /app

ADD ./basketball-videos-api/target/basketball-videos-api-1.0.0.jar /app

EXPOSE 8081

CMD ["java", "-jar", "basketball-videos-api-1.0.0.jar"]
