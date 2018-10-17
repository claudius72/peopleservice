FROM openjdk:8-jdk-alpine
WORKDIR D:\\users\\claudiu\\GitRepo\\peopleservice
VOLUME d:\\temp
RUN sh -c 'touch /app.jar'
ARG JAR_FILE
ADD target/PeopleService-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 8081
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:+UseStringDeduplication"
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar
