FROM adoptopenjdk:15-jre-hotspot

RUN mkdir /app

WORKDIR /app

ADD ./api/target /app

EXPOSE 8080

CMD ["java", "-cp", "classes:dependency/*","com.kumuluz.ee.EeApplication"]