FROM eclipse-temurin:21-alpine
VOLUME /tmp
EXPOSE 8080
ADD target/Sample-0.0.1-SNAPSHOT.jar samplefile.jar
ENTRYPOINT [ "sh", "-c", "java -jar /samplefile.jar" ]